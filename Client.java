import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.UUID;

public class Client {
	
	private static Scanner scanner;

	public static void main(String[] args) throws InterruptedException {
		try {
			DownUnderInterface game = (DownUnderInterface)Naming.lookup("//localhost/DownUnder");
			
			scanner = new Scanner(System.in);
            String nome;
            
            // teste
            if (args.length > 0 && args[0].equals("t")) {
            	nome = UUID.randomUUID().toString();
            } else {
                System.out.println("Seu nome:");
                nome = scanner.nextLine();
            }
            
            int idJogador = game.registraJogador(nome);
            
			if (idJogador == -1) {
				System.out.println("Usuario ja cadastrado.");
				return;
			}
			
			if (idJogador == -2) {
				System.out.println("Numero maximo de jogadores atingido.");
				return;
			}
				
			System.out.println("Seu id: " + idJogador);
			
			ClientImplementation client = new ClientImplementation(game, idJogador, args.length > 0 && args[0].equals("t"));
			
			client.inicia();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	

}
