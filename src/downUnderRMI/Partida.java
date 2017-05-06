package downUnderRMI;

public class Partida {
	
	private Jogador[] jogadores;
	private Tabuleiro tabuleiro;
	private StatusPartida status;
	int jogadorAtual = -1;
	int tipoVitoria = 0;

	
	public Partida(Jogador jogador1) {
		jogadores[0] = jogador1;
		status = StatusPartida.AGUARDANDO;

		tabuleiro = new Tabuleiro();
	}

	public Jogador getJogadorAtual() {
		if (jogadorAtual == -1)
			return null;

		return jogadores[jogadorAtual];
	}

	public int getVencedor() {
		// TODO: retorna vencedor
		return 0;
	}

	public int tipoVitoria() {
		// TODO: retorna tipo da vitoria WO ou normal
		return tipoVitoria;
	}

	public StatusPartida statusPartida() {
		return status;
	}
	
	public boolean verificaJogador(int id) {
		for (int i=0; i<2; i++)
			if (jogadores[i] != null && jogadores[i].getId() == id)
				return jogadores[i].estaAtivo();

		return false;
	}

	public boolean verificaJogador(String nomeJogador) {
		for (int i=0; i<2; i++)
			if (jogadores[i] != null && jogadores[i].getNome() == nomeJogador)
				return jogadores[i].estaAtivo();

		return false;
	}

	public Jogador getJogador1() {
		return jogadores[0];
	}

	public Jogador getJogador2() {
		return jogadores[1];
	}

	public boolean adicionaOponente(Jogador jogador) {
		if (jogadores[1] != null)
			return false;

		jogadores[1] = jogador;

		status = StatusPartida.INICIADA;

		return true;
	}

	public String getOponente(int idJogador) {
		if (jogadores[0].getId() == idJogador)
			return jogadores[1].getNome();
		return jogadores[0].getNome();
	}

	public boolean removeJogador(int idJogador) {
		status = StatusPartida.ENCERRADA;

		for(int i=0; i<2; i++)
			if (jogadores[i] != null)
				jogadores[i] = null;

		if (jogadores[0] == null && jogadores[1] == null)
			return true;

		return false;
	}

	public String getTabuleiro() {
		return tabuleiro.estadoAtual();
	}

	public int realizaJogada(int idJogador, int posicao) {
		if (posicao < 0 || posicao > 4)
			return -1;

		char esfera = 'C';

		if (jogadores[1].getId() == idJogador)
			esfera = 'E';

		int resultado = tabuleiro.colocaEsfera(esfera, posicao);

		if (resultado == 1)
			jogadorAtual = jogadores[0].getId() == idJogador ? 1 : 0;

		if (tabuleiro.estaCompleto())
			status = StatusPartida.ENCERRADA;
		
		return resultado;
	}
}
