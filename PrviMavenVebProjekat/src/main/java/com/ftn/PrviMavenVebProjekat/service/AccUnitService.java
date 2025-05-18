package com.ftn.PrviMavenVebProjekat.service;

import java.util.List;

import com.ftn.PrviMavenVebProjekat.model.AccUnit;

public interface AccUnitService {
	AccUnit findOne(Long id);
	List <AccUnit> findAll();
	AccUnit save(AccUnit accUnit);
	AccUnit update(AccUnit accUnit);
	AccUnit delete(Long id);

    List<AccUnit> findByDestinacijaId(Long destinacijaId);
}
