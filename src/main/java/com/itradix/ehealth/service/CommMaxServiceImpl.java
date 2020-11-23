package com.itradix.ehealth.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.itradix.ehealth.dao.NcziResponseRepository;
import com.itradix.ehealth.model.NcziResponse;

//import sk.gov.ehealth.common.v1.UserContext;
//import sk.gov.ehealth.ehtalkmessage.EHtalkMessage;
//import sk.gov.ehealth.jruz.v1.DajJRUZIdentifikatorKriteria;
//import sk.gov.ehealth.jruz.v1.OID;

@Service
@Transactional
public class CommMaxServiceImpl extends BaseRepositoryImpl<NcziResponse, Long> implements CommMaxService {
	
	public CommMaxServiceImpl(NcziResponseRepository abstractBaseRepository) {
		super(abstractBaseRepository);
	}

	public static <T> T stringToObject(final Resource res, final Class<T> clazz) throws IOException {
		T returnedValue = null;
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			returnedValue = (T) unmarshaller.unmarshal(res.getFile());
		} catch (final JAXBException e) {
			throw new RuntimeException(
					"an error occurred while unmarshalling xml into " + clazz.getCanonicalName() + " object", e);
		}
		return returnedValue;
	}

	public String getCommmaxTemplate(String templateName) {

		ClassLoader cl = this.getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource resource = resolver.getResource("classpath:commmax-templates/" + templateName);
		return asString(resource);

	}

	private static String asString(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private void jaxbXmlFileToObject(ResourceLoader resourceLoader) throws IOException, JAXBException {

		/*ClassLoader cl = this.getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource resource = resolver.getResource("classpath:commmax-templates/jruzid.xml");

		JAXBContext jaxbContext = JAXBContext.newInstance(sk.gov.ehealth.ehtalkmessage.ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		EHtalkMessage eHtalkMessage = (EHtalkMessage) JAXBIntrospector
				.getValue(unmarshaller.unmarshal(resource.getFile()));

		eHtalkMessage.getBody().getData().getAnies().get(0).getAttributes();
		UserContext userContext = eHtalkMessage.getHeader().getSenderInfo().getUserContext();
		userContext.getSpecialization().setCodeValue("blabla");
		// request.getDajJRUZIdentifikatorKriteria();

		DajJRUZIdentifikatorKriteria jruzKriteria = new DajJRUZIdentifikatorKriteria();

		new OID().setOid("1.3.158.00165387.100.40.110");
		jruzKriteria.setTypHladanehoId(new OID());

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(eHtalkMessage, System.out)();

		/*
		 * ClassLoader cl = this.getClass().getClassLoader(); ResourcePatternResolver
		 * resolver = new PathMatchingResourcePatternResolver(cl); Resource[] resources
		 * = resolver.getResources("classpath*:ciselniky_balik_2/*.xml") ; for (Resource
		 * resource: resources){ JAXBContext jaxbContext; try { jaxbContext =
		 * JAXBContext.newInstance(Ciselnik.class); Unmarshaller jaxbUnmarshaller =
		 * jaxbContext.createUnmarshaller(); Ciselnik employee = (Ciselnik)
		 * jaxbUnmarshaller.unmarshal(resource.getFile());
		 * 
		 * System.out.println(employee.getZahlavieCiselnika().getCodingScheme().getOid()
		 * + ":" + employee.getZahlavieCiselnika().getCodingSchemeName() + ":" +
		 * employee.getZahlavieCiselnika().getCodingSchemeVersion());
		 * for(PolozkaCiselnikaOdpis odpis:
		 * employee.getPolozkaCiselnikaZoznam().getPolozkaCiselnikas()) {
		 * //System.out.println(odpis.getDisplayName()); }
		 * 
		 * } catch (JAXBException e) { e.printStackTrace(); } }
		 */

	}

}
