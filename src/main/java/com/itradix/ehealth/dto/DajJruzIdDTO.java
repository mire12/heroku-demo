package com.itradix.ehealth.dto;

import javax.xml.bind.annotation.XmlElement;

import sk.gov.ehealth.common.v1.UserContext;
import sk.gov.ehealth.jruz.v1.IdPolozkaCiselnikaIdentifierType;
import sk.gov.ehealth.jruz.v1.OID;

public class DajJruzIdDTO {

	public DajJruzIdDTO() {
		// TODO Auto-generated constructor stub
	}

	private UserContext userContext;
	private DajJRUZIdentifikator_Kriteria kriteria;

	public DajJRUZIdentifikator_Kriteria getKriteria() {
		return kriteria;
	}

	public void setKriteria(DajJRUZIdentifikator_Kriteria kriteria) {
		this.kriteria = kriteria;
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	
	public class DajJRUZIdentifikator_Kriteria {

		private String zadaneId;
		private String typHladanehoId;
		private IdPolozkaCiselnikaDTO typZadanehoId;

		public String getZadaneId() {
			return zadaneId;
		}

		public void setZadaneId(String zadaneId) {
			this.zadaneId = zadaneId;
		}

		public String getTypHladanehoId() {
			return typHladanehoId;
		}

		public void setTypHladanehoId(String typHladanehoId) {
			this.typHladanehoId = typHladanehoId;
		}

		public IdPolozkaCiselnikaDTO getTypZadanehoId() {
			return typZadanehoId;
		}

		public void setTypZadanehoId(IdPolozkaCiselnikaDTO typZadanehoId) {
			this.typZadanehoId = typZadanehoId;
		}

		public class IdPolozkaCiselnikaDTO {
			protected String codeValue;
			protected String codingSchemeOID;
			protected String codingSchemeVersion;

			public String getCodeValue() {
				return codeValue;
			}

			public void setCodeValue(String codeValue) {
				this.codeValue = codeValue;
			}

			public String getCodingSchemeOID() {
				return codingSchemeOID;
			}

			public void setCodingSchemeOID(String codingSchemeOID) {
				this.codingSchemeOID = codingSchemeOID;
			}

			public String getCodingSchemeVersion() {
				return codingSchemeVersion;
			}

			public void setCodingSchemeVersion(String codingSchemeVersion) {
				this.codingSchemeVersion = codingSchemeVersion;
			}

		}
	}

}
