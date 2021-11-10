package com.br.desafioVotos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.desafioVotos.core.GenericRepository;
import com.br.desafioVotos.model.Voto;

@Repository
public interface VotoRepository extends GenericRepository<Voto> {
	
	@Query("select v from Voto v where v.ativo = true and v.associado.id = ?1")
	public List<Voto> findVotoByAssociadoId(Integer associadoID);
	
	@Query("select v from Voto v where v.ativo = true and v.pauta.id = ?1")
	public List<Voto> findVotoByPautaId(Integer pautaID);

	@Query("select v from Voto v where v.ativo = true and v.associado.id = ?1 and v.pauta.id = ?2")
	public List<Voto> findVotoByPautaAssociado(Integer associadoID , Integer pautaID);

}
