package com.itradix.ehealth.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
@Configurable
public class JruzIdService {
	
	private final WebClient client;
	
		public JruzIdService() {

			client = WebClient
				  .builder()
				    .baseUrl("http://localhost:3377")
				    .defaultCookie("cookieKey", "cookieValue")
				    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
				  .build();
	}

		public String getJruzId(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/GET_JRUZID?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();
			
		}
		
		public String getZpr(String pID, String evID, String dID){
			String url = new StringBuilder().append("/DAJZPR?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();
			
		}
		
		public String getOupzs(String pID, String evID, String dID){
			String url = new StringBuilder().append("/DAJOUPZS?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();		
		}
		
		public String getVyhladajZaznamy(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/VYHLADAJ_ZAZNAMY?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String getVyhladajZaznamyPreZiadatela(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/VYHLADAJ_ZAZNAMY_PREZIADATELA?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String getDajZaznamOVysetreni(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/VYHLADAJ_ZAZNAM?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String zapisVysetrenie(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/REG_VYSETRENIE?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String zapisSumarProblemy(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/ZAPIS_SUMAR_PROBLEMY?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String zapisSumarUdaje(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/ZAPIS_SUMAR_UDAJE?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String getSumar(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/DAJ_SUMAR?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String getSumarEds(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/DAJ_SUMAR_EDS?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		public String getSumarUdaje(String pID, String evID, String dID, String patient){
			String url = new StringBuilder().append("/DAJ_SUMAR_UDAJE?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).append("&patient=").append(patient).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
		
		
		public String getOververziu(String pID, String evID, String dID){
			String url = new StringBuilder().append("/OVERVERZIU?").append("pID=").append(pID).append("&evID=").append(evID).append("&dID=").append(dID).toString();
			Mono<String> response = client.post().uri(url).retrieve().bodyToMono(String.class);
			return response.block();			
		}
	

}
