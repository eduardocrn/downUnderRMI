package downUnderRMI;

public class Tabuleiro {
	
	private char[][] matrix;
	
	public Tabuleiro() {
		matrix = new char[5][8];
	}
	
	public boolean colocaEsfera(char esfera, int posicao) {
		char[] linha = matrix[posicao];
		
		for(int i=0; i<linha.length;i++)
			if (linha[i] == ' ') {
				linha[i] = esfera;
				return true;
			}
				
		return false;
	}
}
