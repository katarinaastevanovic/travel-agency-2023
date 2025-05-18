package com.ftn.PrviMavenVebProjekat.service;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Role;
import com.ftn.PrviMavenVebProjekat.model.User;

public interface UserService {

	User findOneById(Long id);
	User findOneByEmail( String email);
	User findOne( String email, String password);
	List <User> findAll();
	User save(User user);
	User update(User user);
	User delete(Long id);
	User block(Long id);
	User unblock(Long id);
    boolean existsByEmail(String email);
	List<User> findUsersWithLoyaltyCardRequests();
	List<User> findAll(String sortField, String sortOrder);

	List<User> findAllByRole(String role);

	boolean existsByUsername(String username);


	List<User> searchByUsername(String username);

	User findOneByUsername(String identifier);
}
