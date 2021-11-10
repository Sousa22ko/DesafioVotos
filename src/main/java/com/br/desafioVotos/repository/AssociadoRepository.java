package com.br.desafioVotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.desafioVotos.core.GenericRepository;
import com.br.desafioVotos.model.Associado;

@Repository
public interface AssociadoRepository extends GenericRepository<Associado>{

	@Query("select a from Associado a where a.ativo = true and a.cpf like %?1%")
	public List<Associado> findByCpf(String cpf);
	
}
