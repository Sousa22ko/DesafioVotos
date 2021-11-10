package com.br.desafioVotos.core;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T extends GenericEntity> extends JpaRepository<T, Integer>{

	@Override
	@Query(value = "select e from #{#entityName} e where e.ativo = true")
	List<T> findAll();
	
	@Override
	@Query(value = "select e from #{#entityName} e where e.id = ?1 and e.ativo = true")
	Optional<T> findById(Integer id);
	
	@Query(value="select count(e) from #{#entityName} e where e.ativo = true")
	long countActive();
	
	@Query(value = "select count(e) from #{#entityName} e where e.ativo = false")
	long countInactive();
	
	@Override
	@Transactional
	default void deleteById(Integer long1) {
		Optional<T> entity = findById(long1);
		entity.get().setAtivo(false);
		save(entity.get());
	}	

	@Override
	@Transactional
	default void delete(T obj) {
		obj.setAtivo(false);
		save(obj);
	}
	
	@Override
	default void deleteAll(Iterable<? extends T> arg0) {
		arg0.forEach(entity -> {
			deleteById(entity.getId());
		});
	}
	
	
}
