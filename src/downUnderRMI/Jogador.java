package downUnderRMI;

public class Jogador {
	
	private int id;
	
	private String nome;
	
	public Jogador(String nome, int id) {
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
}
