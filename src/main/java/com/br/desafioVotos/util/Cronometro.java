package com.br.desafioVotos.util;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Cronometro {

	public Timer timer;
	public Integer pautaID; // id da pauta que esta em votação

	public Cronometro(Integer pautaID, Integer segundos) {
		this.timer = new Timer();
		System.err.println("Iniciar timer para " + pautaID);
		timer.schedule(new Task(), segundos * 1000);
		this.pautaID = pautaID;
	}

	public class Task extends TimerTask {

		@SuppressWarnings("unused")
		@Override
		public void run() {
			if (pautaID != null) { // impede que a thread rode a primeira vez com a pautaID vazia

				RestTemplate restTemplate = new RestTemplate();
				String url = "http://localhost:8080/mesaVotacao/fechar/" + pautaID;
				
				HttpHeaders headers = new HttpHeaders();
			    headers.setContentType(MediaType.APPLICATION_JSON);
				
				ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, headers);
			}

			// finaliza a thread
			System.err.println("Finalizar timer para " + pautaID);
			pautaID = null;
			timer.cancel();
		}
	}

}
