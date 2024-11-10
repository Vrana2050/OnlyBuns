package rs.ac.uns.ftn.onlybunsapp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import rs.ac.uns.ftn.onlybunsapp.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.onlybunsapp.dto.UserRequest;
import rs.ac.uns.ftn.onlybunsapp.dto.UserTokenState;
import rs.ac.uns.ftn.onlybunsapp.exception.ResourceConflictException;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;
import rs.ac.uns.ftn.onlybunsapp.service.impl.EmailSenderService;
import rs.ac.uns.ftn.onlybunsapp.util.TokenUtils;

import java.util.Map;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailSenderService emailSenderService;

	// Prvi endpoint koji pogadja korisnik kada se loguje.
	// Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
		// Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
		// AuthenticationException
		System.out.println(authenticationRequest.getEmail() + authenticationRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getEmail(), authenticationRequest.getPassword()));

		// Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
		// kontekst
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token za tog korisnika
		User user = (User) authentication.getPrincipal();
		if(!user.isEnabled())
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		String jwt = tokenUtils.generateToken(user.getEmail());
		int expiresIn = tokenUtils.getExpiredIn();

		// Vrati token kao odgovor na uspesnu autentifikaciju
		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}

	// Endpoint za registraciju novog korisnika
	@PostMapping("/signup")
	public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {

		// Check if username already exists
		if (this.userService.findByUsername(userRequest.getUsername()) != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					Map.of("field", "username", "error", "Username already exists")
			);
		}

		// Check if email already exists
		if (this.userService.findByEmail(userRequest.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					Map.of("field", "email", "error", "Email already in use")
			);
		}

		// Save new user if validations pass
		User user = this.userService.save(userRequest);

		// Send account activation email
		emailSenderService.sendAccountActivationEmail(user);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

}