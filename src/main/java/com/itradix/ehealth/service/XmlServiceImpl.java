package com.itradix.ehealth.service;

import java.io.File;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itradix.ehealth.dto.DajJruzIdDTO;
import com.itradix.ehealth.model.DajJruzId;
import com.itradix.ehealth.model.DajOdobornyUtvarPoskytovatelaZS;
import com.itradix.ehealth.model.DajSumar;
import com.itradix.ehealth.model.DajSumarEds;
import com.itradix.ehealth.model.DajSumarUdaje;
import com.itradix.ehealth.model.DajZaznamOVysetreni;
import com.itradix.ehealth.model.DajZpr;
import com.itradix.ehealth.model.OverVerziu;
import com.itradix.ehealth.model.StornujZaznamOVysetreni;
import com.itradix.ehealth.model.VyhladajZaznamOvysetreniPreZiadatela;
import com.itradix.ehealth.model.VyhladajZaznamyOvysetreni;
import com.itradix.ehealth.model.ZapisSuhlasOsobyPrePZS;
import com.itradix.ehealth.model.ZapisSumarProblemy;
import com.itradix.ehealth.model.ZapisSumarUdaje;
import com.itradix.ehealth.model.ZapisZaznamOVysetreni;
import com.itradix.ehealth.model.ZrusSumar;

import ch.qos.logback.classic.Logger;

@Service
@Transactional
public class XmlServiceImpl implements XmlService{
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private S3XmlService s3ImageService;
	
	private Long lastXmlId = -1L;

	private static final Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(XmlServiceImpl.class);
	
	
	public XmlServiceImpl() {
		super();
	}

	public String updateOververziuXml(OverVerziu oververziu, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/oververziu.xml");
		String overVerziuXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

			overVerziuXml = FileCopyUtils.copyToString(reader);

			String formattedDate;
			try {
				LocalDate localDate = LocalDate.parse(oververziu.getDate());
				formattedDate = localDate.format(DateTimeFormatter.ISO_INSTANT);

			} catch (DateTimeException parseEx) {
				parseEx.printStackTrace();
				logger.warn("Not possible to parse date: " + oververziu.getDate() + ". Required format: yyyy-MM-dd'T'HH:mm:ssZ");
				Clock cl = Clock.systemUTC(); 
				formattedDate = Instant.now(cl).toString();
			}

			overVerziuXml = StringUtils.replace(overVerziuXml, "{{date}}", formattedDate);
			overVerziuXml = StringUtils.replace(overVerziuXml, "{{classification}}", oververziu.getClassification());
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, overVerziuXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-oververziu.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";
		


	}
	
	
	public String updateZprXml(DajZpr dajZdravotnehoPracovnika, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/dajzpr.xml");
		String dajZprXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

			dajZprXml = FileCopyUtils.copyToString(reader);


			dajZprXml = StringUtils.replace(dajZprXml, "{{oupzs}}", dajZdravotnehoPracovnika.getOupzs());
			dajZprXml = StringUtils.replace(dajZprXml, "{{classification}}", dajZdravotnehoPracovnika.getClassification());
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajZprXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-dajzpr.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";
		
	
	}
	
	public String updateOupzsXml(DajOdobornyUtvarPoskytovatelaZS dajOdobornyUtvarPoskytovatelaZS, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/dajoupzs.xml");
		String dajOupZSXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

			dajOupZSXml = FileCopyUtils.copyToString(reader);

			String formattedDate;
			try {
				LocalDate localDate = LocalDate.parse(dajOdobornyUtvarPoskytovatelaZS.getDate());
				formattedDate = localDate.format(DateTimeFormatter.ISO_INSTANT);

			} catch (DateTimeException parseEx) {
				parseEx.printStackTrace();
				logger.warn("Not possible to parse date: " + dajOdobornyUtvarPoskytovatelaZS.getDate() + ". Required format: yyyy-MM-dd'T'HH:mm:ssZ");
				Clock cl = Clock.systemUTC(); 
				formattedDate = Instant.now(cl).toString();
			}

			dajOupZSXml = StringUtils.replace(dajOupZSXml, "{{date}}", formattedDate);
			dajOupZSXml = StringUtils.replace(dajOupZSXml, "{{classification}}", dajOdobornyUtvarPoskytovatelaZS.getClassification());
			dajOupZSXml = StringUtils.replace(dajOupZSXml, "{{oupzs}}", dajOdobornyUtvarPoskytovatelaZS.getOupzs());
			dajOupZSXml = StringUtils.replace(dajOupZSXml, "{{find_oupzs}}", dajOdobornyUtvarPoskytovatelaZS.getFindOupzs());
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajOupZSXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-dajoupzs.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";
		
	
	}
	
	public String updateDajPacientskySumarXml(DajSumar dajSumar, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/dajpacientskysumar.xml");
		String dajPacientskySumarXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajPacientskySumarXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", dajSumar.getClassification());
			dajPacientskySumarXml = StringUtils.replace(dajPacientskySumarXml, "{{rc_id}}", IdGenService.genId(1));
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajPacientskySumarXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-dajpacientskysumar.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	public String updateZrusPacientskySumarXml(ZrusSumar zrusSumar, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/zruszapissumar.xml");
		String zrusPacientskySumarXml;
		String archetypeId = null;
		
		switch (zrusSumar.getRcIdOid())
        {
            case "1.3.158.00165387.100.50.40.100": archetypeId = "CEN-EN13606-ENTRY.Pouzivana_zdravotnicka_pomocka.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.120": archetypeId = "CEN-EN13606-ENTRY.Zdravotny_problem.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.50": archetypeId = "CEN-EN13606-COMPOSITION.Varovania.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.60": archetypeId = "CEN-EN13606-COMPOSITION.Porodnicke_zaznamy.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.30": archetypeId = "CEN-EN13606-ENTRY.Krvna_skupina.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.40": archetypeId = "CEN-EN13606-ENTRY.Krvny_tlak.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.90": archetypeId = "CEN-EN13606-ENTRY.Vitalne_a_antropometricke_hodnoty.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.10": archetypeId = "CEN-EN13606-COMPOSITION.Socialna_anamneza_abuzy.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.110": archetypeId = "CEN-EN13606-ENTRY.Zdravotne_obmedzenie.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.20": archetypeId = "CEN-EN13606-ENTRY.Chirurgicky_vykon.v2/at0000"; break;
            case "1.3.158.00165387.100.50.40.70": archetypeId = "CEN-EN13606-ENTRY.Terapeuticke_odporucanie.v2/at0000"; break;

        }
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zrusPacientskySumarXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", zrusSumar.getClassification());
			zrusPacientskySumarXml = StringUtils.replace(zrusPacientskySumarXml, "{{deletion_text}}", zrusSumar.getDeletionText());
			zrusPacientskySumarXml = StringUtils.replace(zrusPacientskySumarXml, "{{version}}", zrusSumar.getDeleteVersion());
			zrusPacientskySumarXml = StringUtils.replace(zrusPacientskySumarXml, "{{rcIdExtensionDeleted}}", zrusSumar.getRcIdExtensionDeleted());
			zrusPacientskySumarXml = StringUtils.replace(zrusPacientskySumarXml, "{{rcIdOid}}", zrusSumar.getRcIdOid());
			zrusPacientskySumarXml = StringUtils.replace(zrusPacientskySumarXml, "{{archetype_id}}", archetypeId);
			zrusPacientskySumarXml = StringUtils.replace(zrusPacientskySumarXml, "{{rc_id}}", IdGenService.genId(1));
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zrusPacientskySumarXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-zruszapissumar.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
		
	public String updateDajPacientskySumarUdajeXml(DajSumarUdaje dajSumarUdaje, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/dajpacientskysumarudaje.xml");
		String dajPacientskySumarudajeXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			
			dajPacientskySumarudajeXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{serviceName}}", "DajPacientskySumarKontaktneUdaje_v4");
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{Guid1}}", UUID.randomUUID().toString());
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{Guid2}}", UUID.randomUUID().toString());
			
			
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{Specialization.codeValue}}", dajSumarUdaje.getUserContext().getSpecialization().getCodeValue());
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{Specialization.codingSchemeOID}}", dajSumarUdaje.getUserContext().getSpecialization().getCodingSchemeOID());
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{Specialization.codingSchemeVersion}}", dajSumarUdaje.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{IdentifikatorOUPZS.extension}}", dajSumarUdaje.getUserContext().getIdentifikatorOUPZS().getExtension());
			dajPacientskySumarudajeXml = StringUtils.replace(dajPacientskySumarudajeXml, "{{IdentifikatorOUPZS.rootOID}}", dajSumarUdaje.getUserContext().getIdentifikatorOUPZS().getRootOID());
			
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajPacientskySumarudajeXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-dajpacientskysumarudaje.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	
	public String updateZapisZaznamOVysetreniXml(ZapisZaznamOVysetreni zapisZaznamOVysetreni, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/vysetrenie.xml");
		String zapisZaznamOVysetreniXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisZaznamOVysetreniXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{serviceName}}", "ZapisZaznamOVysetreni_v6");
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{Guid1}}", UUID.randomUUID().toString());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{Guid2}}", UUID.randomUUID().toString());
			
			
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{Specialization.codeValue}}", zapisZaznamOVysetreni.getUserContext().getSpecialization().getCodeValue());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{Specialization.codingSchemeOID}}", zapisZaznamOVysetreni.getUserContext().getSpecialization().getCodingSchemeOID());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{Specialization.codingSchemeVersion}}", zapisZaznamOVysetreni.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{IdentifikatorOUPZS.extension}}", zapisZaznamOVysetreni.getUserContext().getIdentifikatorOUPZS().getExtension());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{IdentifikatorOUPZS.rootOID}}", zapisZaznamOVysetreni.getUserContext().getIdentifikatorOUPZS().getRootOID());
			
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{id_oupzs}}", zapisZaznamOVysetreni.getUserContext().getIdentifikatorOUPZS().getExtension()); 
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{jruz_idzpr}}", "00020003901");  //idzpr ALes Galko
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{rc_id}}", IdGenService.genId(1));
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{rc_id_2}}", IdGenService.genId(1));
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{vysetrenie_datetime}}", zapisZaznamOVysetreni.getVysetrenieDatetime());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{odoslanie_datetime}}", zapisZaznamOVysetreni.getOdoslanieDatetime());
			
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{UserContextSpecialization}}", zapisZaznamOVysetreni.getUserContext().getSpecialization().getCodeValue());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{UserContextSpecialization.display}}", "všeobecné lekárstvo");
			
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{textovy_popis}}", zapisZaznamOVysetreni.getTextovyPopis());
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zapisZaznamOVysetreniXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evId + "-vysetrenie.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	public String updatePacientskySumarEdsXml(DajSumarEds dajSumarEds, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/dajpacientskysumareds.xml");
		String dajPacientskySumarEdsXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			
			//dajPacientskySumarEdsXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{rc_id}}", IdGenService.genId(1));
			//dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{datetime}}", dajSumarEds.getDatetime());
			
			dajPacientskySumarEdsXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{serviceName}}", "DajPacientskySumarEDS_v2");
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{Guid1}}", UUID.randomUUID().toString());
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{Guid2}}", UUID.randomUUID().toString());
			
			
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{Specialization.codeValue}}", dajSumarEds.getUserContext().getSpecialization().getCodeValue());
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{Specialization.codingSchemeOID}}", dajSumarEds.getUserContext().getSpecialization().getCodingSchemeOID());
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{Specialization.codingSchemeVersion}}", dajSumarEds.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{IdentifikatorOUPZS.extension}}", dajSumarEds.getUserContext().getIdentifikatorOUPZS().getExtension());
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{IdentifikatorOUPZS.rootOID}}", dajSumarEds.getUserContext().getIdentifikatorOUPZS().getRootOID());

			
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajPacientskySumarEdsXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evId + "-dajpacientskysumareds.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	public String updateStornujZaznamOVysetreniXml(StornujZaznamOVysetreni stornujZaznamOVysetreni, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/stornujzaznam.xml");
		String stornujZaznamOVysetreniXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			stornujZaznamOVysetreniXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", stornujZaznamOVysetreni.getClassification());
			stornujZaznamOVysetreniXml = StringUtils.replace(stornujZaznamOVysetreniXml, "{{rc_id_deleted}}", stornujZaznamOVysetreni.getRcIdDeleted());
			stornujZaznamOVysetreniXml = StringUtils.replace(stornujZaznamOVysetreniXml, "{{datetime}}", stornujZaznamOVysetreni.getDatetime());
			stornujZaznamOVysetreniXml = StringUtils.replace(stornujZaznamOVysetreniXml, "{{deletion_text}}", stornujZaznamOVysetreni.getDeletionText());
			stornujZaznamOVysetreniXml = StringUtils.replace(stornujZaznamOVysetreniXml, "{{version}}", stornujZaznamOVysetreni.getDeleteVersion());
			stornujZaznamOVysetreniXml = StringUtils.replace(stornujZaznamOVysetreniXml, "{{rc_id}}", IdGenService.genId(1));
			stornujZaznamOVysetreniXml = StringUtils.replace(stornujZaznamOVysetreniXml, "{{oupzs}}", stornujZaznamOVysetreni.getOupzs());
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, stornujZaznamOVysetreniXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evId + "-stornujzaznam.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	 
	public String updateZapisSuhlasOsobyPrePZSXml(ZapisSuhlasOsobyPrePZS zapisSuhlasOsobyPreZS, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/zapissuhlasosobyprepzs.xml");
		String zapisSuhlasOsobyPreZsXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{Specialization.codeValue}}", zapisSuhlasOsobyPreZS.getUserContext().getSpecialization().getCodeValue());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{Specialization.codingSchemeOID}}", zapisSuhlasOsobyPreZS.getUserContext().getSpecialization().getCodingSchemeOID());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{Specialization.codingSchemeVersion}}", zapisSuhlasOsobyPreZS.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{IdentifikatorOUPZS.extension}}", zapisSuhlasOsobyPreZS.getUserContext().getIdentifikatorOUPZS().getExtension());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{IdentifikatorOUPZS.rootOID}}", zapisSuhlasOsobyPreZS.getUserContext().getIdentifikatorOUPZS().getRootOID());
			
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{citlivost}}", zapisSuhlasOsobyPreZS.getCitlivost());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{platny_do}}", zapisSuhlasOsobyPreZS.getPlatnyDo());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{platny_od}}", zapisSuhlasOsobyPreZS.getPlatnyOd());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{poznamka}}", zapisSuhlasOsobyPreZS.getPoznamka());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{typ_suhlasu}}", zapisSuhlasOsobyPreZS.getTypSuhlasu());
			zapisSuhlasOsobyPreZsXml = StringUtils.replace(zapisSuhlasOsobyPreZsXml, "{{podpisana_dohoda}}", zapisSuhlasOsobyPreZS.getPodpisanaDohoda());
			
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zapisSuhlasOsobyPreZsXml, StandardCharsets.UTF_8);
			
			
			String md5EvId = null;
						
		    try{
		        MessageDigest md = MessageDigest.getInstance("MD5");
		        md.update(evId.getBytes());
		        byte[] digest = md.digest();
		        md5EvId = new BigInteger(1, digest).toString(16);
			
		    } catch (NoSuchAlgorithmException e) {
				logger.error(e.getLocalizedMessage());
			}
			
			s3ImageService.uploadPublicFile(md5EvId + "-zapissuhlasosobyprepzs.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	public String updateJruzidXml(DajJruzIdDTO dajJruzId, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/jruzid.xml");
		String dajJruzidXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajJruzidXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{Specialization.codeValue}}", dajJruzId.getUserContext().getSpecialization().getCodeValue());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{Specialization.codingSchemeOID}}", dajJruzId.getUserContext().getSpecialization().getCodingSchemeOID());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{Specialization.codingSchemeVersion}}", dajJruzId.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{IdentifikatorOUPZS.extension}}", dajJruzId.getUserContext().getIdentifikatorOUPZS().getExtension());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{IdentifikatorOUPZS.rootOID}}", dajJruzId.getUserContext().getIdentifikatorOUPZS().getRootOID());
			
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{ZadaneId}}", dajJruzId.getKriteria().getZadaneId());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{TypHladanehoId.Oid}}", dajJruzId.getKriteria().getTypHladanehoId());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{TypZadanehoId.CodeValue}}", dajJruzId.getKriteria().getTypZadanehoId().getCodeValue());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{TypZadanehoId.CodingScheme.oid}}", dajJruzId.getKriteria().getTypZadanehoId().getCodingSchemeOID());
			dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{TypZadanehoId.CodingSchemeVersion}}", dajJruzId.getKriteria().getTypZadanehoId().getCodingSchemeVersion());
			
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajJruzidXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evId + "-jruzid.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	public String updateDajZaznamOVysetreniXml(DajZaznamOVysetreni dajZaznamOVysetreni, String evID) {
		
		Resource resource = resourceLoader.getResource("classpath:static/dajzaznamovysetreni.xml");
		String dajZaznamOVysetreniXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajZaznamOVysetreniXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", dajZaznamOVysetreni.getClassification());
			dajZaznamOVysetreniXml = StringUtils.replace(dajZaznamOVysetreniXml, "{{rc_id}}", dajZaznamOVysetreni.getRcId());
			dajZaznamOVysetreniXml = StringUtils.replace(dajZaznamOVysetreniXml, "{{oupzs}}", dajZaznamOVysetreni.getOupzs());
			
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, dajZaznamOVysetreniXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-dajzaznamovysetreni.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
public String updateVyhladajZaznamOVysetreniPreZiadatelaXml(VyhladajZaznamOvysetreniPreZiadatela vyhladajZaznamyOvysetreni, String evID) {
		
		Resource resource = resourceLoader.getResource("classpath:static/vyhladajzaznamypreziadatela.xml");
		String vyhladajZaznamyOVysetreniPreZiadatelaXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{serviceName}}", "VyhladajZaznamyOVysetreniPreZiadatela_v6");
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{Guid1}}", UUID.randomUUID().toString());
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{Guid2}}", UUID.randomUUID().toString());
			
			
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{Specialization.codeValue}}", vyhladajZaznamyOvysetreni.getUserContext().getSpecialization().getCodeValue());
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{Specialization.codingSchemeOID}}", vyhladajZaznamyOvysetreni.getUserContext().getSpecialization().getCodingSchemeOID());
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{Specialization.codingSchemeVersion}}", vyhladajZaznamyOvysetreni.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{IdentifikatorOUPZS.extension}}", vyhladajZaznamyOvysetreni.getUserContext().getIdentifikatorOUPZS().getExtension());
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{IdentifikatorOUPZS.rootOID}}", vyhladajZaznamyOvysetreni.getUserContext().getIdentifikatorOUPZS().getRootOID());
			
			vyhladajZaznamyOVysetreniPreZiadatelaXml = StringUtils.replace(vyhladajZaznamyOVysetreniPreZiadatelaXml, "{{externyIDVymennehoListku}}", vyhladajZaznamyOvysetreni.getExternyIDVymennehoListku());
				
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, vyhladajZaznamyOVysetreniPreZiadatelaXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-vyhladajzaznamypreziadatela.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	
	public String updateVyhladajZaznamyOVysetreniXml(VyhladajZaznamyOvysetreni vyhladajZaznamyOvysetreni, String evID) {
		
		Resource resource = resourceLoader.getResource("classpath:static/vyhladajzaznamy.xml");
		String vyhladajZaznamyOVysetreniXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{Specialization.codeValue}}", vyhladajZaznamyOvysetreni.getUserContext().getSpecialization().getCodeValue());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{Specialization.codingSchemeOID}}", vyhladajZaznamyOvysetreni.getUserContext().getSpecialization().getCodingSchemeOID());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{Specialization.codingSchemeVersion}}", vyhladajZaznamyOvysetreni.getUserContext().getSpecialization().getCodingSchemeVersion());
			
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{IdentifikatorOUPZS.extension}}", vyhladajZaznamyOvysetreni.getUserContext().getIdentifikatorOUPZS().getExtension());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{IdentifikatorOUPZS.rootOID}}", vyhladajZaznamyOvysetreni.getUserContext().getIdentifikatorOUPZS().getRootOID());
			
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{datum_od}}", vyhladajZaznamyOvysetreni.getDatumOd());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{datum_do}}", vyhladajZaznamyOvysetreni.getDatumDo());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{citlivost}}", vyhladajZaznamyOvysetreni.getCitlivost());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{kompletny_zaznam}}", vyhladajZaznamyOvysetreni.getKompletnyZaznam());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{popis_medikacnych_zaznamov}}", vyhladajZaznamyOvysetreni.getPopisMedikacnychZaznamov());
			vyhladajZaznamyOVysetreniXml = StringUtils.replace(vyhladajZaznamyOVysetreniXml, "{{vlastne_zaznamy}}", vyhladajZaznamyOvysetreni.getVlastneZaznamy());

			
			
			
			
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, vyhladajZaznamyOVysetreniXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-vyhladajzaznamy.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}
	
	public String updateZapisSumarUdajeXml(ZapisSumarUdaje zapisSumareUdaje, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/zapissumarudaje.xml");
		String zapisSumarUdajeXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisSumarUdajeXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", zapisSumareUdaje.getClassification());
			zapisSumarUdajeXml = StringUtils.replace(zapisSumarUdajeXml, "{{rc_id}}", IdGenService.genId(1));
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zapisSumarUdajeXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-zapissumarudaje.xml",f);
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";


	}
	
	public String updateZapisSumarProblemyXml(ZapisSumarProblemy zapisSumarProblemy, String evID) {
		Resource resource = resourceLoader.getResource("classpath:static/zapissumarproblemy.xml");
		String zapisSumarProblemyXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisSumarProblemyXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{Specialization.codeValue}}", zapisSumarProblemy.getClassification());
			zapisSumarProblemyXml = StringUtils.replace(zapisSumarProblemyXml, "{{rc_id}}", IdGenService.genId(1));
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zapisSumarProblemyXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile(evID + "-zapissumarproblemy.xml",f);
			
			
		} catch (IOException e) {
			
			logger.error("Not possible to prepare xml file and upload file to S3");
			return "Not possible to prepare xml file and upload file to S3";
		}	
		
		return "ok";

	}

	public Long getLastXmlId() {
		return lastXmlId;
	}

	public void setLastXmlId(Long lastXmlId) {
		this.lastXmlId = lastXmlId;
	}
}
