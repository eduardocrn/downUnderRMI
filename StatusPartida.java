public enum StatusPartida {
    AGUARDANDO(-2), INICIADA(1), ENCERRADA(2), TIMEOUT(3);

    private final int value;
	StatusPartida(int value){
		this.value = value;
	}
	public int getValor(){
		return value;
	}
}