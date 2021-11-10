package com.br.desafioVotos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.br.desafioVotos.core.GenericService;
import com.br.desafioVotos.model.Pauta;
import com.br.desafioVotos.repository.PautaRepository;

@Service
public class PautaService extends GenericService<Pauta, PautaRepository> {

	// seta o estado da sessão
	public Pauta updateSessao(Integer pautaID, Boolean sessao) throws Exception {
		Pauta p = this.repository.findById(pautaID).get();
		if (p != null) {
			if (sessao && this.pautasAbertas().size() > 0)
				throw new Exception("Ja existe uma pauta aberta");

			p.setSessaoAberta(sessao);
			return this.update(p);
		} else {
			throw new Exception("Pauta não encontrada");
		}
	}

	public List<Pauta> pautasAbertas() {
		// o ideal é retornar vazio;
		return this.repository.findAll().stream().filter(pauta -> pauta.getSessaoAberta() == true)
				.collect(Collectors.toList());
	}

	public Boolean verificarSessao(Integer pautaID) {
		return findById(pautaID).get().getSessaoAberta();
	}

}
