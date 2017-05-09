public class Jogador {
	
	private int id;
	private String nome;
	private boolean ativo;
	
	public Jogador(String nome, int id) {
		this.id = id;
		this.nome = nome;
		ativo = true;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public boolean estaAtivo() {
		return ativo;
	}

	public void desativar() {
		ativo = false;
	}
}
