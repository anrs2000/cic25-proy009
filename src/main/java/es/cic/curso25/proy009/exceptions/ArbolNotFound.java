package es.cic.curso25.proy009.exceptions;

public class ArbolNotFound extends RuntimeException {
    public ArbolNotFound() {
        super();
    }

    public ArbolNotFound(String mensaje) {
        super(mensaje);
    }

    public ArbolNotFound(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }

    public ArbolNotFound(Throwable cause) {
        super(cause);
    }
}
