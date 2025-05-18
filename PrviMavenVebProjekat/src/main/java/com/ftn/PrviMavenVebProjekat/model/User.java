package com.ftn.PrviMavenVebProjekat.model;


import java.time.LocalDateTime;
import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class User {
	
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	private Long id;
	private String username;
	private String password;	
	private String name;
	private String lastName;
	private String email;
	private Date dateOfBirth;
	private String address;
	private String number;
	private LocalDateTime dateOfReg;
	private Role role; 
	private boolean isBlocked;
	
	
	public boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	

	public User(Long id, String username, String password, 
			String name, String lastName, String email, 
			Date dateOfBirth, String address, String number,
			LocalDateTime dateOfReg, Role role, boolean isBlocked) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.number = number;
		this.dateOfReg = dateOfReg;
		this.role = role;
		this.isBlocked=isBlocked;
	}
	public User( String username, String password, 
			String name, String lastName, String email, 
			Date dateOfBirth, String address, String number,
			LocalDateTime dateOfReg, Role role, boolean isBlocked) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.number = number;
		this.dateOfReg = dateOfReg;
		this.role = role;
		this.isBlocked=isBlocked;
	}
	public User() {
		super();
	}

	public User(Long id, String username, String password, String name, String lastName, String email, Date dateOfBirth,
			String address, String number, LocalDateTime dateOfReg, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.number = number;
		this.dateOfReg = dateOfReg;
		this.role = role;
	}

	public User( String username, String password, 
			String name, String lastName, String email, 
			Date dateOfBirth, String address, String number) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.number = number;
		
	}
	

	public Long getId() {
	    return id;
	}

	public void setId(Long id) {
	    this.id = id;
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

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getLastName() {
	    return lastName;
	}

	public void setLastName(String lastName) {
	    this.lastName = lastName;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public Date getDateOfBirth() {
	    return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
	    this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public String getNumber() {
	    return number;
	}

	public void setNumber(String number) {
	    this.number = number;
	}

	public LocalDateTime getDateOfReg() {
	    return dateOfReg;
	}

	public void setDateOfReg(LocalDateTime dateOfReg) {
	    this.dateOfReg = dateOfReg;
	}

	public Role getRole() {
	    return role;
	}

	public void setRole(Role role) {
	    this.role = role;
	}
	public boolean isAdmin() {
        if (role == Role.ADMIN) {
            return true;
        } else
            return false;

    }
	public boolean isManager() {
		if (role == Role.MANAGER) {
			return true;
		} else
			return false;

	}

	public boolean isPassenger() {
		if (role == Role.PASSENGER) {
			return true;
		} else
			return false;

	}

}
