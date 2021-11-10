package com.br.desafioVotos.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.br.desafioVotos.core.GenericEntity;

@Table
@Entity
@AttributeOverride(name = "id", column = @Column(name = "associadoID"))
public class Associado extends GenericEntity {

	private static final long serialVersionUID = 1L;

	private String cpf;

	public Associado() {
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
