package es.cic.curso25.proy009.exceptions;

public class RamaNotFound extends RuntimeException {
    public RamaNotFound() {
        super();
    }

    public RamaNotFound(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public RamaNotFound(String mensaje) {
        super(mensaje);
    }

    public RamaNotFound(Throwable causa) {
        super(causa);
    }
}


