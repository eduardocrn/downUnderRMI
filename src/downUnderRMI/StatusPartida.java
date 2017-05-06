package downUnderRMI;

public enum StatusPartida {
    AGUARDANDO(1), INICIADA(2), ENCERRADA(3);

    private final int value;
	StatusPartida(int value){
		this.value = value;
	}
	public int getValor(){
		return value;
	}
}