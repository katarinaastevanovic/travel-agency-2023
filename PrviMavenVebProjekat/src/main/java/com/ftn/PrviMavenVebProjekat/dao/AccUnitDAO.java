package com.ftn.PrviMavenVebProjekat.dao;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.AccUnit;


public interface AccUnitDAO {
	AccUnit findOne(Long id);
	List <AccUnit> findAll();
	int save(AccUnit accUnit);
	int update(AccUnit accUnit);
	int delete(Long id);

    List<AccUnit> findByDestinacijaId(Long destinacijaId);
}
