package downUnderRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DownUnderImplementation extends UnicastRemoteObject implements DownUnderInterface{
	
	private static int MAX_PLAYERS = 50;
	
	private int idCount = 10;
	
	private static final long serialVersionUID = 189L;
	
	private ArrayList<Partida> partidas = new ArrayList<>();
	
	private HashMap<String, Jogador> jogadores = new HashMap<String, Jogador>();
	
	public DownUnderImplementation() throws RemoteException {
	}

	@Override
	public int registraJogador(String nomeJogador) throws RemoteException {
		if (jogadores.size() == MAX_PLAYERS)
			return -2;
		
		if (jogadores.get(nomeJogador) != null)
			return -1;
		
		Jogador novo = new Jogador(nomeJogador, idCount);
		
		jogadores.put(nomeJogador, novo);

		return novo.getId();
	}

	@Override
	public int encerraPartida(int idJogador) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int temPartida(int idJogador) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int minhaVez(int idjogador) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String obtemTabuleiro(int idJogador) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int soltaEsfera(int idJogador, int posicao) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String obtemOponente(int idJogador) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
