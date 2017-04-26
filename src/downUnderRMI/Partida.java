package downUnderRMI;

public class Partida {
	
	private Jogador jogador1, jogador2;
	private Tabuleiro tabuleiro;
	
	public Partida(Jogador jogador1) {
		this.jogador1 = jogador1;
		
		tabuleiro = new Tabuleiro();
	}
	
	public boolean addJogador(Jogador jogador2) {
		if (this.jogador2 != null)
			return false;
		
		this.jogador2 = jogador2;
		return true;
	}
	
	public boolean existeJogador(String nomeJogador) {
		return jogador1.getNome().toLowerCase() == nomeJogador.toLowerCase() ||
				jogador2.getNome().toLowerCase() == nomeJogador.toLowerCase();
	}
	
	public boolean existeJogador(int id) {
		return jogador1.getId() == id || jogador2.getId() == id;
	}
	
	public boolean partidaFechada() {
		return jogador1 != null && jogador2 != null;
	}
}
