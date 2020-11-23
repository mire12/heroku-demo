package com.itradix.ehealth.service;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openehr.referencemodels.BuiltinReferenceModels;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.nedap.archie.adl14.ADL14ConversionConfiguration;
import com.nedap.archie.adl14.ADL14Converter;
import com.nedap.archie.adl14.ADL14Parser;
import com.nedap.archie.adl14.ADL2ConversionResult;
import com.nedap.archie.adl14.ADL2ConversionResultList;
import com.nedap.archie.adlparser.ADLParser;
import com.nedap.archie.aom.Archetype;
import com.nedap.archie.aom.ArchetypeModelObject;
import com.nedap.archie.query.AOMPathQuery;

@Service
public class AdlParserService {

	public AdlParserService() {
		callParser();
		
	}
	
	public String callParser() {
		
		
		
		/*ClassLoader cl = this.getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource resource = resolver.getResource("classpath:nczi-templates/ALOK/Adl/CEN-EN13606-ENTRY.Zaznam_o_vysetreni-Hlavicka.v1.adl");
		
		
		ADL14ConversionConfiguration conversionConfiguration = new ADL14ConversionConfiguration();
		ADL14Converter converter = new ADL14Converter(BuiltinReferenceModels.getMetaModels(), conversionConfiguration);

		List<Archetype> archetypes = new ArrayList<>();
		ADL14Parser parser = new ADL14Parser(BuiltinReferenceModels.getMetaModels());

		
		   try(InputStream stream = new FileInputStream(resource.getFile())) {
		       Archetype archetype = parser.parse(stream, conversionConfiguration);
		       if(parser.getErrors().hasNoErrors()) {
		           archetypes.add(archetype);
		       } else {
		           //handle parse error
		       }
		   } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		ADL2ConversionResultList resultList = converter.convert(archetypes);
		for(ADL2ConversionResult adl2ConversionResult:resultList.getConversionResults()) {
		   if(adl2ConversionResult.getException() == null) {
		       // convertedArchetype is the ADL 2 conversion result. Additional warning messages in adl2ConversionResult.getLog()
		       Archetype convertedArchetype = adl2ConversionResult.getArchetype();
		   }
		}*/
		
		return "";
	}

	

	
}
