package com.ftn.PrviMavenVebProjekat.dao;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Role;
import com.ftn.PrviMavenVebProjekat.model.User;

public interface UserDAO {
	public User findOne(Long id);

	public User findOne(String email);

	public User findOne(String email, String password);

	public List<User> findAll();

	public int save(User user);

	public int update(User user);

	public int delete(Long id);

	public int block(Long id);

	public int unblock(Long id);

	public boolean existsByEmail(String email);

	List<User> findByIds(List<Long> ids);
	List<User> findAll(String sortField, String sortOrder);


	List<User> findAllByRole(String role);

	List<User> searchByUsername(String username);

    boolean existsByUsername(String username);

	User findOneByUsername(String username);
}
