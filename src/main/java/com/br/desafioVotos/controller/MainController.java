package com.br.desafioVotos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.desafioVotos.model.Associado;
import com.br.desafioVotos.model.Pauta;
import com.br.desafioVotos.model.Voto;
import com.br.desafioVotos.service.AssociadoService;
import com.br.desafioVotos.service.PautaService;
import com.br.desafioVotos.service.VotoService;
import com.br.desafioVotos.util.Cronometro;

@RestController
@RequestMapping(path = "/mesaVotacao")
public class MainController {

	@Autowired
	private PautaService pService;

	@Autowired
	private VotoService vService;
	
	@Autowired
	private AssociadoService aService;

	@PostMapping("/novaPauta")
	@ResponseBody
	public ResponseEntity<Object> novaPauta(@RequestBody Pauta pauta) {
		try {
			return new ResponseEntity<Object>(this.pService.save(pauta), HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Não foi possivel abrir a pauta");
			System.err.println(e.getMessage());
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/novoAssociado")
	@ResponseBody
	public ResponseEntity<Object> novoAssociado(@RequestBody Associado associado) {
		try {
			return new ResponseEntity<Object>(this.aService.save(associado), HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Não foi possivel cadastrar o associado");
			System.err.println(e.getMessage());
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = { "/abrirSessao/{pautaID}/{tempoSessao}", "/abrirSessao/{pautaID}" })
	@ResponseBody
	public ResponseEntity<Object> abrirSessaoParaVoto(@PathVariable Integer pautaID, @PathVariable Integer tempoSessao) {
		if (tempoSessao == null || tempoSessao <= 0)
			tempoSessao = 60;

		// Procurar a pauta
		Pauta p = pService.findById(pautaID).get();
		if (p != null) {
			try {
				pService.updateSessao(pautaID, true);
				new Cronometro(p.getId(), tempoSessao);
				return new ResponseEntity<Object>("Sessão aberta para votação", HttpStatus.OK);
			} catch(Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else
			return new ResponseEntity<Object>("Pauta não encontrada", HttpStatus.OK);
	}

	@GetMapping("/fechar/{pautaID}")
	@ResponseBody
	public ResponseEntity<Object> fecharSessaoParaVoto(@PathVariable Integer pautaID) throws Exception {

		// Procurar a pauta
		Pauta p = pService.findById(pautaID).get();
		if (p != null) {
			pService.updateSessao(pautaID, false);
			return new ResponseEntity<Object>("Sessão finalizada", HttpStatus.OK);
		} else
			return new ResponseEntity<Object>("Pauta não encontrada", HttpStatus.OK);
	}

	@PostMapping("/votar")
	public ResponseEntity<Object> receberVotosAcossiados(@RequestBody List<Voto> listAssociados) {
		List<Voto> salvos = new ArrayList<Voto>();
		int problemas = 0;
		
		for (Voto novo : listAssociados) {
			try {
				Voto temp = this.vService.save(novo);
				if(temp != null)
					salvos.add(temp);
			} catch (Exception e) {
				System.err.println("Não foi possivel votar");
				System.err.println(e.getMessage());
				problemas++;
				if(e.getMessage().contains("A sessão da pauta '")) {
					return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		String response = salvos.size() + " votos adicionados com sucesso" + (problemas > 0 ? (" e " + problemas + " itens não foram salvos"): "");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping("/contabilizar/{pautaID}")
	public ResponseEntity<Object> contabilizar(@PathVariable Integer pautaID) {
		return new ResponseEntity<Object>(vService.contabilizar(pautaID) + " votos contabilizados na pauta", HttpStatus.OK);
	}

}
