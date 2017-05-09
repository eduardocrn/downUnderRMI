public class Partida {

	private Jogador[] jogadores = new Jogador[2];
	private Jogador vencedor;
	private Tabuleiro tabuleiro;
	private StatusPartida status;
	private int jogadorAtual = -1;
	private int countJogadas = 0;
	private String tipoVitoria = "NORMAL";
	private Thread threadJogada;
	private long tempoEncerrada;

	public Partida(Jogador jogador1) {
		jogadores[0] = jogador1;
		status = StatusPartida.AGUARDANDO;

		tabuleiro = new Tabuleiro();
		timerSegundoJogador();
	}

	public Jogador getJogadorAtual() {
		if (jogadorAtual == -1)
			return null;

		return jogadores[jogadorAtual];
	}

	public int getResultado(int idJogador) {
		if (status != StatusPartida.ENCERRADA)
			return -1; // erro

		if (vencedor == null) // empate
			return 4;

		if (vencedor.getId() == idJogador) { // ganhou
			if (tipoVitoria == "NORMAL")
				return 2;
			return 5;
		} else {
			if (tipoVitoria == "NORMAL") // perdeu
				return 3;
			return 6;
		}
	}
	
	public long getTempoEncerrada() {
		return tempoEncerrada;
	}

	public StatusPartida statusPartida() {
		return status;
	}

	public boolean verificaJogador(int id) {
		for (int i = 0; i < 2; i++)
			if (jogadores[i] != null && jogadores[i].getId() == id)
				return true;

		return false;
	}

	public boolean verificaJogador(String nomeJogador) {
		for (int i = 0; i < 2; i++)
			if (jogadores[i] != null && jogadores[i].getNome().toLowerCase().equals(nomeJogador.toLowerCase()))
				return jogadores[i].estaAtivo();

		return false;
	}

	public Jogador getJogador1() {
		return jogadores[0];
	}

	public Jogador getJogador2() {
		return jogadores[1];
	}

	public boolean adicionaOponente(Jogador jogador) {
		if (jogadores[1] != null)
			return false;

		jogadores[1] = jogador;

		status = StatusPartida.INICIADA;

		jogadorAtual = 0;

		return true;
	}

	public Jogador getOponente(int idJogador) {
		if (jogadores[0].getId() == idJogador)
			return jogadores[1];
		return jogadores[0];
	}

	public boolean removeJogador(int idJogador) {
		if (status == StatusPartida.INICIADA)
			vencedor = getOponente(idJogador);

		tipoVitoria = "WO";

		status = StatusPartida.ENCERRADA;
		
		tempoEncerrada = System.currentTimeMillis();

		removeJogadores();

		return true;
	}

	public void removeJogadores() {
		jogadores[0].desativar();

		if (jogadores[1] != null)
			jogadores[1].desativar();
	}

	public String getTabuleiro() {
		return tabuleiro.estadoAtual();
	}

	public int realizaJogada(int idJogador, int posicao) {
		if (posicao < 0 || posicao > 4)
			return -1;

		char esfera = 'C';

		if (jogadores[1].getId() == idJogador)
			esfera = 'E';

		if (threadJogada != null)
			threadJogada.interrupt();

		threadJogada = timerJogada();

		int resultado = tabuleiro.colocaEsfera(esfera, posicao);

		if (resultado == 1) {
			countJogadas++;
			if (countJogadas == 2) {
				jogadorAtual = jogadores[0].getId() == idJogador ? 1 : 0;
				countJogadas = 0;
			}
		}
		
		if (tabuleiro.estaCompleto()) {
			calculaPontuacao();
			status = StatusPartida.ENCERRADA;
			tempoEncerrada = System.currentTimeMillis();
		}

		return resultado;
	}

	public synchronized void encerraPartida(int idJogador) {
		removeJogadores();

		if (status == StatusPartida.INICIADA)
			tipoVitoria = "WO";

		status = StatusPartida.ENCERRADA;
		tempoEncerrada = System.currentTimeMillis();
	}

	private Thread timerJogada() {

		Thread t = new Thread() {
			public void run() {
				try {
					Thread.sleep(60000);

					if (status == StatusPartida.INICIADA)
						tipoVitoria = "WO_INTERROMPIDA";

					status = StatusPartida.ENCERRADA;

				} catch (InterruptedException e) {
					if (tipoVitoria == "WO_INTERROMPIDA") {
						tipoVitoria = "NORMAL";
						status = StatusPartida.INICIADA;
					}
				}

			}
		};

		t.start();

		return t;
	}

	private void timerSegundoJogador() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(120000);

					if (status == StatusPartida.AGUARDANDO) {
						jogadores[0].desativar();
						status = StatusPartida.TIMEOUT;
						tempoEncerrada = System.currentTimeMillis();
					}

				} catch (InterruptedException e) {
				}
			}
		}.start();
	}
	
	private void calculaPontuacao() {
		char board[][] = tabuleiro.getTabuleiro();
		
		int score[] = new int[]{0, 0}; // C, E
		
		int count[] = new int[]{0, 0}; // C, E
		
		// linha
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 8 ; col ++) {
				if (board[row][col] == 'C') {
					count[0]++;
					
					if (count[1] >= 4)
						score[1] += count[1]-3;
					
					count[1] = 0;
				}
				else if (board[row][col] == 'E') {
					count[1]++;

					if (count[0] >= 4)
						score[0] += count[0]-3;
					
					count[0] = 0;
				}
			}
			
			if (count[0] >= 4)
				score[0] += count[0]-3;
			if (count[1] >= 4)
				score[1] += count[1]-3;

			count = new int[]{0, 0};
		}
		
		count = new int[]{0, 0};
		
		// coluna
		for (int col = 0; col < 8 ; col ++) {
			for (int row = 0; row < 5; row++) {
				if (board[row][col] == 'C') {
					count[0]++;
					
					if (count[1] >= 4)
						score[1] += count[1]-3;
					
					count[1] = 0;
				}
				else if (board[row][col] == 'E') {
					count[1]++;

					if (count[0] >= 4)
						score[0] += count[0]-3;
					
					count[0] = 0;
				}
			}
			
			if (count[0] >= 4)
				score[0] += count[0]-3;
			if (count[1] >= 4)
				score[1] += count[1]-3;

			count = new int[]{0, 0};
		}
		
		count = new int[]{0, 0};
		
		// diagonal principal       
        for(int col = 7; col>= 0; col--){
            for(int row = 0; row< 5; row++){
                if((col + row) < 8){
                	if (board[row][col + row] == 'C') {
    					count[0]++;
    					
    					if (count[1] >= 4)
    						score[1] += count[1]-3;
    					
    					count[1] = 0;
    				}
    				else if (board[row][col + row] == 'E') {
    					count[1]++;

    					if (count[0] >= 4)
    						score[0] += count[0]-3;
    					
    					count[0] = 0;
    				}
                } else {
                    break;
                }
            }

			if (count[0] >= 4)
				score[0] += count[0]-3;
			if (count[1] >= 4)
				score[1] += count[1]-3;

			count = new int[]{0, 0};
        }
        count = new int[]{0, 0};
        for(int i = 1; i < 5; i++){
            for(int row = i, col = 0; row < 5 && col < 8; row++, col++){
            	if (board[row][col] == 'C') {
					count[0]++;
					
					if (count[1] >= 4)
						score[1] += count[1]-3;
					
					count[1] = 0;
				}
				else if (board[row][col] == 'E') {
					count[1]++;

					if (count[0] >= 4)
						score[0] += count[0]-3;
					
					count[0] = 0;
				}
            }
        }
        
        count = new int[]{0, 0};
        
        // diagonal secundaria
		for(int col = 7; col >= 0; col--) {
			for(int row = 0; row < 5; row++) {
                if((col - row) < 8 && (col - row) >= 0) {
                	if (board[row][col - row] == 'C') {
    					count[0]++;
    					
    					if (count[1] >= 4)
    						score[1] += count[1]-3;
    					
    					count[1] = 0;
    				}
    				else if (board[row][col - row] == 'E') {
    					count[1]++;

    					if (count[0] >= 4)
    						score[0] += count[0]-3;
    					
    					count[0] = 0;
    				}
                } else {
                    break;
                }
            }

			if (count[0] >= 4)
				score[0] += count[0]-3;
			if (count[1] >= 4)
				score[1] += count[1]-3;

			count = new int[]{0, 0};
        }
		count = new int[]{0, 0};
        for(int i = 1; i < 5; i++){
            for(int row = i, col = 7; row < 5 && col >= 0; row++, col--){
            	if (board[row][col] == 'C') {
					count[0]++;
					
					if (count[1] >= 4)
						score[1] += count[1]-3;
					
					count[1] = 0;
				}
				else if (board[row][col] == 'E') {
					count[1]++;

					if (count[0] >= 4)
						score[0] += count[0]-3;
					
					count[0] = 0;
				}
            }
        }
		
		if (score[0] > score[1])
			vencedor = jogadores[0];
		else
			vencedor = jogadores[1];
	}

}
