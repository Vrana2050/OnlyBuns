package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentCreateDto;
import rs.ac.uns.ftn.onlybunsapp.model.Comment;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.service.CommentService;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity commentPost(@AuthenticationPrincipal User user,@Valid @RequestBody CommentCreateDto commentCreateDto) {
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(this.commentService.CreateComment(user, commentCreateDto));
        } catch (AccessDeniedException exception)
        {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }
}
