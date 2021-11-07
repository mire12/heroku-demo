package com.itradix.ehealth.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itradix.ehealth.dao.BaseRepository;
import com.itradix.ehealth.dao.PatientRepository;
import com.itradix.ehealth.dao.XmlRepository;
import com.itradix.ehealth.model.DajJruzId;
import com.itradix.ehealth.model.DajOdobornyUtvarPoskytovatelaZS;
import com.itradix.ehealth.model.DajSumar;
import com.itradix.ehealth.model.DajSumarEds;
import com.itradix.ehealth.model.DajZaznamOVysetreni;
import com.itradix.ehealth.model.DajZpr;
import com.itradix.ehealth.model.Patient;
import com.itradix.ehealth.model.StornujZaznamOVysetreni;
import com.itradix.ehealth.model.XmlTempObject;
import com.itradix.ehealth.model.ZapisSumarProblemy;
import com.itradix.ehealth.model.ZapisSumarUdaje;
import com.itradix.ehealth.model.ZapisZaznamOVysetreni;
import com.itradix.ehealth.model.ZrusSumar;

import ch.qos.logback.classic.Logger;

@Service
@Transactional
public class XmlServiceImpl extends BaseRepositoryImpl<XmlTempObject, Long> implements XmlService{
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private S3XmlService s3ImageService;
	
	private Long lastXmlId = -1L;

	private static final Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(XmlServiceImpl.class);
	
	
	public XmlServiceImpl(XmlRepository abstractBaseRepository) {
		super(abstractBaseRepository);
	}

	public String updateOververziuXml(String dateTime, String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/oververziu.xml");
		String overVerziuXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

			overVerziuXml = FileCopyUtils.copyToString(reader);

			String formattedDate;
			try {
				LocalDate localDate = LocalDate.parse(dateTime);
				formattedDate = localDate.format(DateTimeFormatter.ISO_INSTANT);

			} catch (DateTimeException parseEx) {
				parseEx.printStackTrace();
				logger.warn("Not possible to parse date: " + dateTime + ". Required format: yyyy-MM-dd'T'HH:mm:ssZ");
				Clock cl = Clock.systemUTC(); 
				formattedDate = Instant.now(cl).toString();
			}

			overVerziuXml = StringUtils.replace(overVerziuXml, "{{date}}", formattedDate);
			overVerziuXml = StringUtils.replace(overVerziuXml, "{{classification}}", ciselnik);
			
			

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(overVerziuXml);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		

		return overVerziuXml;

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
		
	public String updateDajPacientskySumarUdajeXml(String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/dajpacientskysumarudaje.xml");
		String dajPacientskySumarXmlUdaje;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajPacientskySumarXmlUdaje = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", ciselnik);
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(dajPacientskySumarXmlUdaje);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		
		return dajPacientskySumarXmlUdaje;

	}
	
	
	public String updateZapisZaznamOVysetreniXml(ZapisZaznamOVysetreni zapisZaznamOVysetreni, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/vysetrenie.xml");
		String zapisZaznamOVysetreniXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisZaznamOVysetreniXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", zapisZaznamOVysetreni.getClassification());
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{rc_id}}", IdGenService.genId(1));
			zapisZaznamOVysetreniXml = StringUtils.replace(zapisZaznamOVysetreniXml, "{{datetime}}", zapisZaznamOVysetreni.getDatetime());
			
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
			dajPacientskySumarEdsXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", dajSumarEds.getClassification());
			dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{rc_id}}", IdGenService.genId(1));
			//dajPacientskySumarEdsXml = StringUtils.replace(dajPacientskySumarEdsXml, "{{datetime}}", dajSumarEds.getDatetime());
			
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
	
	public String updateJruzidXml(DajJruzId dajJruzId, String evId) {
		Resource resource = resourceLoader.getResource("classpath:static/jruzid.xml");
		String dajJruzidXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajJruzidXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", dajJruzId.getClassification());
			//dajJruzidXml = StringUtils.replace(dajJruzidXml, "{{rc_id}}", dajJruzidXml.getRcId());
			
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
			zapisSumarProblemyXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", zapisSumarProblemy.getClassification());
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
