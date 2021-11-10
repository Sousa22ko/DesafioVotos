package com.br.desafioVotos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.desafioVotos.core.GenericService;
import com.br.desafioVotos.model.Associado;
import com.br.desafioVotos.model.Pauta;
import com.br.desafioVotos.model.Voto;
import com.br.desafioVotos.repository.VotoRepository;

@Service
public class VotoService extends GenericService<Voto, VotoRepository> {

	@Autowired
	private AssociadoService aService;

	@Autowired
	private PautaService pService;

	@Override
	public void validate(Voto entity) throws Exception {

		// voto ja foi cadastrado
		if (entity.getId() != null && this.repository.findById(entity.getId()) != null)
			throw new Exception("Voto ja cadastrado");

		Associado associado;
		associado = aService.findById(entity.getAssociado().getId()).get();

		Pauta pauta;
		pauta = pService.findById(entity.getPauta().getId()).get();

		if (pauta == null || associado == null) {
			throw new Exception("Pauta ou associado não encontrado");
		}

		List<Pauta> abertas = pService.pautasAbertas();
		if (abertas.size() == 0)
			throw new Exception("A sessão da pauta '" + pauta.getAtaDaPauta() + "' não foi aberta");

		Pauta aberta = abertas.get(0);
		if (pauta.getId() != aberta.getId())
			throw new Exception("A sessão da pauta '" + pauta.getAtaDaPauta() + "' não foi aberta");
		
		List<Voto> voto = this.repository.findVotoByPautaAssociado(associado.getId(), pauta.getId());
		if (voto.size() > 0)
			throw new Exception("Voto do associado " + associado.getCpf() + " ja cadastrado para a pauta '" + pauta.getAtaDaPauta() + "'");
	}

	@Override
	public Voto posSave(Voto entity) throws Exception {
		System.out.println("Voto do associado registrado");
		return super.posSave(entity);
	}

	public Integer contabilizar(Integer pautaID) {
		List<Voto> listVotosDaPauta = this.repository.findVotoByPautaId(pautaID);
		return listVotosDaPauta.size();
	}

}
