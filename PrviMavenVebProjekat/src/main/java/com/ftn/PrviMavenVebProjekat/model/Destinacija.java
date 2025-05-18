package com.ftn.PrviMavenVebProjekat.model;

public class Destinacija {

	private Long id;
	private String grad;
	private String drzava;
	private String kontinent;
	private String slika;
	private Boolean aktivnost;

	public Destinacija() {
		this.aktivnost = true;
	}

	public Destinacija(Long id, String grad, String drzava, String kontinent, String slika, Boolean aktivnost) {
		this.id = id;
		this.grad = grad;
		this.drzava = drzava;
		this.kontinent = kontinent;
		this.slika = slika;
		this.aktivnost = aktivnost;
	}


	public Destinacija(Long id, String grad, String drzava, String kontinent, String slika) {
		this(id, grad, drzava, kontinent, slika, true);
	}

	public Destinacija(String grad, String drzava, String kontinent, String slika) {
		this(null, grad, drzava, kontinent, slika, true);
	}

	public Destinacija(String grad, String drzava, String kontinent) {
		this(null, grad, drzava, kontinent, null, true);
	}

	public Destinacija(Long id, String grad, String drzava, String kontinent) {

	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getGrad() { return grad; }
	public void setGrad(String grad) { this.grad = grad; }

	public String getDrzava() { return drzava; }
	public void setDrzava(String drzava) { this.drzava = drzava; }

	public String getKontinent() { return kontinent; }
	public void setKontinent(String kontinent) { this.kontinent = kontinent; }

	public String getSlika() { return slika; }
	public void setSlika(String slika) { this.slika = slika; }

	public Boolean getAktivnost() { return aktivnost; }
	public void setAktivnost(Boolean aktivnost) { this.aktivnost = aktivnost; }
}
