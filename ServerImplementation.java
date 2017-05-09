import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.util.ArrayList;

import com.sun.glass.ui.Timer;

public class ServerImplementation extends UnicastRemoteObject implements DownUnderInterface {
	
	private static int MAX_PARTIDAS = 50;
	
	private int idCount = 100;
	
	private static final long serialVersionUID = 189L;
	
	private ArrayList<Partida> partidas = new ArrayList<>();
	
	public ServerImplementation() throws RemoteException {
		timerPartida();
	}

	@Override
	public int registraJogador(String nomeJogador) throws RemoteException {
		if (partidas.size() == MAX_PARTIDAS)
			return -2;
		
		Partida partidaExistente = buscaPartidaPorJogador(nomeJogador);
		
		if (partidaExistente != null)
			return -1;
		
		Jogador novo = new Jogador(nomeJogador, idCount); 
		
		idCount++;
		
		if(!alocaJogador(novo))
			return -2;

		return novo.getId();
	}

	@Override
	public int encerraPartida(int idJogador) throws RemoteException {
		
		Partida p = buscaPartidaPorJogador(idJogador);
				
		if (p == null)
			return -1;
		
		p.encerraPartida(idJogador);
		
		return 0;
	}

	@Override
	public int temPartida(int idJogador) throws RemoteException {
		try {
			Partida partida = buscaPartidaPorJogador(idJogador);

			if(partida.statusPartida() == StatusPartida.AGUARDANDO)
				return 0;
			
			if (partida.statusPartida() == StatusPartida.TIMEOUT)
				return -2;
			
			if (partida.getJogador1().getId() == idJogador)
				return 1;
			else
				return 2;

		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public int ehMinhaVez(int idjogador) throws RemoteException {
		try {
			Partida partida = buscaPartidaPorJogador(idjogador);

			switch(partida.statusPartida()) {
				case AGUARDANDO :
					return -2;
				case ENCERRADA :
					int res = partida.getResultado(idjogador);
					
					return res;
				case INICIADA:
					if (partida.getJogadorAtual().getId() == idjogador)
						return 1;
					else
						return 0;
				default :
					return -1;
			}

		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public String obtemTabuleiro(int idJogador) throws RemoteException {
		try {
			Partida partida = buscaPartidaPorJogador(idJogador);

			return partida.getTabuleiro();
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public int soltaEsfera(int idJogador, int posicao) throws RemoteException {
		Partida partida = buscaPartidaPorJogador(idJogador);

		if (partida.statusPartida() == StatusPartida.AGUARDANDO)
			return -2;

		if (partida.getJogadorAtual().getId() != idJogador)
			return -3;
		
		if (partida.statusPartida() == StatusPartida.TIMEOUT)
			return 2;
		
		int resultado = partida.realizaJogada(idJogador, posicao);
		
		if (partida.statusPartida() == StatusPartida.ENCERRADA || partida.statusPartida() == StatusPartida.TIMEOUT)
			partida.removeJogadores();
		
		return resultado;
	}

	@Override
	public String obtemOponente(int idJogador) throws RemoteException {
		try {
			Partida partida = buscaPartidaPorJogador(idJogador);

			return partida.getOponente(idJogador).getNome();
		} catch (Exception e) {
			return "";
		}
	}
	
	private synchronized Partida buscaPartidaPorJogador(int id) {
		for (Partida partida : partidas)
			if (partida.verificaJogador(id))
				return partida;
		
		return null;
	}
	
	private synchronized void timerPartida() {
		new Thread() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(60000);
						long now  = System.currentTimeMillis();
						for (int i=0; i<partidas.size(); i++) {
							if (partidas.get(i).statusPartida() == StatusPartida.ENCERRADA || partidas.get(i).statusPartida() == StatusPartida.TIMEOUT)
								if (now - partidas.get(i).getTempoEncerrada() > 60000)
									partidas.remove(i);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private synchronized Partida buscaPartidaPorJogador(String nomeJogador) {
		for (Partida partida : partidas)
			if (partida.verificaJogador(nomeJogador))
				return partida;
		
		return null;
	}

	private synchronized boolean alocaJogador(Jogador jogador) {
		// TODO: ADICIONAR TIMER PARA ESPERA DO SEGUNDO JOGADOR
		Partida partidaDisponivel = null;
		
		for (Partida partida : partidas)
			if (partida.statusPartida() == StatusPartida.AGUARDANDO)
				partidaDisponivel = partida;
		
		if (partidaDisponivel != null)
			return partidaDisponivel.adicionaOponente(jogador);
		
		partidaDisponivel = new Partida(jogador);
		partidas.add(partidaDisponivel);
		
		return true;
			
	}

}

