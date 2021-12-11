package com.itradix.ehealth.model;

import sk.gov.ehealth.common.v1.UserContext;
import sk.gov.ehealth.jruz.v1.IdPolozkaCiselnikaIdentifierType;
import sk.gov.ehealth.jruz.v1.OID;

public class DajJruzId {

	public DajJruzId() {
		// TODO Auto-generated constructor stub
	}
	
	private UserContext userContext;

	private Object kriteria;

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}

	public Object getKriteria() {
		return kriteria;
	}

	public void setKriteria(Object kriteria) {
		this.kriteria = kriteria;
	}


	public class DajJRUZIdentifikator_Kriteria {

		private String zadaneIdField;
		private OID typHladanehoIdField;
		private IdPolozkaCiselnikaIdentifierType typZadanehoIdField;
		
		public String getZadaneIdField() {
			return zadaneIdField;
		}
		public void setZadaneIdField(String zadaneIdField) {
			this.zadaneIdField = zadaneIdField;
		}
		public OID getTypHladanehoIdField() {
			return typHladanehoIdField;
		}
		public void setTypHladanehoIdField(OID typHladanehoIdField) {
			this.typHladanehoIdField = typHladanehoIdField;
		}
		public IdPolozkaCiselnikaIdentifierType getTypZadanehoIdField() {
			return typZadanehoIdField;
		}
		public void setTypZadanehoIdField(IdPolozkaCiselnikaIdentifierType typZadanehoIdField) {
			this.typZadanehoIdField = typZadanehoIdField;
		}
		

	}

}
