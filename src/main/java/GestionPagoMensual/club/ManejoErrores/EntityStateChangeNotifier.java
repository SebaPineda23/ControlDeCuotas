//package GestionPagoMensual.club.ManejoErrores;
//
//import GestionPagoMensual.club.Entitys.Cliente;
//import GestionPagoMensual.club.Entitys.Estado;
//import GestionPagoMensual.club.Services.PagoMensualService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EntityStateChangeNotifier {
//
//    private final ApplicationEventPublisher eventPublisher;
//
//    @Autowired
//    PagoMensualService pagoMensualService;
//
//    public EntityStateChangeNotifier(ApplicationEventPublisher eventPublisher) {
//        this.eventPublisher = eventPublisher;
//    }
//
//    public void notifyStateChange(Cliente cliente) {
//        // Verifica si el estado cambió a "PAGO" y programa la tarea de revertir el estado después de 2 días
//        if (Estado.PAGO.equals(cliente.getEstado())) {
//            pagoMensualService.programarRevertirEstado(cliente);
//        }
//    }
//}
