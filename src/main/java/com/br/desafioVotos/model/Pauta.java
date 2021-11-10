package com.br.desafioVotos.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.br.desafioVotos.core.GenericEntity;

@Table
@Entity
@AttributeOverride(name = "id", column = @Column(name = "pautaID"))
public class Pauta extends GenericEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * descrição da pauta
	 */
	private String ataDaPauta;

	private Boolean sessaoAberta = false;

	public Pauta() {
		super();
		this.sessaoAberta = false;
	}

	public String getAtaDaPauta() {
		return ataDaPauta;
	}

	public void setAtaDaPauta(String ataDaPauta) {
		this.ataDaPauta = ataDaPauta;
	}

	public Boolean getSessaoAberta() {
		return sessaoAberta;
	}

	public void setSessaoAberta(Boolean sessaoAberta) {
		this.sessaoAberta = sessaoAberta;
	}
}
