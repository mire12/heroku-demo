package com.itradix.ehealth.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itradix.ehealth.dto.DajJruzIdDTO;
import com.itradix.ehealth.dto.PatientDTO;
import com.itradix.ehealth.exception.PatientNotFoundException;
import com.itradix.ehealth.model.DajJruzId;
import com.itradix.ehealth.model.DajOdobornyUtvarPoskytovatelaZS;
import com.itradix.ehealth.model.DajSumar;
import com.itradix.ehealth.model.DajSumarEds;
import com.itradix.ehealth.model.DajSumarUdaje;
import com.itradix.ehealth.model.DajZaznamOVysetreni;
import com.itradix.ehealth.model.DajZpr;
import com.itradix.ehealth.model.EzClassifications;
import com.itradix.ehealth.model.NcziResponse;
import com.itradix.ehealth.model.OverVerziu;
import com.itradix.ehealth.model.Patient;
import com.itradix.ehealth.model.StornujZaznamOVysetreni;
import com.itradix.ehealth.model.VyhladajZaznamOvysetreniPreZiadatela;
import com.itradix.ehealth.model.VyhladajZaznamyOvysetreni;
import com.itradix.ehealth.model.ZapisSuhlasOsobyPrePZS;
import com.itradix.ehealth.model.ZapisSumarProblemy;
import com.itradix.ehealth.model.ZapisSumarUdaje;
import com.itradix.ehealth.model.ZapisZaznamOVysetreni;
import com.itradix.ehealth.model.ZrusSumar;
import com.itradix.ehealth.service.CommMaxService;
import com.itradix.ehealth.service.CommMaxServiceImpl;
import com.itradix.ehealth.service.JruzIdService;
import com.itradix.ehealth.service.PatientService;
import com.itradix.ehealth.service.PatientServiceImpl;
import com.itradix.ehealth.service.S3XmlService;
import com.itradix.ehealth.service.XmlServiceImpl;

import cen._13606.rm.COMPOSITION;
import cen._13606.rm.EHREXTRACT;
import ch.qos.logback.classic.Logger;
import net.minidev.json.JSONArray;
import sk.gov.ehealth.ehtalkmessage.EHtalkMessage;
import sk.gov.ehealth.jruz.v1.DajPrZSResponse;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "https://ehealth-ng-app.herokuapp.com")
@RestController
public class EhealthController {

	private final PatientService patientService;
	private final JruzIdService jruzService;
	private final CommMaxService commmaxService;
	private final XmlServiceImpl xmlService;
	private final S3XmlService s3XmlService;
	private static final Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(EhealthController.class);

	@Autowired
	public EhealthController(PatientServiceImpl patientService, JruzIdService jruzService,
			CommMaxServiceImpl commmaxService, XmlServiceImpl xmlService, S3XmlService s3XmlService) {
		this.patientService = patientService;
		this.jruzService = jruzService;
		this.commmaxService = commmaxService;
		this.xmlService = xmlService;
		this.s3XmlService = s3XmlService;
	}
	
	//---------------------------PATIENT -------------------------------------------------//

	@GetMapping("/patient/{id}")
	@ResponseBody
	public Patient get(@PathVariable Long id) throws PatientNotFoundException {
		return patientService.findById(id).orElseThrow(() -> PatientNotFoundException.createWith(id.toString()));

	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/patient", produces = "application/json")
	public List<Patient> getPatientList() {
		List<Patient> patientMap = patientService.findAll();
		
		if (!patientMap.isEmpty()) {
			return patientMap;
		} else {
			return Collections.emptyList();
		}
		
		
		/*ObjectMapper objectMapper = new ObjectMapper();
		String patients = null;
		
        try {
            patients = objectMapper.writeValueAsString(patientMap);
            logger.debug(patients);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.OK);
        }
        return new ResponseEntity<String>(patients, HttpStatus.OK);*/
	    

	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/patient/save", consumes = "application/json", produces = "application/json")
	public Patient index(@RequestBody PatientDTO patientDto) {

		Patient patient = new Patient(patientDto.getBirthDate(), patientDto.getBirthNumber(),
				patientDto.getFirstName(), patientDto.getGender(), patientDto.getLastNames(), patientDto.getDoctorPrZs(), patientDto.getJruzId(), patientDto.getInsurance());
		return patientService.save(patient);
	}

	//---------------------------CISELNIKY -------------------------------------------------//
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/classifications/{oid}/{oid_ver}", produces = "application/json")
	public List<Object> getZdravotnickaOdbornostList(@PathVariable String oid, @PathVariable String oid_ver) {

		List<Object> ezClassifications = commmaxService.findZdravotnickaOdbornostList(oid, oid_ver);
		if (!ezClassifications.isEmpty()) {
			return ezClassifications;
		} else {
			return Collections.emptyList();
		}
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/poskytovatelzs/{idoupzs}", produces = "application/json")
	public List<Object> getOdbornyUtvarPoskytovatelaZS(@PathVariable String idoupzs) {

		List<Object> odbornyUtvarPzsList = commmaxService.findOdbornyUtvarPoskytovatelaZS(idoupzs);
		if (!odbornyUtvarPzsList.isEmpty()) {
			return odbornyUtvarPzsList;
		} else {
			return Collections.emptyList();
		}
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/poskytovatelzs", produces = "application/json")
	public List<Object> getOdbornyUtvarPoskytovatelaZSList(@RequestParam String obec) {

		List<Object> odbornyUtvarPzsList = commmaxService.findOdbornyUtvarPoskytovatelaZSList(obec);
		if (!odbornyUtvarPzsList.isEmpty()) {
			return odbornyUtvarPzsList;
		} else {
			return Collections.emptyList();
		}
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/identificators/{oid}/{oid_ver}", produces = "application/json")
	public List<Object> getCiselnikIdentifikatorList(@PathVariable String oid, @PathVariable String oid_ver) {

		List<Object> indentificators = commmaxService.findCiselnikIdentifikatorList(oid, oid_ver);
		if (!indentificators.isEmpty()) {
			return indentificators;
		} else {
			return Collections.emptyList();
		}
	}
	
	//---------------------------HISTORIA VOLANI-------------------------------------------------//
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/getehealthresponse", produces = "text/plain")
	public String getEhealthResponse(@RequestParam String evId) {

		List<NcziResponse> ncziresponse = commmaxService.findNcziRespUsingNativeQuery(evId);
		if (!ncziresponse.isEmpty()) {
			return ncziresponse.get(0).getReturnMsg();
		} else {
			return "Nepodarilo sa vytiahnut spravu";
		}
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/getehealthresponse", produces = "application/json")
	public ResponseEntity<?> getEhealthResponses(@RequestParam(name = "did") String dId, @RequestParam(name = "pid") String pId, @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationDate) {

		List<NcziResponse> ncziresponse = commmaxService.searchNcziResp(dId, pId, creationDate);
		if (!ncziresponse.isEmpty()) {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = null;
			try {
				jsonString = objectMapper.writeValueAsString(ncziresponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.OK);
			}
			return new ResponseEntity<String>(jsonString, HttpStatus.OK) ;
		} else {

			return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.OK);
		}
	}
	 
	//--------------------------- Zapis Suhlas Osoby Pre PZS -------------------------------------------------//
		
		@GetMapping(path = "/commmax/zapissuhlasosobyprepzs")
		public String getZapisSuhlasOsobyPrePZS(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
				@RequestParam String patient) {
			return jruzService.getJruzId(pID, evID, dID, patient);
		}

		@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
		@PostMapping(path = "/zapissuhlasosobyprepzs", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
		public String getZapisSuhlasOsobyPrePZSXml(String pID, String evID, String dID, String patient) {
			
			String encodedEvId = evID.split("/")[1];
						
		    try{
				return new String(s3XmlService.downloadPublicFile(encodedEvId, "zapissuhlasosobyprepzs"), StandardCharsets.UTF_8);
				
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
			}
						
			return commmaxService.getCommmaxTemplate("zapissuhlasosobyprepzs.xml");
		}

		@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
		@PostMapping(path = "/zapissuhlasosobyprepzs/xml", produces = "text/plain")
		public String feedZapisSuhlasOsobyPrePZS(@RequestBody ZapisSuhlasOsobyPrePZS zapisSuhlasOsobyPrePZS , @RequestParam String evID) {

			return xmlService.updateZapisSuhlasOsobyPrePZSXml(zapisSuhlasOsobyPrePZS, evID);

		}
		
	
	//---------------------------DAJ JRUZ ID-------------------------------------------------//
	
	@GetMapping(path = "/commmax/jruzid")
	public String getJruzId(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getJruzId(pID, evID, dID, patient);
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/jruzid", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getJruzIdXml(String pID, String evID, String dID, String patient) {
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "jruzid"), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("jruzid.xml");
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/jruzid/xml", produces = "text/plain")
	public String feedJruzid(@RequestBody DajJruzIdDTO dajJruzId , @RequestParam String evID) {

		return xmlService.updateJruzidXml(dajJruzId, evID);

	}
	

	//---------------------------DAJ SUMAR-------------------------------------------------//
	
	@GetMapping(path = "/commmax/dajpacientskysumar")
	public String getPacientskySumar(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getSumar(pID, evID, dID, patient);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajpacientskysumar/xml", produces = "text/plain")
	public String feedDajPacientskySumar(@RequestBody DajSumar dajSumar , @RequestParam String evID) {

		return xmlService.updateDajPacientskySumarXml(dajSumar, evID);
		
	}	

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajpacientskysumar", produces = { "application/xml", "text/xml" })
	public String getPacientskySumarXml(String pID, String evID, String dID, String patient) {
		
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "dajpacientskysumar"), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("dajpacientskysumar.xml");
	}
	
	//---------------------------ZRUS SUMAR-------------------------------------------------//
	@GetMapping(path = "/commmax/zruspacientskysumar")
	public String deletePacientskySumar(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.zrusSumar(pID, evID, dID, patient);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/zruspacientskysumar/xml", produces = "text/plain")
	public String feedZrusPacientskySumar(@RequestBody ZrusSumar zrusSumar , @RequestParam String evID) {

		return xmlService.updateZrusPacientskySumarXml(zrusSumar, evID);
		
	}	

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/zruszapissumar",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getZrusPacientskySumarXml(String pID, String evID, String dID, String patient) {
		
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "zruszapissumar"), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("zruszapissumar.xml");
	}
		
	//-----------------------------ZAPIS SUMAR PROBLEMY-----------------------------------------------//
	
	@PostMapping(path = "/commmax/zapissumarproblemy")
	public String zapisSumarProblemy(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.zapisSumarProblemy(pID, evID, dID, patient);
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/zapissumarproblemy", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getZapisSumarProblemyXml(String pID, String evID, String dID, String patient) {
		
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "zapissumarproblemy"), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("zapissumarproblemy.xml");
	}
		
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/zapissumarproblemy/xml", produces = "text/plain")
	public String feedZapisSumarProblemy(@RequestBody ZapisSumarProblemy zapisSumarProblemy , @RequestParam String evID) {
		return xmlService.updateZapisSumarProblemyXml(zapisSumarProblemy, evID);
	}
	
	//-------------------------------ZAPIS SUMAR UDAJE ---------------------------------------------//
	
	@PostMapping(path = "/commmax/zapissumarudaje")
	public String zapisSumarUdaje(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.zapisSumarUdaje(pID, evID, dID, patient);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/zapissumarudaje/xml", produces = "text/plain")
	public String feedZapisSumarUdaje(@RequestBody ZapisSumarUdaje zapisSumareUdaje, @RequestParam String evID) {
		return xmlService.updateZapisSumarUdajeXml(zapisSumareUdaje, evID);
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/zapissumarudaje",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getZapisSumarUdajeXml(String pID, String evID, String dID, String patient) {
		
		try {
			
			return new String(s3XmlService.downloadPublicFile(evID, "zapissumarudaje"), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("zapissumarudaje.xml");
	}
	
	//------------------------------ZAPIS VYSETRENIE-----------------------------------------------//
	@PostMapping(path = "/commmax/vysetrenie")
	public String zapisVysetrenie(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.zapisVysetrenie(pID, evID, dID, patient);
	}

		
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/vysetrenie",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getZapisZaznamOVysetreniXml(String pID, String evID, String dID, String patient) {
		
		try {
			
			return new String(s3XmlService.downloadPublicFile(evID, "vysetrenie"), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("vysetrenie.xml");
	}
	
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/vysetrenie/xml", produces = "text/plain")
	public String feedZapisZaznamOVysetreni(@RequestBody ZapisZaznamOVysetreni zapisZaznamOVysetreni, @RequestParam String evID) {
		return xmlService.updateZapisZaznamOVysetreniXml(zapisZaznamOVysetreni, evID);
	}
	
		
	//-------------------------------VYHLADAJ ZAZNAMY PRE ZIADATELA--------------------------------//

	@GetMapping(path = "/commmax/vyhladajzaznamypreziadatela")
	public String getVyhladajZaznamyPreZiadatela(@RequestParam String pID, @RequestParam String evID,
			@RequestParam String dID, @RequestParam String patient) {
		return jruzService.getVyhladajZaznamyPreZiadatela(pID, evID, dID, patient);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/vyhladajzaznamypreziadatela/xml", produces = "text/plain")
	public String feedVyhladajZaznamyOVysetreni(@RequestBody VyhladajZaznamOvysetreniPreZiadatela vyhladajZaznamOvysetreniPreZiadatela, @RequestParam String evID) {
		return xmlService.updateVyhladajZaznamOVysetreniPreZiadatelaXml(vyhladajZaznamOvysetreniPreZiadatela, evID);
	}

		
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/vyhladajzaznamypreziadatela", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getVyhladajZaznamyPreZiadatelaXml(String pID, String evID, String dID, String patient) {
				
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "vyhladajzaznamypreziadatela"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("vyhladajzaznamypreziadatela.xml");
	}

	//-------------------------------VYHLADAJ ZAZNAMY---------------------------------------------//
	@GetMapping(path = "/commmax/vyhladajzaznamy")
	public String getVyhladajZaznamy(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getVyhladajZaznamy(pID, evID, dID, patient);
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/vyhladajzaznamy/xml", produces = "text/plain")
	public String feedVyhladajZaznamyOVysetreni(@RequestBody VyhladajZaznamyOvysetreni vyhladajZaznamyOvysetreni, @RequestParam String evID) {
		try{
			if(Integer.parseInt(vyhladajZaznamyOvysetreni.getCitlivost()) > 3 || vyhladajZaznamyOvysetreni.getVlastneZaznamy().equals("false") ) {
				ZapisSuhlasOsobyPrePZS zapisSuhlasOsobyPrePZS =  new ZapisSuhlasOsobyPrePZS();
				zapisSuhlasOsobyPrePZS.setUserContext(vyhladajZaznamyOvysetreni.getUserContext());
				zapisSuhlasOsobyPrePZS.setCitlivost(vyhladajZaznamyOvysetreni.getCitlivost());
				zapisSuhlasOsobyPrePZS.setPlatnyDo(vyhladajZaznamyOvysetreni.getDatumDo());
				zapisSuhlasOsobyPrePZS.setPlatnyOd(vyhladajZaznamyOvysetreni.getDatumOd());
				zapisSuhlasOsobyPrePZS.setPoznamka("ZapisSuhlasOsobyPrePZS");
				zapisSuhlasOsobyPrePZS.setPodpisanaDohoda("true");
				zapisSuhlasOsobyPrePZS.setTypSuhlasu("0");
				xmlService.updateZapisSuhlasOsobyPrePZSXml(zapisSuhlasOsobyPrePZS, evID);
			}
			
		}catch (NumberFormatException e) {
			logger.error("Citlivost nie je cislo: " + e.getMessage());
			vyhladajZaznamyOvysetreni.setCitlivost("3");		
			
		}
		
		return xmlService.updateVyhladajZaznamyOVysetreniXml(vyhladajZaznamyOvysetreni, evID);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/vyhladajzaznamy", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getVyhladajZaznamyOVysetreniXml(String pID, String evID, String dID, String patient) {
				
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "vyhladajzaznamy"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("vyhladajzaznamy.xml");
	}

	
	//-------------------------------DAJ ZAZNAM O VYSETRENI ---------------------------------------//
	
	@PostMapping(path = "/commmax/dajzaznamovysetreni")
	public String getDajZaznamOVysetreni(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getDajZaznamOVysetreni(pID, evID, dID, patient);
	}	
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajzaznamovysetreni/xml", produces = "text/plain")
	public String feedDajZaznamOVysetreni(@RequestBody DajZaznamOVysetreni dajZaznamOVysetreni, @RequestParam String evID) {
		return xmlService.updateDajZaznamOVysetreniXml(dajZaznamOVysetreni, evID);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajzaznamovysetreni", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getDajZaznamOVysetreniXml(String pID, String evID, String dID, String patient) {
				
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "dajzaznamovysetreni"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("dajzaznamovysetreni.xml");
	}
	
	//-------------------------------STORNUJ ZAZNAM O VYSETRENI ---------------------------------------//
	
		@PostMapping(path = "/commmax/stornujzaznam")
		public String getStornujZaznamOVysetreni(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
				@RequestParam String patient) {
			return jruzService.getStornujZaznamOVysetreni(pID, evID, dID, patient);
		}	
		
		@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
		@PostMapping(path = "/stornujzaznam/xml", produces = "text/plain")
		public String feedStornujZaznamOVysetreni(@RequestBody StornujZaznamOVysetreni stornujZaznamOVysetreni, @RequestParam String evID) {
			return xmlService.updateStornujZaznamOVysetreniXml(stornujZaznamOVysetreni, evID);
		}
		
		@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
		@PostMapping(path = "/stornujzaznam", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
		public String getStornujZaznamOVysetreniXml(String pID, String evID, String dID, String patient) {
					
			try {			
				return new String(s3XmlService.downloadPublicFile(evID, "stornujzaznam"), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getLocalizedMessage());
			}
			
			return commmaxService.getCommmaxTemplate("stornujzaznam.xml");
		}
	
	//-------------------------------DAJ PACIENTSKY SUMAR EDS----------------------------------------------------//

	@GetMapping(path = "/commmax/dajpacientskysumareds")
	public String getPacientskySumarEds(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getSumarEds(pID, evID, dID, patient);
	}

	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajpacientskysumareds/xml", produces = "text/plain")
	public String feedPacientskySumarEds(@RequestBody DajSumarEds dajSumarEds, @RequestParam String evID) {
		return xmlService.updatePacientskySumarEdsXml(dajSumarEds, evID);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajpacientskysumareds", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getPacientskySumarEdsXml(String pID, String evID, String dID, String patient) {
				
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "dajpacientskysumareds"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("dajpacientskysumareds.xml");
	}

	//-------------------------------DAJ PACIENTSKY SUMAR UDAJE----------------------------------------------------//

	@GetMapping(path = "/commmax/dajpacientskysumarudaje", produces = { "text/plain" })
	public String getPacientskySumarUdaje(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getSumarUdaje(pID, evID, dID, patient);
	}

	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajpacientskysumarudaje", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getPacientskySumarUdajeXml(String pID, String evID, String dID, String patient) {
				
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "dajpacientskysumarudaje"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("dajpacientskysumarudaje.xml");
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajpacientskysumarudaje/xml", produces = "text/plain")
	public String feedDajPacientskySumarUdaje(@RequestBody DajSumarUdaje dajSumarudaje, @RequestParam String evID) {
		return xmlService.updateDajPacientskySumarUdajeXml(dajSumarudaje,evID);

	}
	
	//-------------------------------DAJ ZPR----------------------------------------------------//
	@GetMapping(path = "/commmax/dajzpr")
	public String getZpr(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID) {
		return jruzService.getZpr(pID, evID, dID);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajzpr/xml", produces = "text/plain")
	public String feedDajOupzs(@RequestBody DajZpr dajZdravotnehoPracovnika, @RequestParam String evID) {

		return xmlService.updateZprXml(dajZdravotnehoPracovnika, evID);

	}	

	
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajzpr", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String dajZpr(String pID, String evID, String dID, String patient) {
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "dajzpr"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("dajzpr.xml");
	}
	
	
	

	//-------------------------------DAJ OUPZS--------------------------------//

	@GetMapping(path = "/commmax/dajoupzs")
	public String getOupzs(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID) {
		return jruzService.getOupzs(pID, evID, dID);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/oupzs/xml", produces = "text/plain")
	public String feedDajOupzs(@RequestBody DajOdobornyUtvarPoskytovatelaZS dajOdbornyUtvarPoskytovaleZS, @RequestParam String evID) {

		return xmlService.updateOupzsXml(dajOdbornyUtvarPoskytovaleZS, evID);

	}	
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/dajoupzs", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String dajOupzs(String pID, String evID, String dID, String patient) {
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "dajoupzs"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("dajoupzs.xml");
	}
	
	//---------------------------OVER VERZIU CISELNIKOV-------------------------------------------------//
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@GetMapping(path = "/commmax/oververziu", produces = "text/plain")
	public String getOververziu(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID) {
		return jruzService.getOververziu(pID, evID, dID);
	}
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/oververziu/xml", produces = "text/plain")
	public String feedOververziu(@RequestBody OverVerziu oververziu, @RequestParam String evID) {

		return xmlService.updateOververziuXml(oververziu, evID);

	}
	
	
	@CrossOrigin(origins = { "https://ehealth-ng-app.herokuapp.com", "http://localhost:4200" })
	@PostMapping(path = "/oververziu", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = { "application/xml", "text/xml" })
	public String getOververziuXml(String pID, String evID, String dID) {
		
		try {			
			return new String(s3XmlService.downloadPublicFile(evID, "oververziu"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		
		return commmaxService.getCommmaxTemplate("oververziu.xml");
	}
	
	//----------------------------------------------------------------------------------------//

	
	

	@PostMapping(path = "/log", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> postCommaxLog(@RequestParam("dID") String doctorID, @RequestParam("evID") String evID,
			@RequestParam("pID") String patientID, @RequestParam("signed") String signedMsg,
			@RequestParam("return") String returnMsg, @RequestParam("code") String code,
			@RequestParam(value = "detail", required = false) String detail,
			@RequestParam(value = "message", required = false) String message, @RequestParam("method") String method){
		try {
			signedMsg = java.net.URLDecoder.decode(signedMsg, StandardCharsets.UTF_8);
			returnMsg = java.net.URLDecoder.decode(returnMsg, StandardCharsets.UTF_8);
			detail = java.net.URLDecoder.decode(detail, StandardCharsets.UTF_8);

			logger.trace("dID: " + doctorID);
			logger.trace("evID: " + evID);
			logger.trace("pID: " + patientID);
			logger.trace("signed: " + signedMsg);
			logger.trace("return: " + returnMsg);
			logger.trace("detail: " + detail);

			if ("dajpacientskysumarudaje".equals(method)) {
				DajPrZSResponse dajPrZSresp = null;
				detail = java.net.URLDecoder.decode(detail, StandardCharsets.UTF_8);

				if (!(detail.isBlank() || detail.isEmpty())) {
					String dajPrZS_Response = detail.substring(detail.indexOf("<DajPrZS_Response"),
							detail.indexOf("</DajPrZS_Response>") + "</DajPrZS_Response>".length());

					JAXBContext jaxbContext = JAXBContext.newInstance(DajPrZSResponse.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					StringReader reader = new StringReader(dajPrZS_Response);
					dajPrZSresp = (DajPrZSResponse) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));
					logger.trace(dajPrZSresp.getPrijimatelZS().getPersonData().getPhysicalAddresses().get(0)
							.getAddressLine());
				}
			}

			if ("dajpacientskysumar".equals(method)) {

				JAXBContext jaxbContext = JAXBContext.newInstance(sk.gov.ehealth.ehtalkmessage.ObjectFactory.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				StringReader reader = new StringReader(returnMsg);
				EHtalkMessage eHtalkMessage = (EHtalkMessage) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));

				Node node = eHtalkMessage.getBody().getData().getAnies().get(0);

				StringWriter writer = new StringWriter();
				Transformer transformer = null;
				try {
					transformer = TransformerFactory.newInstance().newTransformer();
				} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
					e.printStackTrace();
				}
				try {
					transformer.transform(new DOMSource(node), new StreamResult(writer));
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String xml = writer.toString();

				returnMsg = java.net.URLDecoder.decode(returnMsg, StandardCharsets.UTF_8);

				JAXBContext jaxbContext2 = JAXBContext.newInstance(EHREXTRACT.class);
				Unmarshaller unmarshaller2 = jaxbContext2.createUnmarshaller();
				StringReader reader2 = new StringReader(xml);

				EHREXTRACT dajSumarResp = null;
				dajSumarResp = (EHREXTRACT) JAXBIntrospector.getValue(unmarshaller2.unmarshal(reader2));

				logger.debug(dajSumarResp.getAllCompositions().toString());

				ObjectMapper mapper = new ObjectMapper();

				List<COMPOSITION> problemsComposition = dajSumarResp.getAllCompositions().stream()
						.filter(composition -> composition.getArchetypeId()
								.startsWith("CEN-EN13606-ENTRY.Pouzivana_zdravotnicka_pomocka.v2"))
						.collect(Collectors.toList());

				try {
					String json = mapper
							.writeValueAsString(dajSumarResp.getAllCompositions().get(0).getName().getOriginalText());
					System.out.println("ResultingJSONstring = " + json);
					// System.out.println(json);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			}

			if ("dajzaznamovysetreni".equals(method)) {

				JAXBContext jaxbContext = JAXBContext.newInstance(sk.gov.ehealth.ehtalkmessage.ObjectFactory.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				StringReader reader = new StringReader(returnMsg);
				EHtalkMessage eHtalkMessage = (EHtalkMessage) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));

				Node node = eHtalkMessage.getBody().getData().getAnies().get(0);

				StringWriter writer = new StringWriter();
				Transformer transformer = null;
				try {
					transformer = TransformerFactory.newInstance().newTransformer();
				} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					transformer.transform(new DOMSource(node), new StreamResult(writer));
				} catch (TransformerException e) {
					e.printStackTrace();
				}
				String xml = writer.toString();

				returnMsg = java.net.URLDecoder.decode(returnMsg, StandardCharsets.UTF_8);

				jaxbContext = JAXBContext.newInstance(EHREXTRACT.class);
				unmarshaller = jaxbContext.createUnmarshaller();
				reader = new StringReader(xml);

				try {
					EHREXTRACT dajZaznam = (EHREXTRACT) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));
					logger.debug(dajZaznam.getAllCompositions().toString());
				} catch (UnmarshalException unme) {
					logger.debug("Not possible to unmarshall ehrextract");
					unme.printStackTrace();
				}

			}

			commmaxService.save(
					new NcziResponse(doctorID, evID, patientID, signedMsg, returnMsg, code, detail, message, method));
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());

		}
		return ResponseEntity.ok("OK");
	}

}