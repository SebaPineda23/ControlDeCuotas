package GestionPagoMensual.club.ManejoErrores;

public class ErrorMessage extends RuntimeException {

    private String tipoError;
    private String mensaje;

    public ErrorMessage(String mensaje) {
        super(mensaje);
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
