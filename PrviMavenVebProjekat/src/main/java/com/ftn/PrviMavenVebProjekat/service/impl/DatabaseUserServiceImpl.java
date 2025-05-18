package com.ftn.PrviMavenVebProjekat.service.impl;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.dao.LoyaltyCardRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.PrviMavenVebProjekat.dao.UserDAO;
import com.ftn.PrviMavenVebProjekat.model.User;
import com.ftn.PrviMavenVebProjekat.service.UserService;

@Service
public class DatabaseUserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private LoyaltyCardRequestDAO loyaltyCardRequestDAO;

	@Override
	public User findOne(String email, String password) {
		return userDAO.findOne(email, password);
	}
	
	@Override
	public User findOneById(Long id) {
		return userDAO.findOne(id);
	}

	@Override
	public User findOneByEmail(String email) {
		return userDAO.findOne(email);
	}

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}

	@Override
	public User save(User user) {
		userDAO.save(user);
		return user;
	}
	
	@Override
	public User update(User user) {
		userDAO.update(user);
		return user;
	}

	@Override
	public User delete(Long id) {
		User user = userDAO.findOne(id);
		userDAO.delete(id);
		return user;
	}

	public boolean isRegistered(String username) {
		List<User> users=findAll();

		for (User user : users) {
			if (user.getUsername().equals(username)){
				return true;
			}
		}

		return false;
	}

	@Override
	public User block(Long id) {
		User user = userDAO.findOne(id);
		userDAO.block(id);
		return user;
	}
	@Override
	public User unblock(Long id) {
		User user = userDAO.findOne(id);
		userDAO.unblock(id);
		return user;
	}

	@Override
	public boolean existsByEmail(String email) {
		return userDAO.existsByEmail(email);
	}



	@Override
	public List<User> findUsersWithLoyaltyCardRequests() {
		List<Long> userIdsWithRequests = loyaltyCardRequestDAO.findUserIdsWithRequests();
		return userDAO.findByIds(userIdsWithRequests);
	}

	@Override
	public List<User> findAll(String sortField, String sortOrder) {
		return userDAO.findAll(sortField, sortOrder);
	}

	@Override
	public List<User> findAllByRole(String role) {
		return userDAO.findAllByRole(role);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userDAO.existsByUsername(username);
	}

	@Override
	public List<User> searchByUsername(String username) {
		return userDAO.searchByUsername(username);
	}

	@Override
	public User findOneByUsername(String username) {
		return userDAO.findOneByUsername(username);
	}



}