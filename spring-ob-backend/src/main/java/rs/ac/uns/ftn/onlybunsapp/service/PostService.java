package rs.ac.uns.ftn.onlybunsapp.service;

import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostCreateDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostReadDto;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.util.List;

public interface PostService {
    public List<PostReadDto> getAll();
    public PostReadDto create(PostCreateDto postDto, User user);
    public Boolean like(User user,long postId);
    public Boolean unlike(User user,long postId);
    public List<PostReadDto> getAllSortedByDate();


    List<PostReadDto> getPostsFromFollowingUsers(User user);
    public Boolean delete(User user,long postId);

    public List<PostReadDto> getPostsForUser(User user);
    public PostReadDto editDescription(User user, long postId, String newDescription);
    public int countAllTimePosts();
    public int countThisMonthPosts();
    public List<PostReadDto> getTop5MostLikedPostsLast7Days();
    public List<PostReadDto> getTop10MostLikedPostsOfAllTime();
}
