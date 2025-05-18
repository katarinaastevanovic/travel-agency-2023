package com.ftn.PrviMavenVebProjekat.dao;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Destinacija;

public interface DestinacijaDAO {
	Destinacija findOne(Long id);
	List <Destinacija> findAll();
	int save(Destinacija destinacija);
	int update(Destinacija destinacija);
	int delete(Long id);
}
