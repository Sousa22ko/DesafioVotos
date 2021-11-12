# DesafioVotos

Para executar o projeto basta rodar a classe DesafioVotosApplication.java localizado em ```/src/main/java/com/br/desafioVotos/DesafioVotosApplication.java```

##### endpoints

- "/mesaVotacao/novaPauta"
Salvar uma nova pauta passada pelo body

- "/mesaVotacao/novoAssociado"
Salvar um novo associado passada pelo body

- "/mesaVotacao/abrirSessao/{pautaID}/{tempoSessao}" ou "/mesaVotacao/abrirSessao/{pautaID}" (para o default de 60s)
Abre a sessão para votação

- "/mesaVotacao/fechar/{pautaID}"
fecha a sessão para votação

- "/mesaVotacao/votar"
Processa e adiciona os votos (passados pelo body) ao banco de dados
formato dos votos:
```json
[
  {
    "associado": {
      "id":1
    },
    "pauta":{
      "id": 1
    },
    "voto": true
  } 
]
```

- "/mesaVotacao/contabilizar/{pautaID}"
Contabiliza os votos de uma pauta
