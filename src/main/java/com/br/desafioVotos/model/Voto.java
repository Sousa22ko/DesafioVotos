package com.br.desafioVotos.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.br.desafioVotos.core.GenericEntity;

@Table
@Entity
@AttributeOverride(name = "id", column = @Column(name = "votoID"))
public class Voto extends GenericEntity {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "associadoID")
	private Associado associado;

	@OneToOne
	@JoinColumn(name = "pautaID")
	private Pauta pauta;
	
	private Boolean voto;

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

	public Boolean getVoto() {
		return voto;
	}

	public void setVoto(Boolean voto) {
		this.voto = voto;
	}
}
