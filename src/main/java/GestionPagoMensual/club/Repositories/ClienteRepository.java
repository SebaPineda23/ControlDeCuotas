package GestionPagoMensual.club.Repositories;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.Estado;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.dto.ClienteMontoTotalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByDni(String dni);
    //List<Cliente> findByNombreAndApellido(String nombre, String apellido);
    List<Cliente> findByNombreContainingOrApellidoContaining(String letras1, String letras2);
    List<Cliente> findByFechaCambioEstadoAfter(LocalDateTime fechaLimite);
    List<Cliente> findClienteByCategoria(String categoria);
    @Query("SELECT c, SUM(p.monto) " +
            "FROM Cliente c JOIN c.cronogramaPagos p " +
            "WHERE c.categoria = :categoria AND SUBSTRING(p.fecha, 4, 2) = :monthOfPayment " +
            "GROUP BY c")
    List<Object[]> findClientesAndTotalAmountByCategoryAndMonthOfPayment(
            @Param("categoria") String categoria,
            @Param("monthOfPayment") String monthOfPayment);
}
