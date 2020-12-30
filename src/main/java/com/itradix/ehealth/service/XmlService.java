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

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import ch.qos.logback.classic.Logger;

@Service
@Transactional
public class XmlService {

	@Autowired
	private ResourceLoader resourceLoader;

	private static final Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(XmlService.class);

	public XmlService() {

	}

	public String updateOververziuXml(String dateTime, String ciselnik) {
		Resource resource = resourceLoader.getResource("classpath:static/oververziu.xml");

		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

			String overVerziuXml = FileCopyUtils.copyToString(reader);

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

			InputStream inputStream = new ByteArrayInputStream(overVerziuXml.getBytes("UTF-8"));
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, byteStream);
			
					        
	               
			

			//File file = new File("temp.xml");
			//logger.debug(file.getAbsolutePath());
	        //logger.debug(file.getCanonicalPath());
			//Resource res = new ClassPathResource("/static/oververziu.xml");
			
			ByteArrayOutputStream byteStream2 = new ByteArrayOutputStream();
			IOUtils.copy(resource.getInputStream(), byteStream2);
			
			byteStream2.write(byteStream.toByteArray());
	        //File convFile = File.createTempFile("temp", ".xml");
	        //logger.debug(file.getAbsolutePath());
	        //logger.debug(file.getCanonicalPath());
	        // choose your own extension I guess? Filename accessible with convFile.getAbsolutePath()
	        //FileOutputStream fos = new FileOutputStream(file); 
	        //fos.write(byteStream.toByteArray());
	        //fos.close(); 

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		return ciselnik;

	}
}
