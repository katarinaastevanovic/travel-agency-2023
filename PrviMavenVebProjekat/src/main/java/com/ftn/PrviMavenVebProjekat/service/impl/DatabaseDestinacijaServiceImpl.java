package com.ftn.PrviMavenVebProjekat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.PrviMavenVebProjekat.dao.DestinacijaDAO;
import com.ftn.PrviMavenVebProjekat.model.Destinacija;
import com.ftn.PrviMavenVebProjekat.service.DestinacijaService;

@Service
public class DatabaseDestinacijaServiceImpl implements DestinacijaService{

	@Autowired
	private DestinacijaDAO destinacijaDAO;

	@Override
	public Destinacija findOne(Long id) {
		return destinacijaDAO.findOne(id);
	}

	@Override
	public List<Destinacija> findAll() {
		return destinacijaDAO.findAll();
	}

	@Override
	public Destinacija save(Destinacija destinacija) {
		destinacijaDAO.save(destinacija);
		return destinacija;
	}

	@Override
	public Destinacija update(Destinacija destinacija) {
		destinacijaDAO.update(destinacija);
		return destinacija;
	}

	@Override
	public Destinacija delete(Long id) {
		Destinacija destinacija = destinacijaDAO.findOne(id);
		destinacija.setAktivnost(false);
		destinacijaDAO.update(destinacija); // Ažuriranje destinacije
		return destinacija;
	}
}
