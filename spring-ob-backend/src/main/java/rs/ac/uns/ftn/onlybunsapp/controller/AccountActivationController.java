package rs.ac.uns.ftn.onlybunsapp.controller;

import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountActivationController {

    @Autowired
    private UserService userService;

    @GetMapping("/activate/{userId}")
    public ResponseEntity<String> activateAccount(@PathVariable("userId") long userId) {
        if (userService.activateUser(userId))
            return ResponseEntity.ok("Account activated successfully!");

        return ResponseEntity.badRequest().body("Account activation failed!.");
    }
}
