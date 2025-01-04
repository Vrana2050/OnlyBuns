package rs.ac.uns.ftn.onlybunsapp.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import ch.qos.logback.core.CoreConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.onlybunsapp.dto.AdminUserList;
import rs.ac.uns.ftn.onlybunsapp.dto.PaginationRequest;
import rs.ac.uns.ftn.onlybunsapp.dto.UserRequest;
import rs.ac.uns.ftn.onlybunsapp.model.Post;
import rs.ac.uns.ftn.onlybunsapp.model.PostUserLike;
import rs.ac.uns.ftn.onlybunsapp.model.Role;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.CommentRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.LikeRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.PostRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.LikeService;
import rs.ac.uns.ftn.onlybunsapp.service.PostService;
import rs.ac.uns.ftn.onlybunsapp.service.RoleService;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;
import rs.ac.uns.ftn.onlybunsapp.util.TokenUtils;

import javax.persistence.EntityNotFoundException;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private RoleService roleService;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	public User findById(Long id) throws AccessDeniedException {
		return userRepository.findById(id).orElseGet(null);
	}

	public List<User> findAll() throws AccessDeniedException {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());

		// pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
		// treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));

		u.setFirstName(userRequest.getFirstname());
		u.setLastName(userRequest.getLastname());
		u.setEnabled(false);
		u.setEmail(userRequest.getEmail());
		u.setAddress(userRequest.getAddress());

		// u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
		List<Role> roles = roleService.findByName("ROLE_USER");
		u.setRoles(roles);

		return this.userRepository.save(u);
	}


	public Page<User> probaPaginacije(PaginationRequest p) {
		// Set up the Pageable with sorting information
		Pageable pageable = PageRequest.of(
				p.getPage(),
				p.getSize(),
				"desc".equalsIgnoreCase(p.getSortDirection())
						? Sort.by(p.getSortBy()).descending()
						: Sort.by(p.getSortBy()).ascending()
		);

		// Query the database with the filtered parameters
		return userRepository.findAllWithFilters(
				p.getFirstName(),
				p.getLastName(),
				p.getEmail(),
				p.getMinPosts(),
				p.getMaxPosts(),
				pageable
		);

	}

	public User update (User updatedUser)throws AccessDeniedException {
		if (userRepository.findById(updatedUser.getId()).isPresent()) {
			return this.userRepository.save(updatedUser);
		}
		return null;
	}

	public boolean activateUser ( long userId){
		User user = userRepository.findById(userId).orElse(null);
		if (user != null) {
			user.setEnabled(true);
			return update(user) != null;
		}
		return false;
	}

	@Override
	public User findByEmail (String email){
		return userRepository.findByEmail(email);

	}
	public void followUser(long followerId, long followingId) {
		User follower = userRepository.findById(followerId)
				.orElseThrow(() -> new EntityNotFoundException("Follower not found"));
		User following = userRepository.findById(followingId)
				.orElseThrow(() -> new EntityNotFoundException("Following not found"));

		if (userRepository.isFollowing(followerId, followingId)) {
			throw new IllegalStateException("Already following this user");
		}

		follower.setNumberOfFollowing(follower.getNumberOfFollowing() - 1);
		following.setNumberOfFollowers(following.getNumberOfFollowers() - 1);

		userRepository.save(follower);
		userRepository.save(following);
	}
/*
	@Override
	public List<User> getTop10UsersThatLikedMost() {
		return userRepository.getTop10UsersThatLikedTheMost();
	}*/


	public void unfollowUser(long followerId, long followingId) {
		User follower = userRepository.findById(followerId)
				.orElseThrow(() -> new EntityNotFoundException("Follower not found"));
		User following = userRepository.findById(followingId)
				.orElseThrow(() -> new EntityNotFoundException("Following not found"));

		// Check if not following
		if (!userRepository.isFollowing(followerId, followingId)) {
			throw new IllegalStateException("Not following this user");
		}
		follower.setNumberOfFollowing(follower.getNumberOfFollowing() - 1);
		following.setNumberOfFollowers(following.getNumberOfFollowers() - 1);

		userRepository.save(follower);
		userRepository.save(following);

		userRepository.deleteFollowRelation(followerId, followingId);
	}

	public boolean isFollowing(long followerId, long followingId) {
		return userRepository.isFollowing(followerId, followingId);
	}


	@Override
	public void SendEmailToInactiveUsers() {
		List<User> inactiveUsers = this.userRepository.getAllByLastLoginDateBefore(Date.from(Instant.now().minus(Duration.ofDays(7))));
	    for (User user : inactiveUsers) {
			String subject = "Whats new on OnlyBuns";

			int newLikes = GetNumberOfUnseenLikes(user);
			int newPosts = GetNumberOfUnseenPosts(user);
			int newComments =GetNumberOfUnseenComments(user);
			String body = String.format(
					"Hello %s,\n\n" +
							"We've missed you on OnlyBuns! Here's what's new since your last visit:\n\n" +
							"- You have %d new likes on your posts.\n" +
							"- There are %d new posts from people you follow.\n" +
							"- You have %d new comments on your posts.\n\n" +
							"Don't miss out on the latest activity—come back and see what's happening!\n\n" +
							"Your OnlyBuns Team",
					user.getFirstName(), // Assuming User has a method to get the first name
					newLikes,
					newPosts,
					newComments
			);
			this.emailSenderService.sendEmail(user.getEmail(), subject, body);
		}
	}
  
	private int GetNumberOfUnseenLikes(User user) {
		List<Post> userPosts= postRepository.findAllByUserIdAndNotDeletedAndNotRestricted(user.getId());
		List<Long> userPostIds = new ArrayList<>();
		for (Post post : userPosts) {
			userPostIds.add(post.getId());
		}
	  return likeRepository.findByPostIdInAndLikeDateAfter(userPostIds,user.getLastLoginDate()).size();
	}
  
	private int GetNumberOfUnseenPosts(User user) {
	  return postRepository.findAllByCreatorInAndIsDeletedFalseAndIsRestrictedFalseAndPostDateAfter(user.getFollowing(),user.getLastLoginDate()).size();
  }

  
	private int GetNumberOfUnseenComments(User user) {
		List<Post> userPosts= postRepository.findAllByUserIdAndNotDeletedAndNotRestricted(user.getId());
		return commentRepository.findAllByPostInAndCreatedAfter(userPosts,user.getLastLoginDate()).size();
	}

	@Scheduled(cron = "0 0 0 L * ?")
	@Transactional
	public void deleteUnactivatedAccounts() {
		userRepository.deleteByEnabledFalse();
		System.out.println("Scheduled account cleanup completed: Deleted unactivated accounts");
	}

}

