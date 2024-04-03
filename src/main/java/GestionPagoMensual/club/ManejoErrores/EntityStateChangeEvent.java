package GestionPagoMensual.club.ManejoErrores;

import GestionPagoMensual.club.Entitys.Cliente;
import org.springframework.context.ApplicationEvent;

public class EntityStateChangeEvent extends ApplicationEvent {
    private Cliente cliente;

    public EntityStateChangeEvent(Cliente cliente) {
        super(cliente);
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

}
