package downUnderRMI;

public class Partida {
	
	private int jogador1, jogador2;
	private Tabuleiro tabuleiro;
	
	public Partida(int jogador1, int jogador2) {
		this.jogador1 = jogador1;
		this.jogador2 = jogador2;
		
		tabuleiro = new Tabuleiro();
	}
}
