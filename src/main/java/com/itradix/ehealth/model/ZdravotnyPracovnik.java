package com.itradix.ehealth.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="zdravotny_pracovnik")
public class ZdravotnyPracovnik extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="firstname")
	private String firstName;
		
	@Column(name="lastnames")
	@ElementCollection(targetClass=String.class)
	private List<String> lastNames;
	
	@Column(name="jruzidzpr")
	private String jruzIdZpr;
	
	@Column(name="kodzpr")
	private String kodZpr;
	
	@Column(name="odbornostzpr")
	private String odbornostZpr;
	
	public ZdravotnyPracovnik() {
		super();
	}
	
	public ZdravotnyPracovnik(String firstName, String jruzIdZpr, String kodZpr, String odbornostZpr,
			List<String> lastNames) {
		super();
		
		this.firstName = firstName;
		this.lastNames = lastNames;
		this.odbornostZpr = odbornostZpr;
		this.jruzIdZpr = jruzIdZpr;
		this.kodZpr = kodZpr;
	}

	

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public List<String> getLastNames() {
		return lastNames;
	}

	public void setLastNames(List<String> lastNames) {
		this.lastNames = lastNames;
	}

	public String getJruzIdZpr() {
		return jruzIdZpr;
	}

	public void setJruzIdZpr(String jruzIdZpr) {
		this.jruzIdZpr = jruzIdZpr;
	}

	public String getKodZpr() {
		return kodZpr;
	}

	public void setKodZpr(String kodZpr) {
		this.kodZpr = kodZpr;
	}

	public String getOdbornostZpr() {
		return odbornostZpr;
	}

	public void setOdbornostZpr(String odbornostZpr) {
		this.odbornostZpr = odbornostZpr;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder().append(getFirstName()).append(getLastNames())
				.append(getJruzIdZpr());
		return builder.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, Arrays.asList(id.toString()));
	}

}
