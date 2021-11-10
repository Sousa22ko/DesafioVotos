package com.br.desafioVotos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path = "/mesaVotacao")//, consumes = "application/json")
public class MainController {

	@Autowired
	private PautaService pService;

	@Autowired
	private VotoService vService;
	
	@Autowired
	private AssociadoService aService;

	@PostMapping("/novaPauta")
	@ResponseBody
	public Pauta novaPauta(@RequestBody Pauta pauta) {
		try {
			return this.pService.save(pauta);
		} catch (Exception e) {
			System.err.println("Não foi possivel abrir a pauta");
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	@PostMapping("/novoAssociado")
	@ResponseBody
	public Associado novoAssociado(@RequestBody Associado associado) {
		try {
			return this.aService.save(associado);
		} catch (Exception e) {
			System.err.println("Não foi possivel cadastrar o associado");
			System.err.println(e.getMessage());
			return null;
		}
	}

	@GetMapping(value = { "/abrirSessao/{pautaID}/{tempoSessao}", "/abrirSessao/{pautaID}" })
	@ResponseBody
	public String abrirSessaoParaVoto(@PathVariable Integer pautaID, @PathVariable Integer tempoSessao) {
		if (tempoSessao == null || tempoSessao <= 0)
			tempoSessao = 60;

		// Procurar a pauta
		Pauta p = pService.findById(pautaID).get();
		if (p != null) {
			try {
				pService.updateSessao(pautaID, true);
				new Cronometro(p.getId(), tempoSessao);
				return "Sessão aberta para votação";
			} catch(Exception e) {
				return e.getMessage();
			}
		} else
			return "Pauta não encontrada";
	}

	@GetMapping("/fechar/{pautaID}")
	@ResponseBody
	public String fecharSessaoParaVoto(@PathVariable Integer pautaID) throws Exception {

		// Procurar a pauta
		Pauta p = pService.findById(pautaID).get();
		if (p != null) {
			pService.updateSessao(pautaID, false);
			return "Sessão finalizada";
		} else
			return "Pauta não encontrada";
	}

	@PostMapping("/votar")
	public String receberVotosAcossiados(@RequestBody List<Voto> listAssociados) {
		List<Voto> salvos = new ArrayList<Voto>();
		for (Voto novo : listAssociados)
			try {
				Voto temp = this.vService.save(novo);
				if(temp != null)
					salvos.add(temp);
			} catch (Exception e) {
				System.err.println("Não foi possivel votar");
				System.err.println(e.getMessage());
				return "Não foi possivel votar. " + e.getMessage();
			}

		return salvos.size() + " votos adicionados com sucesso";
	}

	@GetMapping("/contabilizar/{pautaID}")
	public String contabilizar(@PathVariable Integer pautaID) {
		return vService.contabilizar(pautaID) + " votos contabilizados na pauta";
	}

}
