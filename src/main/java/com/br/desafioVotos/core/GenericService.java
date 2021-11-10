package com.br.desafioVotos.core;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author manom
 *
 * @param <T> tipo da entidade trabalhada pelo service
 */
public class GenericService<T extends GenericEntity, R extends GenericRepository<T>> {

	@Autowired
	protected R repository;

	public GenericService() {
	}

	/**
	 * Metodo para implementar regras de negocio relacionadas a validacao dos
	 * métodos save e update
	 *
	 * @param entity
	 * @throws Exception
	 */
	public void validate(T entity) throws Exception {
	}

	/**
	 * Metodo para implementar regras de negocio relacionadas a validacao no método
	 * save
	 *
	 * @param entity
	 * @throws Exception
	 */
	public void validateOnSave(T entity) throws Exception {
	}

	/**
	 * Metodo para implementar regras de negocio relacionadas a validacao no método
	 * update
	 *
	 * @param entity
	 * @throws Exception
	 */
	public void validateOnUpdate(T entity) throws Exception {
	}

	/**
	 * Método para implementar regras de negócio relacionadas à validacao no método
	 * delete
	 *
	 * @param entity
	 * @throws Exception
	 */
	public void validateOnDelete(T entity) throws Exception {
	}

	/**
	 * Realiza um pré-processamento antes de salvar ou atualizar a entidade
	 *
	 * @param entity
	 * @throws Exception
	 */
	public T preSaveOrUpdate(T entity) throws Exception {
		return entity;
	}

	/**
	 * Realiza um pré-processamento antes de salvar a entidade
	 *
	 * @param entity
	 * @throws Exception
	 */
	public T preSave(T entity) throws Exception {
		return entity;
	}

	/**
	 * Realiza um pré-processamento antes de atualizar a entidade
	 *
	 * @param entity
	 * @throws Exception
	 */
	public T preUpdate(T entity) throws Exception {
		return entity;
	}

	/**
	 * Realiza um processamento após salvar a entidade
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public T posSave(T entity) throws Exception {
		return entity;
	}

	/**
	 * Realiza um processamento após atualizar a entidade
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public T posUpdate(T entity) throws Exception {
		return entity;
	}

	/**
	 * Realiza um processamento após salvar ou atualizar a entidade
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public T posSaveOrUpdate(T entity) throws Exception {
		return entity;
	}

	/**
	 * Salva ou atualiza uma entidade
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public T saveOrUpdate(T entity) throws Exception {
		return entity.getId() == null ? save(entity) : update(entity);
	}

	/**
	 * Persiste uma entidade no banco de dados.
	 * 
	 * @param obj Objeto a ser persistido.
	 * @throws Exception
	 */
	public T save(T obj) throws Exception {
		T entity = this.preSaveOrUpdate(obj);
		entity = this.preSave(entity);
		this.validate(entity);
		this.validateOnSave(entity);
		entity = repository.save(obj);
		entity = this.posSave(entity);
		entity = this.posSaveOrUpdate(entity);
		return entity;
	}

	/**
	 * Altera uma entidade já existente no banco de dados.
	 * 
	 * @param obj Objeto a ser alterado.\
	 * @throws Exception
	 */
	public T update(T obj) throws Exception {
		T entity = this.preSaveOrUpdate(obj);
		entity = this.preUpdate(entity);
		this.validate(entity);
		this.validateOnUpdate(entity);
		entity = repository.save(entity);
		entity = this.posUpdate(entity);
		entity = this.posSaveOrUpdate(entity);
		return entity;
	}

	/**
	 * Remove uma entidade no banco de dados.
	 * 
	 * @param obj Objeto a ser removido.
	 * @throws Exception
	 */
	public void delete(T obj) throws Exception {
		this.validateOnDelete(obj);
		obj.setAtivo(false);
		@SuppressWarnings("unused")
		T entity = this.repository.findById(obj.getId()).get();
		repository.save(obj);
	}

	/**
	 * Remove uma entidade no banco de dados a partir do seu ID.
	 * 
	 * @param id ID da entidade a ser removida.
	 * @throws Exception
	 */
	public void deleteById(Integer id) throws Exception {
		this.validateOnDelete(repository.findById(id).get());
		repository.deleteById(id);
	}

	public void softDelete(T entity) throws Exception {
		this.validateOnDelete(entity);
		repository.delete(entity);
	}

	/**
	 * Encontra uma entidade no banco de dados a partir do seu ID.
	 * 
	 * @param id ID da entidade a ser procurada.
	 * @return Objeto referente a entidade.
	 */
	public Optional<T> findById(Integer id) {
		return repository.findById(id);
	}

	/**
	 * Encontra todos os objetos no banco de dados referentes a uma entidade.
	 * 
	 * @return Lista contendo todas as entidades.
	 */
	public List<T> findAll() {
		return repository.findAll();
	}

	/**
	 * Conta quantas entradas no banco de dados ainda estão ativas (em uso).
	 * 
	 * @return Número de entidades ativas.
	 */
	public long countAtivo() {
		return repository.count();
	}
}
