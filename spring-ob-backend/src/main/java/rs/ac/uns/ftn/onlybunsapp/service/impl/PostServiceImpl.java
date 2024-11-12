package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentReadDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostCreateDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostReadDto;
import rs.ac.uns.ftn.onlybunsapp.mapper.CommentMapper;
import rs.ac.uns.ftn.onlybunsapp.mapper.PostMapper;
import rs.ac.uns.ftn.onlybunsapp.mapper.UserMapper;
import rs.ac.uns.ftn.onlybunsapp.model.Comment;
import rs.ac.uns.ftn.onlybunsapp.model.Post;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.LikeRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.PostRepository;
import rs.ac.uns.ftn.onlybunsapp.service.ImageService;
import rs.ac.uns.ftn.onlybunsapp.service.LikeService;
import rs.ac.uns.ftn.onlybunsapp.service.PostService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<PostReadDto> getAll() {
        List<Post> posts = postRepository.findAll();
        List<PostReadDto> postReadDtos = new ArrayList<>();
        for (Post post : posts) {
            PostReadDto postReadDto = postMapper.toPostReadDto(post);
            postReadDto.creator = userMapper.toUserReadDto(post.getCreator());
            for(Comment comment : post.getComments()){
                postReadDto.comments.add(commentMapper.toCommentReadDto(comment));
            }
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
        return postMapper.toPostReadDto(newPost);
    }

    @Override
    public Boolean like(User user, long postId) {
        try {
            Post post = postRepository.getById(postId);
            if (!likeService.save(user.getId(), postId))
                return false;
            post.addLike();
            postRepository.save(post);
            return true;
        }
        catch (EntityNotFoundException e) {
            return false;
        }
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
}
