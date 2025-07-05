package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentReadDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.LocationDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostCreateDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostReadDto;
import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserLikesDto;
import rs.ac.uns.ftn.onlybunsapp.mapper.CommentMapper;
import rs.ac.uns.ftn.onlybunsapp.mapper.LocationMapperImpl;
import rs.ac.uns.ftn.onlybunsapp.mapper.PostMapper;
import rs.ac.uns.ftn.onlybunsapp.mapper.UserMapper;
import rs.ac.uns.ftn.onlybunsapp.model.Comment;
import rs.ac.uns.ftn.onlybunsapp.model.Location;
import rs.ac.uns.ftn.onlybunsapp.model.Post;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.producer.Producer;
import rs.ac.uns.ftn.onlybunsapp.repository.LikeRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.PostRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.ImageService;
import rs.ac.uns.ftn.onlybunsapp.service.LikeService;
import rs.ac.uns.ftn.onlybunsapp.service.LocationService;
import rs.ac.uns.ftn.onlybunsapp.service.PostService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LocationService locationService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserMapper userMapper;
    public int gas=0;
    @Autowired
    private Producer producer;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private LocationMapperImpl locationMapperImpl;

    @Override
    public List<PostReadDto> getAll() {
        List<Post> posts = postRepository.findAll();
        List<PostReadDto> postReadDtos = new ArrayList<>();
        for (Post post : posts) {
            post.getComments().sort((c1, c2) -> c2.getCreated().compareTo(c1.getCreated()));
            PostReadDto postReadDto = postMapper.toPostReadDto(post);
            postReadDto.setImageBase64(imageService.toImageBase64(post.getFolderPath()));
            Location location = locationService.getById(post.getLocation().getId());
            postReadDto.setLocation(locationMapperImpl.toLocationDto(location));
            postReadDtos.add(postReadDto);
        }
        return postReadDtos;
    }

    @Override
    public PostReadDto create(PostCreateDto postDto, User creator) {
        Post post = postMapper.toPostDomain(postDto);
        post.publish(creator);
        post.setFolderPath(imageService.saveImage(postDto.getImage(),creator.getUsername()));
        Post newPost = postRepository.save(post);
        locationService.cacheLocation(newPost.getLocation());
        return postMapper.toPostReadDto(newPost);
    }

    @Override
    @Transactional
    public Boolean like(User user, long postId)throws Exception {
            gas++;
            Post post = postRepository.getById(postId);
            if (!likeService.save(user.getId(), postId))
                return false;
            post.addLike();
            if (gas == 1) {
                Thread.sleep(3000);
            }
            postRepository.save(post);
            return true;
    }

    @Override
    public Boolean unlike(User user, long postId) {
        try {
            Post post = postRepository.getById(postId);

            if (!likeService.delete(user.getId(), postId))
                return false;
            post.removeLike();
            postRepository.save(post);
            return true;
        }
        catch(EntityNotFoundException ex) {
            return false;
        }
    }


    @Override
    public List<PostReadDto> getAllSortedByDate() {
        List<PostReadDto> unsortedPosts = getAll();
        unsortedPosts.sort(Comparator.comparing(PostReadDto::getPostDate).reversed());

        return new ArrayList<>(unsortedPosts);
    }

    public List<PostReadDto> getPostsFromFollowingUsers(User user) {

        List<User> followingUsers = user.getFollowing();

        if (followingUsers.isEmpty()) {
            return List.of();
        }
        List<Post> posts = postRepository.findAllByCreatorInAndIsDeletedFalseAndIsRestrictedFalseOrderByPostDateDesc(followingUsers);

        List<PostReadDto> postReadDtos = new ArrayList<>();
        for (Post post : posts) {
            post.getComments().sort((c1, c2) -> c2.getCreated().compareTo(c1.getCreated()));
            PostReadDto postReadDto = postMapper.toPostReadDto(post);
            postReadDto.setImageBase64(imageService.toImageBase64(post.getFolderPath()));
            postReadDtos.add(postReadDto);
        }
        return postReadDtos;
    }

    @Override
    public Boolean delete(User user, long postId) {
        Post post = postRepository.getById(postId);
        if(post.getCreator().getId() != user.getId()){
            return false;
        }
        postRepository.delete(post);
        return true;
    }

    public List<PostReadDto> getPostsForUser(User user) {
        List<Post> posts = postRepository.findAllByUserIdAndNotDeletedAndNotRestricted(user.getId());
        List<PostReadDto> postReadDtos = new ArrayList<>();
        for (Post post : posts) {
            post.getComments().sort((c1, c2) -> c2.getCreated().compareTo(c1.getCreated()));
            PostReadDto postReadDto = postMapper.toPostReadDto(post);
            postReadDto.setImageBase64(imageService.toImageBase64(post.getFolderPath()));
            postReadDtos.add(postReadDto);
        }
        return postReadDtos;
    }

    public PostReadDto editDescription(User user, long postId, String newDescription) {
        Post post = postRepository.findById(postId);
        post.setDescription(newDescription);
        postRepository.save(post);
        PostReadDto postReadDto = postMapper.toPostReadDto(post);
        postReadDto.setImageBase64(imageService.toImageBase64(post.getFolderPath()));
        return postReadDto;
    }

    @Override
    public Boolean sendPostsToAgencies(List<Long> postIds) {
        List<Post>selectedPosts = postRepository.findAllById(postIds);
        StringBuilder text = new StringBuilder();
        for (Post post : selectedPosts) {
            text.append(createMessage(post.getDescription(),post.getCreator().getUsername(),post.getPostDate().getTime()));
        }
        producer.sendFanoutMessage("agenciesFanoutExchange",text.toString());
        return true;
    }
    public static String createMessage(String description, String username, long publishedAtTimestamp) {
        LocalDateTime publishedAt = Instant.ofEpochMilli(publishedAtTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return String.format(
                "Nova objava! 📢\n\n" +
                        "📝 Opis: %s\n\n" +
                        "👤 Korisnik: %s\n\n" +
                        "⏰ Vreme objavljivanja: %s\n",
                description,
                username,
                publishedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))
        );
    }
    public int countAllTimePosts() {
        return postRepository.countAllPosts();
    }

    @Override
    public int countThisMonthPosts() {
        return postRepository.countThisMonthPosts();
    }

    @Override
    public List<PostReadDto> getTop5MostLikedPostsLast7Days() {
        Pageable top5 = PageRequest.of(0,5);
        List<Post> posts = postRepository.getTop5MostLikedPostsLast7Days(top5);
        List<PostReadDto> postReadDtos = new ArrayList<>();
        for (Post post : posts) {
            PostReadDto postReadDto = postMapper.toPostReadDto(post);
            postReadDto.setImageBase64(imageService.toImageBase64(post.getFolderPath()));
            postReadDtos.add(postReadDto);
        }
        return postReadDtos;
    }

    @Override
    public List<PostReadDto> getTop10MostLikedPostsOfAllTime() {
        Pageable top5 = PageRequest.of(0,10);
        List<Post> posts = postRepository.getTop10MostLikedPosts(top5);
        List<PostReadDto> postReadDtos = new ArrayList<>();
        for (Post post : posts) {
            PostReadDto postReadDto = postMapper.toPostReadDto(post);
            postReadDto.setImageBase64(imageService.toImageBase64(post.getFolderPath()));
            postReadDtos.add(postReadDto);
        }
        return postReadDtos;
    }

    @Override
    public List<UserLikesDto> findTop10UsersByLikesGivenThisWeek() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> results = postRepository.findTop10UsersByLikesGivenThisWeek(pageable);
        System.out.println("LUKA VRANA ALFA");
        System.out.println(results.size());
        System.out.println("LUKA VRANA ALFA");

        // Map results to DTO
        return results.stream()
                .map(row -> new UserLikesDto((User) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }


}
