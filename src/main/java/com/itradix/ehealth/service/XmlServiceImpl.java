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
import com.itradix.ehealth.model.Patient;
import com.itradix.ehealth.model.XmlTempObject;

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
	
	public String updateOupzsXml(String dateTime, String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/dajoupzs.xml");
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
	
	public String updatePacientskySumarXml(String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/dajpacientskysumar.xml");
		String dajPacientskySumarXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajPacientskySumarXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", ciselnik);
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(dajPacientskySumarXml);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		
		return dajPacientskySumarXml;

	}
	
	public String updatePacientskySumarUdajeXml(String ciselnik) {
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
	
	
	public String updateJruzidXml(String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/jruzid.xml");
		String dajJruzidXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajJruzidXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", ciselnik);
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(dajJruzidXml);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		
		return dajJruzidXml;

	}
	
	public String updateDajZaznamOVysetreniXml(String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/dajzaznamovysetreni.xml");
		String dajZaznamOVysetreniXmlUdaje;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			dajZaznamOVysetreniXmlUdaje = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", ciselnik);
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(dajZaznamOVysetreniXmlUdaje);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		
		return dajZaznamOVysetreniXmlUdaje;

	}
	
	public String updateZapisSumarUdajeXml(String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/zapissumarudaje.xml");
		String zapisSumarUdajeXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisSumarUdajeXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", ciselnik);
			zapisSumarUdajeXml = StringUtils.replace(zapisSumarUdajeXml, "{{rc_id}}", IdGenService.genId(1));
			
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zapisSumarUdajeXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile("zapissumarudaje.xml",f);
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(zapisSumarUdajeXml);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		
		return zapisSumarUdajeXml;

	}
	
	public String updateZapisSumarProblemyXml(String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/zapissumarproblemy.xml");
		String zapisSumarProblemyXml;
		
		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			zapisSumarProblemyXml = StringUtils.replace(FileCopyUtils.copyToString(reader), "{{classification}}", ciselnik);
			zapisSumarProblemyXml = StringUtils.replace(zapisSumarProblemyXml, "{{rc_id}}", IdGenService.genId(1));
			File f = new File("tempfile");
			FileUtils.writeStringToFile(f, zapisSumarProblemyXml, StandardCharsets.UTF_8);
			s3ImageService.uploadPublicFile("zapissumarproblemy.xml",f);
			
			
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
		XmlTempObject xmlTempObject = new XmlTempObject(zapisSumarProblemyXml);	
	    this.lastXmlId = this.save(xmlTempObject).getId();
		
		return zapisSumarProblemyXml;

	}

	public Long getLastXmlId() {
		return lastXmlId;
	}

	public void setLastXmlId(Long lastXmlId) {
		this.lastXmlId = lastXmlId;
	}
}
