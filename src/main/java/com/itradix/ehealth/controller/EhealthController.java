package com.itradix.ehealth.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.itradix.ehealth.dto.PatientDTO;
import com.itradix.ehealth.exception.PatientNotFoundException;
import com.itradix.ehealth.model.EzClassifications;
import com.itradix.ehealth.model.NcziResponse;
import com.itradix.ehealth.model.Patient;
import com.itradix.ehealth.model.XmlTempObject;
import com.itradix.ehealth.service.CommMaxService;
import com.itradix.ehealth.service.CommMaxServiceImpl;
import com.itradix.ehealth.service.JruzIdService;
import com.itradix.ehealth.service.PatientService;
import com.itradix.ehealth.service.PatientServiceImpl;
import com.itradix.ehealth.service.XmlServiceImpl;

import cen._13606.rm.EHREXTRACT;
import ch.qos.logback.classic.Logger;
import sk.gov.ehealth.ehtalkmessage.EHtalkMessage;
import sk.gov.ehealth.jruz.v1.DajPrZSResponse;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	private static final Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(EhealthController.class);

	@Autowired
	public EhealthController(PatientServiceImpl patientService, JruzIdService jruzService,
			CommMaxServiceImpl commmaxService, XmlServiceImpl xmlService) {
		this.patientService = patientService;
		this.jruzService = jruzService;
		this.commmaxService = commmaxService;
		this.xmlService = xmlService;
	}

	@GetMapping("/patient/{id}")
	@ResponseBody
	public Patient get(@PathVariable Long id) throws PatientNotFoundException {
		return patientService.findById(id).orElseThrow(() -> PatientNotFoundException.createWith(id.toString()));

	}
	
		
	@CrossOrigin(origins = {"https://ehealth-ng-app.herokuapp.com", "http://localhost:4200"})
	@GetMapping(path="/classifications/{oid}/{oid_ver}", produces = "application/json")
	public List<Object> getZdravotnickaOdbornostList(@PathVariable String oid, @PathVariable String oid_ver) {
		
		List<Object> ezClassifications = commmaxService.findZdravotnickaOdbornostList(oid, oid_ver);
		if(!ezClassifications.isEmpty()) {
			return ezClassifications;
		}else {
			return Collections.emptyList();
		}
	}
	
	@CrossOrigin(origins = {"https://ehealth-ng-app.herokuapp.com", "http://localhost:4200"})
	@PostMapping(path="/oververziu/xml", produces = "text/plain")
	public String feedOververziu(@RequestParam String date, @RequestParam String classification) {
		
		XmlTempObject xmlTempObject = new XmlTempObject(xmlService.updateOververziuXml(date, classification));	
		return xmlTempObject.getXmlobject();
		
	}
	
	@CrossOrigin(origins = {"https://ehealth-ng-app.herokuapp.com", "http://localhost:4200"})
	@PostMapping(path="/dajpacientskysumar/xml", produces = "text/plain")
	public String feedDajPacientskySumar(@RequestParam String classification) {
		
		XmlTempObject xmlTempObject = new XmlTempObject(xmlService.updatePacientskySumarXml(classification));	
		return xmlTempObject.getXmlobject();
		
	}
	
	@CrossOrigin(origins = {"https://ehealth-ng-app.herokuapp.com", "http://localhost:4200"})
	@PostMapping(path="/dajpacientskysumarudaje/xml", produces = "text/plain")
	public String feedDajPacientskySumarUdaje(@RequestParam String classification) {
		
		XmlTempObject xmlTempObject = new XmlTempObject(xmlService.updatePacientskySumarUdajeXml(classification));	
		return xmlTempObject.getXmlobject();
		
	}
	
	

	@PostMapping(path = "/patient/save", consumes = "application/json", produces = "application/json")
	public Patient index(@RequestBody PatientDTO patientDto) {

		Patient patient = new Patient(patientDto.getBirthDate(), patientDto.getEmail(), patientDto.getBloodGroup(),
				patientDto.getFirstName(), patientDto.getGender(), patientDto.getLastNames(), patientDto.getMessage());
		return patientService.save(patient);
	}

	@PostMapping(path = "/commmax/zapissumarproblemy")
	public String zapisSumarProblemy(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.zapisSumarProblemy(pID, evID, dID, patient);
	}

	@PostMapping(path = "/zapissumarproblemy", produces = { "application/xml", "text/xml" })
	public String getZapisSumarProblemyXml() {
		return commmaxService.getCommmaxTemplate("zapissumarproblemy.xml");
	}

	@PostMapping(path = "/commmax/vysetrenie")
	public String zapisVysetrenie(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.zapisVysetrenie(pID, evID, dID, patient);
	}

	@PostMapping(path = "/vysetrenie", produces = { "application/xml", "text/xml" })
	public String getVysetrenieXml() {
		return commmaxService.getCommmaxTemplate("vysetrenie.xml");
	}

	@GetMapping(path = "/commmax/vyhladajzaznamypreziadatela")
	public String getVyhladajZaznamyPreZiadatela(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getVyhladajZaznamyPreZiadatela(pID, evID, dID, patient);
	}

	@PostMapping(path = "/vyhladajzaznamypreziadatela", produces = { "application/xml", "text/xml" })
	public String getVyhladajZaznamyPreZiadatelaXml() {
		return commmaxService.getCommmaxTemplate("vyhladajzaznamypreziadatela.xml");
	}
	
	@GetMapping(path = "/commmax/vyhladajzaznamy")
	public String getVyhladajZaznamy(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getVyhladajZaznamy(pID, evID, dID, patient);
	}

	@PostMapping(path = "/vyhladajzaznamy", produces = { "application/xml", "text/xml" })
	public String getVyhladajZaznamyXml() {
		return commmaxService.getCommmaxTemplate("vyhladajzaznamy.xml");
	}
	
	@GetMapping(path = "/commmax/dajzaznamovysetreni")
	public String getDajZaznamOVysetreni(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getDajZaznamOVysetreni(pID, evID, dID, patient);
	}
	
	@PostMapping(path = "/dajzaznamovysetreni", produces = { "application/xml", "text/xml" })
	public String getDajZaznamOVysetreniXml() {
		return commmaxService.getCommmaxTemplate("dajzaznamovysetreni.xml");
	}
		
	@GetMapping(path = "/commmax/dajpacientskysumareds")
	public String getPacientskySumarEds(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getSumarEds(pID, evID, dID, patient);
	}

	@PostMapping(path = "/dajpacientskysumareds", produces = { "application/xml", "text/xml" })
	public String getPacientskySumarEdsXml() {
		return commmaxService.getCommmaxTemplate("dajpacientskysumareds.xml");
	}

	@GetMapping(path = "/commmax/dajpacientskysumar")
	public String getPacientskySumar(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getSumar(pID, evID, dID, patient);
	}

	@PostMapping(path = "/dajpacientskysumar", produces = { "application/xml", "text/xml" })
	public String getPacientskySumarXml() {
		//return commmaxService.getCommmaxTemplate("dajpacientskysumar.xml");
		return xmlService.findById(xmlService.getLastXmlId()).get().getXmlobject();
	}

	@GetMapping(path = "/commmax/dajpacientskysumarudaje", produces = { "text/plain" })
	public String getPacientskySumarUdaje(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getSumarUdaje(pID, evID, dID, patient);
	}

	@PostMapping(path = "/dajpacientskysumarudaje", produces = { "application/xml", "text/xml" })
	public String getPacientskySumarUdajeXml() {
		return xmlService.findById(xmlService.getLastXmlId()).get().getXmlobject();
	}

	@PostMapping(path = "/dajzpr", produces = { "application/xml", "text/xml" })
	public String getZpr() {
		return commmaxService.getCommmaxTemplate("dajzpr.xml");
	}

	@GetMapping(path = "/commmax/dajzpr")
	public String getZpr(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID) {
		return jruzService.getZpr(pID, evID, dID);
	}

	@PostMapping(path = "/dajoupzs", produces = { "application/xml", "text/xml" })
	public String getOupzs() {
		return commmaxService.getCommmaxTemplate("dajoupzs.xml");
	}

	@GetMapping(path = "/commmax/dajoupzs")
	public String getOupzs(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID) {
		return jruzService.getOupzs(pID, evID, dID);
	}

	@GetMapping(path = "/commmax/jruzid")
	public String getJruzId(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID,
			@RequestParam String patient) {
		return jruzService.getJruzId(pID, evID, dID, patient);
	}

	@PostMapping(path = "/jruzid", produces = { "application/xml", "text/xml" })
	public String getJruzIdXml() {
		return commmaxService.getCommmaxTemplate("jruzid.xml");
	}

	@CrossOrigin(origins = {"https://ehealth-ng-app.herokuapp.com", "http://localhost:4200"})
	@GetMapping(path = "/commmax/oververziu", produces = "text/plain")
	public String getOververziu(@RequestParam String pID, @RequestParam String evID, @RequestParam String dID) {
		return jruzService.getOververziu(pID, evID, dID);
	}
	
	@CrossOrigin(origins = {"https://ehealth-ng-app.herokuapp.com", "http://localhost:4200"})
	@GetMapping(path = "/getehealthresponse", produces = "text/plain")
	public String getEhealthResponse(@RequestParam String evId) {
		
		List<NcziResponse> ncziresponse = commmaxService.findNcziRespUsingNativeQuery(evId);
		if(!ncziresponse.isEmpty()) {
			return ncziresponse.get(0).getReturnMsg();
		}else {
			return "Nepodarilo sa vytiahnut spravu";
		}
	}	

	@PostMapping(path = "/oververziu", produces = { "application/xml", "text/xml" })
	public String getOververziuXml() {
		return xmlService.findById(xmlService.getLastXmlId()).get().getXmlobject();
		//return commmaxService.getCommmaxTemplate("oververziu.xml");
	}

	@PostMapping(path = "/log", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> postCommaxLog(@RequestParam("dID") String doctorID, @RequestParam("evID") String evID,
			@RequestParam("pID") String patientID, @RequestParam("signed") String signedMsg,
			@RequestParam("return") String returnMsg, @RequestParam("code") String code,
			@RequestParam(value = "detail", required = false) String detail,
			@RequestParam(value = "message", required = false) String message, @RequestParam("method") String method)
			throws IOException, JAXBException {
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

			if(!(detail.isBlank() || detail.isEmpty())) {
				String dajPrZS_Response = detail.substring(detail.indexOf("<DajPrZS_Response"),
					detail.indexOf("</DajPrZS_Response>") + "</DajPrZS_Response>".length());
					
				JAXBContext jaxbContext = JAXBContext.newInstance(DajPrZSResponse.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				StringReader reader = new StringReader(dajPrZS_Response);
				dajPrZSresp = (DajPrZSResponse) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));
				logger.trace(dajPrZSresp.getPrijimatelZS().getPersonData().getPhysicalAddresses().get(0).getAddressLine());
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

			EHREXTRACT dajZaznam = (EHREXTRACT) JAXBIntrospector.getValue(unmarshaller.unmarshal(reader));

			logger.debug(dajZaznam.getAllCompositions().toString());

		}

		commmaxService
				.save(new NcziResponse(doctorID, evID, patientID, signedMsg, returnMsg, code, detail, message, method));
		return ResponseEntity.ok("OK");
	}

}