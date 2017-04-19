package downUnderRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DownUnderInterface extends Remote{
	
	public int registraJogador(String nomeJogador) throws RemoteException;
	
	public int encerraPartida(int idJogador) throws RemoteException;
	
	public int temPartida(int idJogador) throws RemoteException;
	
	public int minhaVez(int idjogador) throws RemoteException;
	
	public String obtemTabuleiro(int idJogador) throws RemoteException;
	
	public int soltaEsfera(int idJogador, int posicao) throws RemoteException;
	
	public String obtemOponente(int idJogador) throws RemoteException;
	
}
