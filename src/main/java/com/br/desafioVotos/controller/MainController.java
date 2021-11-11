package com.br.desafioVotos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.desafioVotos.model.Associado;
import com.br.desafioVotos.model.Pauta;
import com.br.desafioVotos.model.Voto;
import com.br.desafioVotos.service.AssociadoService;
import com.br.desafioVotos.service.PautaService;
import com.br.desafioVotos.service.VotoService;
import com.br.desafioVotos.util.Cronometro;

/**
 * 
 * 	Controlador principal da aplicação. Recebe as requisições e processa os dados.
 * 
 * @author manom
 *
 */
@RestController
@RequestMapping(path = "/mesaVotacao")
public class MainController {

	@Autowired
	private PautaService pService;

	@Autowired
	private VotoService vService;
	
	@Autowired
	private AssociadoService aService;

	/**
	 * Registra uma nova pauta no banco de dados
	 * @param pauta 
	 * @return pauta salva no banco
	 */
	@PostMapping("/novaPauta")
	@ResponseBody
	public ResponseEntity<Object> novaPauta(@RequestBody Pauta pauta) {
		try {
			return new ResponseEntity<Object>(this.pService.save(pauta), HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Não foi possivel abrir a pauta");
			System.err.println(e.getMessage());
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Registra um novo associado no banco de dados. O associado precisa ter um CPF valido
	 * @param associado
	 * @return associado salvo no banco
	 */
	@PostMapping("/novoAssociado")
	@ResponseBody
	public ResponseEntity<Object> novoAssociado(@RequestBody Associado associado) {
		try {
			return new ResponseEntity<Object>(this.aService.save(associado), HttpStatus.OK);
		} catch (Exception e) {
			System.err.println("Não foi possivel cadastrar o associado");
			System.err.println(e.getMessage());
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Abre a sessão para votação. é necessario passar como parametro o ID da pauta e opcionalmente o tempo de sessão. por padrão o tempo é de 60s.
	 * Não é possivel ter mais de 1 sessão aberta ao mesmo tempo, sendo nescessário fechar a atual para abrir uma nova.
	 * @param pautaID
	 * @param tempoSessao
	 * @return Mensagem especificando se foi possivel ou não abrir a sessão
	 */
	@GetMapping(value = { "/abrirSessao/{pautaID}/{tempoSessao}", "/abrirSessao/{pautaID}" })
	@ResponseBody
	public ResponseEntity<Object> abrirSessaoParaVoto(@PathVariable Integer pautaID, @PathVariable Optional<Integer> tempoSessao) {

		// Procurar a pauta
		Pauta p = pService.findById(pautaID).get();
		if (p != null) {
			try {
				pService.updateSessao(pautaID, true);
				// ativa o timer da sessão
				new Cronometro(p.getId(), tempoSessao.isPresent() ? tempoSessao.get(): 60);
				return new ResponseEntity<Object>("Sessão aberta para votação", HttpStatus.OK);
			} catch(Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else
			return new ResponseEntity<Object>("Pauta não encontrada", HttpStatus.OK);
	}

	/**
	 * Fecha a sessão para novos votos
	 * @param pautaID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/fechar/{pautaID}")
	@ResponseBody
	public ResponseEntity<Object> fecharSessaoParaVoto(@PathVariable Integer pautaID) throws Exception {

		// Procurar a pauta
		Pauta p = pService.findById(pautaID).get();
		if (p != null) {
			pService.updateSessao(pautaID, false);
			System.err.println("finalizar sessão "+ pautaID);
			return new ResponseEntity<Object>("Sessão finalizada", HttpStatus.OK);
		} else
			return new ResponseEntity<Object>("Pauta não encontrada", HttpStatus.OK);
	}

	/**
	 * Recebe uma lista de votos dos associados e salva os no banco de dados.
	 * Votos repetidos (mesmo associado na mesma pauta) será ignorado
 	 * @param listAssociados
	 * @return Uma mensagem com quantos votos foram salvos e quantos não foram caso possuam algum problema
	 */
	@PostMapping("/votar")
	public ResponseEntity<Object> receberVotosAcossiados(@RequestBody List<Voto> listAssociados) {
		List<Voto> salvos = new ArrayList<Voto>();
		int problemas = 0;
		
		// salva individualmente cada voto
		for (Voto novo : listAssociados) {
			try {
				Voto temp = this.vService.save(novo);
				if(temp != null)
					salvos.add(temp);
			} catch (Exception e) {
				System.err.println("Não foi possivel votar");
				System.err.println(e.getMessage());
				problemas++;
				if(e.getMessage().contains("A sessão da pauta '")) {
					return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		String response = salvos.size() + " votos adicionados com sucesso" + (problemas > 0 ? (" e " + problemas + " itens não foram salvos"): "");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/**
	 * Contabiliza os votos recebidos em uma determinada pauta
	 * @param pautaID
	 * @return
	 */
	@GetMapping("/contabilizar/{pautaID}")
	public ResponseEntity<Object> contabilizar(@PathVariable Integer pautaID) {
		List<Voto> votos = vService.findByPautaID(pautaID);
		
		// filtra os votos positivos
		List<Voto> positivo = votos.stream().filter(voto -> voto.getVoto() == true).collect(Collectors.toList());
		//filtra os votos negativos
		List<Voto> negativos = votos.stream().filter(voto -> voto.getVoto() == false).collect(Collectors.toList());
		
		
		String response = vService.contabilizar(pautaID) + " votos contabilizados na pauta. "+ positivo.size() +" votos a favor e "+ negativos.size()+ " votos contra";
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

}
