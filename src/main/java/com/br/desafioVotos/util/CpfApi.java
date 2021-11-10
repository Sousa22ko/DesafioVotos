package com.br.desafioVotos.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CpfApi {
	
	final static String url = "https://user-info.herokuapp.com/users/";

	public CpfApi() {
	} 
	
	public static Boolean consultarCpf(String cpf) throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.getForEntity(url + cpf, String.class);
		
		if(result.getStatusCode().equals(HttpStatus.NOT_FOUND))
			throw new Exception("CPF invalido");
		
		return !result.getBody().contains("UNABLE_TO_VOTE");
	}
	
}
