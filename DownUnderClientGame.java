import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class DownUnderClientGame {
	
	private DownUnderInterface game;
	
	private int idJogador;
	
	private char esfera;

	private Scanner scanner;
	
	public DownUnderClientGame(DownUnderInterface game, int idJogador) throws RemoteException, InterruptedException {
		this.game = game;
		this.idJogador = idJogador;
		esfera = 0;
		executa();
	}
	
	private void executa() throws RemoteException, InterruptedException {
		int temPartida = game.temPartida(idJogador);
		
		if (temPartida == 0)
			System.out.println("Aguardando partida...");

		// Aguarda ate partida iniciar
		while (temPartida == 0) {
			Thread.sleep(4000);
			temPartida = game.temPartida(idJogador);
		}
		
		switch (temPartida) {
			case 1:
				esfera = 'C';
			case 2:
				if (esfera == 0)
					esfera = 'E';
				
				System.out.println("Partida iniciada\n"
						+ "Voce joga com as esferas: " + (esfera == 'C' ? "Claras" : "Escuras") + "(" + esfera + ")\n"
						+ "Seu opente: " + game.obtemOponente(idJogador));
				jogar();
				break;
			case -2 :
				System.out.println("Tempo de espera esgotado.");
				break;
			case -1 :
				System.out.println("Ocorreu um erro ao consultar partida.");
				break;
			default:
				break;
		}

	}
	
	private void jogar() throws RemoteException, InterruptedException {
		int minhaVez = 0;

		while (true) {
			minhaVez = game.minhaVez(idJogador);
			
			switch (minhaVez) {
			case -2:
				System.out.println("Erro: ainda nao ha 2 jogadores registrados na partida.");
				return;
			case -1 :
				System.out.println("Ocorreu um erro ao consultar partida.");
				return;
			case 0 :
				Thread.sleep(2000); // aguarda sua vez
				break;
			case 1 :
				int resultado = realizaJogada();
				
				if (resultado == 2) {
					System.out.println("Partida encerrada, voce demorou muito para jogar.");
					return;
				}
				
				break;
			case 2 :
				System.out.println(game.obtemTabuleiro(idJogador));
				System.out.println("VOCE VENCEU!!!");
				return;
			case 3 :
				System.out.println(game.obtemTabuleiro(idJogador));
				System.out.println("VOCE PERDEU!");
				return;
			case 4 :
				System.out.println(game.obtemTabuleiro(idJogador));
				System.out.println("Ocorreu um empate!");
				return;
			case 5 :
				System.out.println(game.obtemTabuleiro(idJogador));
				System.out.println("VOCE VENCEU POR WO!!!");
				return;
			case 6 :
				System.out.println(game.obtemTabuleiro(idJogador));
				System.out.println("VOCE PERDEU POR WO!");
				return;
			default:
				return;
			}
		}
	}
	
	private int realizaJogada() throws RemoteException {
		String tabuleiro = game.obtemTabuleiro(idJogador);
		
		System.out.println("Tabuleiro: " + tabuleiro);
		
		scanner = new Scanner(System.in);
		
		System.out.print("Orificio para jogada: ");
		
		int orificio = -1;
		
		int resultado = 0;
		
		// TODO: teste mode
		ArrayList<Integer> posicoes = new ArrayList<Integer>(); 
		for (int i=0; i<5; i++)
			if (tabuleiro.charAt(i) == '-')
				posicoes.add(i);
		
		try {		
//			if(scanner.hasNextInt())
//				orificio = scanner.nextInt();
			// TODO: teste
			int randomPos = Math.round((float) Math.random() * posicoes.size());
			orificio = posicoes.get(randomPos);
			System.out.print(orificio);
			
			System.out.println();
			
			resultado = game.soltaEsfera(idJogador, orificio);
			
			while (resultado == 0 || resultado == -1) {
				if (resultado == 0)
					System.out.println("Movimento invalido.");
				if (resultado == -1)
					System.out.println("Orifício invalido(0 < posicao < 4);");
				
				System.out.print("Orificio para jogada: ");
				orificio = scanner.nextInt();
				System.out.println();
				resultado =  game.soltaEsfera(idJogador, orificio);
			}
		} catch (Exception e) {
			System.out.println();
			return 0;
		}
		
		if (resultado == -2)
			System.out.println("Partida nao iniciada, ainda nao ha 2 jogadores.");
		
		if (resultado == -3)
			System.out.println("Nao e sua vez de jogar.");
				
		return resultado;
	}
	
}
