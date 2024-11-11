package rs.ac.uns.ftn.onlybunsapp.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.onlybunsapp.dto.AdminUserList;
import rs.ac.uns.ftn.onlybunsapp.dto.PaginationRequest;
import rs.ac.uns.ftn.onlybunsapp.dto.UserRequest;
import rs.ac.uns.ftn.onlybunsapp.model.Role;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.RoleService;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;



@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

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
		u.setEnabled(true);
		u.setEmail(userRequest.getEmail());

		// u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
		List<Role> roles = roleService.findByName("ROLE_USER");
		u.setRoles(roles);
		
		return this.userRepository.save(u);
	}

	public Page<AdminUserList> getUsers(Pageable pageable, String firstName, String lastName,
										String email, Integer minPosts, Integer maxPosts) {
		System.out.println("Page request: " + pageable);

		// Check if page size or page number is being passed correctly
		if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
			throw new IllegalArgumentException("Invalid page or size");
		}

		Page<User> users = userRepository.findAllByFirstNameAndLastNameAndEmail(firstName, lastName, email, pageable);
		System.out.println("MIKA JE LEPA");
		System.out.println(users.getTotalElements());

		// Use the mapper to convert each User in the Page<User> to AdminUserList
		Page<AdminUserList> adminUserListPage = users.map(this::toAdminUserList);

		for (AdminUserList adminUserList : adminUserListPage) {
			System.out.println(adminUserList.getEmail());
		}
		System.out.println("Obecavam");

		return adminUserListPage;
	}
	public AdminUserList toAdminUserList(User user) {
		AdminUserList adminUserList = new AdminUserList();
		adminUserList.setEmail(user.getEmail());
		adminUserList.setFirstname(user.getFirstName());
		adminUserList.setLastname(user.getLastName());
		adminUserList.setNumberOfPosts(user.getNumberOfPosts());
		adminUserList.setNumberOfFollowing(user.getNumberOfFollowing());
		adminUserList.setId(user.getId());
		return adminUserList;
	}

	public List<User> getFilteredUsers(PaginationRequest p) {

		// Fetch all users
		List<User> users = userRepository.findAll();

		// Filter users based on criteria
		List<User> filteredUsers = users.stream()
				.filter(user -> user.getFirstName().toLowerCase().contains(p.getFirstName().toLowerCase()))
				.filter(user -> user.getLastName().toLowerCase().contains(p.getLastName().toLowerCase()))
				.filter(user -> user.getEmail().toLowerCase().contains(p.getEmail().toLowerCase()))
				.filter(user -> (p.getMinPosts() == null || user.getNumberOfPosts() >= p.getMinPosts()))
				.filter(user -> (p.getMaxPosts() == null || user.getNumberOfPosts() <= p.getMaxPosts()))
				.collect(Collectors.toList());

		// Sort users based on sortBy and sortOrder
		Comparator<User> comparator;
		if ("numberOfFollowing".equalsIgnoreCase(p.getSortBy())) {
			comparator = Comparator.comparing(User::getNumberOfFollowing);
		} else {
			comparator = Comparator.comparing(User::getEmail);
		}

		if ("desc".equalsIgnoreCase(p.getSortDirection())) {
			comparator = comparator.reversed();
		}

		filteredUsers.sort(comparator);

		return filteredUsers;
	}






}
