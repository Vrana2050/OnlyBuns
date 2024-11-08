package rs.ac.uns.ftn.onlybunsapp.service;

import java.util.List;

import rs.ac.uns.ftn.onlybunsapp.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findByName(String name);
}
