import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientImplementation {
	
	private DownUnderInterface game;
	
	private int idJogador;
	
	private char esfera;

	private Scanner scanner;
	
	private boolean isTest;
	
	public ClientImplementation(DownUnderInterface game, int idJogador, boolean isTest) throws RemoteException, InterruptedException {
		this.game = game;
		this.idJogador = idJogador;
		esfera = 0;
		this.isTest = isTest;
	}
	
	public void inicia() throws RemoteException, InterruptedException {
		int temPartida = game.temPartida(idJogador);
		
		if (temPartida == 0)
			System.out.println("Aguardando partida...");

		// Aguarda ate partida iniciar
		while (temPartida == 0) {
			Thread.sleep(3000);
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
			minhaVez = game.ehMinhaVez(idJogador);
			
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
				
				if (resultado == 2)
					System.out.println("Partida encerrada, voce demorou muito para jogar.");
				
				if (resultado == -3)
					System.out.println("Partida encerrada.");
				
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
		
		String opcao =  "";
		
		int orificio = -1;
		
		int resultado = 0;
		
		try {			
			//  teste mode
			if (isTest) {
				ArrayList<Integer> posicoes = new ArrayList<Integer>(); 
				for (int i=0; i<5; i++)
					if (tabuleiro.charAt(i) == '-')
						posicoes.add(i);
				int randomPos = Math.round((float) Math.random() * posicoes.size());
				orificio = posicoes.get(randomPos);
				resultado =  game.soltaEsfera(idJogador, orificio);
				System.out.println("Orificio: " + orificio);
			}
			
			while (resultado == 0 || resultado == -1) {
				System.out.print("Orificio para jogada(digite 'exit' se deseja abandonar partida): ");
				
				opcao = scanner.nextLine();
				
				if (opcao.toLowerCase().equals("exit")) {
					game.encerraPartida(idJogador);
					return -3;
				}
					
				orificio = Integer.parseInt(opcao);
				
				System.out.println();
				
				resultado =  game.soltaEsfera(idJogador, orificio);
				
				if (resultado == 0)
					System.out.println("Movimento invalido.");
				if (resultado == -1)
					System.out.println("Orifício invalido(0 < posicao < 4);");
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
