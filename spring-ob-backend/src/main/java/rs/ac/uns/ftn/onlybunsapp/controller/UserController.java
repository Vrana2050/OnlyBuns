package rs.ac.uns.ftn.onlybunsapp.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.onlybunsapp.dto.AdminUserList;
import rs.ac.uns.ftn.onlybunsapp.dto.PaginationRequest;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;


// Primer kontrolera cijim metodama mogu pristupiti samo autorizovani korisnici
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	// Za pristup ovoj metodi neophodno je da ulogovani korisnik ima ADMIN ulogu
	// Ukoliko nema, server ce vratiti gresku 403 Forbidden
	// Korisnik jeste autentifikovan, ali nije autorizovan da pristupi resursu
	@GetMapping("/user/{userId}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")

	public User loadById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}

	@GetMapping("/user/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> loadAll() {
		List<User> users = this.userService.findAll();
		return users;
	}
	@PostMapping("/user/allUsersFiltered")
	@PreAuthorize("hasRole('ADMIN')")
	public Page<User> getUsersFiltered(@RequestBody PaginationRequest p) {
		Page<User> filteredUsers = this.userService.probaPaginacije(p);
		return filteredUsers;
	}


	@GetMapping("/whoami")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public User user(Principal user) {
		return this.userService.findByUsername(user.getName());
	}
	
	@GetMapping("/foo")
    public Map<String, String> getFoo() {
        Map<String, String> fooObj = new HashMap<>();
        fooObj.put("foo", "bar");
        return fooObj;
    }

	@GetMapping("/getById/{userId}")
	public User getById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}


	@GetMapping("/getUsersThatLikedMost")
	public List<User> getUsersThatLikedMost(){
		//return userService.getTop10UsersThatLikedMost();
		return null;
	}

	@PostMapping("/{userId}/follow")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> followUser(@PathVariable long userId, @AuthenticationPrincipal User currentUser) {
		userService.followUser(currentUser.getId(), userId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{userId}/unfollow")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<?> unfollowUser(@PathVariable long userId, @AuthenticationPrincipal User currentUser) {
		userService.unfollowUser(currentUser.getId(), userId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{userId}/is-following")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Boolean> isFollowing(@PathVariable long userId, @AuthenticationPrincipal User currentUser) {
		boolean isFollowing = userService.isFollowing(currentUser.getId(), userId);
		return ResponseEntity.ok(isFollowing);
	}


}
