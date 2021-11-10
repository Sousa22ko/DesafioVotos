package com.br.desafioVotos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.br.desafioVotos.core.GenericService;
import com.br.desafioVotos.model.Associado;
import com.br.desafioVotos.repository.AssociadoRepository;

@Service
public class AssociadoService extends GenericService<Associado, AssociadoRepository> {
	
	@Override
	public void validate(Associado entity) throws Exception {
		List<Associado> associados = this.repository.findByCpf(entity.getCpf());
		
		if(associados.size() > 0)
			throw new Exception("Associado com cpf ja cadastrado");
	}

}
