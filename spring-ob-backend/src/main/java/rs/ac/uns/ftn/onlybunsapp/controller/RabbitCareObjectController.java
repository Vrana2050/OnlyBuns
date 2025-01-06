package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostReadDto;
import rs.ac.uns.ftn.onlybunsapp.model.RabbitCareObject;
import rs.ac.uns.ftn.onlybunsapp.repository.RabbitCareObjectRepository;

import java.util.List;

@RestController
@RequestMapping("/api/rabbitCareObjects")
public class RabbitCareObjectController {

    @Autowired
    private RabbitCareObjectRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PreAuthorize("hasRole('USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RabbitCareObject> getAll() {
        return this.repository.findAll();
    }


}
