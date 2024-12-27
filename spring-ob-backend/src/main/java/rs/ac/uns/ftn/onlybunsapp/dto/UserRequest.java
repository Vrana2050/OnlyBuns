package rs.ac.uns.ftn.onlybunsapp.dto;

// DTO koji preuzima podatke iz HTML forme za registraciju
public class UserRequest {

	private Long id;

	private String username;

	private String password;

	private String firstname;

	private String lastname;
	
	private String email;

	private String address;

	private int numberOfFollowers;
	private int numberOfFollowing;

	public int getNumberOfFollowers() {
		return numberOfFollowers;
	}

	public void setNumberOfFollowers(int numberOfFollowers) {
		this.numberOfFollowers = numberOfFollowers;
	}

	public int getNumberOfFollowing() {
		return numberOfFollowing;
	}

	public void setNumberOfFollowing(int numberOfFollowing) {
		this.numberOfFollowing = numberOfFollowing;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
