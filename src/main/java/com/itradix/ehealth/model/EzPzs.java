package com.itradix.ehealth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "ez_pzs")
public class EzPzs implements Serializable {

	

	public EzPzs(String kodpzs, String idpzs, String idoupzs, String ico, String nazov, String ulica, String obec, String obecou) {
		super();
		this.kodpzs = kodpzs;
		this.idpzs = idpzs;
		this.idoupzs = idoupzs;
		this.ico = ico;
		this.nazov = nazov;
		this.ulica = ulica;
		this.obec = obec;
		this.obecou = obecou;
	}




	public String getKodpzs() {
		return kodpzs;
	}

	public void setKodpzs(String kodpzs) {
		this.kodpzs = kodpzs;
	}

	public String getIdpzs() {
		return idpzs;
	}

	public void setIdpzs(String idpzs) {
		this.idpzs = idpzs;
	}

	public String getIdoupzs() {
		return idoupzs;
	}

	public void setIdoupzs(String idoupzs) {
		this.idoupzs = idoupzs;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getUlica() {
		return ulica;
	}

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}

	public String getObec() {
		return obec;
	}

	public void setObec(String obec) {
		this.obec = obec;
	}



	@Id
	@Column(name = "kodpzs")
	private String kodpzs;

	@Column(name = "idpzs")
	private String idpzs;

	@Column(name = "idoupzs")
	private String idoupzs;
	
	@Column(name = "ico")
	private String ico;
	
	@Column(name = "nazov")
	private String nazov;

	@Column(name = "ulica")
	private String ulica;

	@Column(name = "obec")
	private String obec;
	
	@Column(name = "obecou")
	private String obecou;
}