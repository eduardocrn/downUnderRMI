package downUnderRMI;

public class Tabuleiro {
	
	private char[][] matrix;
	private int[] ultimasJogadas = new int[]{-1, -1};
	
	public Tabuleiro() {
		matrix = new char[5][8];
	}
	
	public int colocaEsfera(char esfera, int posicao) {
		char[] linha = matrix[posicao];
		
		for(int i=0; i<linha.length; i++)
			if (linha[i] == ' ') {
				linha[i] = esfera;
				ultimasJogadas[0] = ultimasJogadas[1];
				ultimasJogadas[1] = posicao;
				return 1;
			}

		return 0;
	}

	public boolean estaCompleto() {
		for (int i=0; i<5; i++)
			if (matrix[i][7] == ' ')
				return false;

		return true;
	}

	public String estadoAtual() {
		StringBuilder estado = new StringBuilder("");

		if (estaCompleto()) {
			for (int i=0; i<5; i++)
				for (int j=0; j<8; j++)
					estado.append(matrix[i][j]);
		} else {
			for (int i=0; i<5; i++) {
				if (matrix[i][8] == ' ')
					estado.append('-');
				else estado.append(matrix[i][8]);
			}

			estado.append(".....");

			if (ultimasJogadas[0] > -1)
				estado.setCharAt(ultimasJogadas[0], '^');

			if (ultimasJogadas[1] > -1)
				estado.setCharAt(ultimasJogadas[1], '^');
		}

		return estado.toString();
	}
}
