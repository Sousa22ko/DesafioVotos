package com.br.desafioVotos.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author manom
 *
 * @param <T> tipo da entidade que vai ser trabalhada
 * @param <S> Service da entidade
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class GenericController<T extends GenericEntity, S extends GenericService> {

	/**
	 * Service injetado a partir do tipo genérico.
	 */
	@Autowired
	protected S service;

	@PostMapping
	@ResponseBody
	public T salvar(@RequestBody T entidade) throws Exception {
		return (T) this.service.save(entidade);
	}

	@DeleteMapping
	public void remover(@PathVariable("id") Integer id) throws Exception {
		this.service.deleteById(id);
	}

	@PutMapping("/edit")
	@ResponseBody
	public T editar(@RequestBody T entidade) throws Exception {
		return (T) this.service.update(entidade);
	}

}
