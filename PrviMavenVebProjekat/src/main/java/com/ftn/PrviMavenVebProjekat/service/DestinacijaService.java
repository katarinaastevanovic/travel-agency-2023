package com.ftn.PrviMavenVebProjekat.service;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.Destinacija;

public interface DestinacijaService {
	
	Destinacija findOne(Long id);
	List <Destinacija> findAll();
	Destinacija save(Destinacija destinacija);
	Destinacija update(Destinacija destinacija);
	Destinacija delete(Long id);



}
