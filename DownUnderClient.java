import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.UUID;

public class DownUnderClient {
	
	public static void main(String[] args) throws InterruptedException {
		try {
			DownUnderInterface game = (DownUnderInterface)Naming.lookup("//localhost/DownUnder");
			
			Scanner scanner = new Scanner(System.in);
            String nome;
            // System.out.println("Seu nome:");
            // nome = scanner.nextLine();
            // int idJogador = game.registraJogador(nome);

			int idJogador = game.registraJogador(UUID.randomUUID().toString());
			
			
			if (idJogador == -1) {
				System.out.println("Usuario ja cadastrado.");
				return;
			}
			
			if (idJogador == -2) {
				System.out.println("Numero maximo de jogadores atingido.");
				return;
			}
				
			System.out.println("Seu id: " + idJogador);
			
			DownUnderClientGame client = new DownUnderClientGame(game, idJogador);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	

}
