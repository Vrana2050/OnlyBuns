package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostCreateDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostReadDto;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.service.PostService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PostService postService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostReadDto createPost(@AuthenticationPrincipal User user, @RequestPart("postDto") PostCreateDto postDto,
                                  @RequestPart("image") MultipartFile image) {
        postDto.setImage(image);
        return this.postService.create(postDto,user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostReadDto> getAll() {
        return this.postService.getAll();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/like/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean likePost(@AuthenticationPrincipal User user, @PathVariable long postId) {
        return this.postService.like(user, postId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(value = "/unlike/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean unlikePost(@AuthenticationPrincipal User user, @PathVariable long postId) {
        return this.postService.unlike(user, postId);
    }


    @GetMapping(value="/getAllSortedByTime")
    public List<PostReadDto> getAllSortedByTime() {
        return this.postService.getAllSortedByDate();
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping(value = "/following", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostReadDto> getFollowedPosts(@AuthenticationPrincipal User user) {
        return postService.getPostsFromFollowingUsers(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/delete",produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deletePost(@AuthenticationPrincipal User user, @PathVariable long postId) {
        return postService.delete(user,postId);
    }



}
