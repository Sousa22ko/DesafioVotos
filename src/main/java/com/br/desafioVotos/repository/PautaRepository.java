package com.br.desafioVotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.desafioVotos.core.GenericRepository;
import com.br.desafioVotos.model.Pauta;

@Repository
public interface PautaRepository extends GenericRepository<Pauta>{

	@Query("select p from Pauta p where p.ativo = true and p.sessaoAberta = true")
	public List<Pauta> findPautaSessaoAtiva();

}
