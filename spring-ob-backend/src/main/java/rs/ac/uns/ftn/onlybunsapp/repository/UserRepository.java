package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.onlybunsapp.dto.AdminUserList;
import rs.ac.uns.ftn.onlybunsapp.model.User;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Page<User> findAllByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email, Pageable pageable);
    Page<AdminUserList> findAllByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);



}

