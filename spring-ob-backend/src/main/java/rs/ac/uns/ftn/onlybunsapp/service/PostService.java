package rs.ac.uns.ftn.onlybunsapp.service;

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

}
