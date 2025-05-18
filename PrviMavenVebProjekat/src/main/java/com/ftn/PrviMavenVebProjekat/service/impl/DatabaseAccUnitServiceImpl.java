package com.ftn.PrviMavenVebProjekat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.PrviMavenVebProjekat.dao.AccUnitDAO;
import com.ftn.PrviMavenVebProjekat.model.AccUnit;
import com.ftn.PrviMavenVebProjekat.service.AccUnitService;

@Service
public class DatabaseAccUnitServiceImpl implements AccUnitService{
	
	@Autowired
	private AccUnitDAO accUnitDAO;

	@Override
	public AccUnit findOne(Long id) {
		return accUnitDAO.findOne(id);
	}

	@Override
	public List<AccUnit> findAll() {
		return accUnitDAO.findAll();
	}

	@Override
	public AccUnit save(AccUnit accUnit) {
		accUnitDAO.save(accUnit);
		return accUnit;
	}

	@Override
	public AccUnit update(AccUnit accUnit) {
		accUnitDAO.update(accUnit);
		return accUnit;
	}

	@Override
	public AccUnit delete(Long id) {
		AccUnit accUnit = accUnitDAO.findOne(id);
		if (accUnit != null) {
			accUnitDAO.delete(id);
		}
		return accUnit;
	}

	@Override
	public List<AccUnit> findByDestinacijaId(Long destinacijaId) {
		return accUnitDAO.findByDestinacijaId(destinacijaId);
	}

}
