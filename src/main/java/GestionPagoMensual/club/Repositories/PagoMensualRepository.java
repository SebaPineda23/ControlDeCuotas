package GestionPagoMensual.club.Repositories;

import GestionPagoMensual.club.Entitys.PagoMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoMensualRepository extends JpaRepository<PagoMensual, Long> {
    List<PagoMensual> findByClienteId(Long idCliente);
    @Query(value = "SELECT * FROM pago_mensual WHERE SUBSTRING(fecha, 4, 7) = :mesAno", nativeQuery = true)
    List<PagoMensual> findPagosByMes(@Param("mesAno") String mesAno);

    @Query(value = "SELECT SUM(p.monto) FROM pago_mensual p JOIN Cliente c ON p.cliente_id = c.id WHERE SUBSTRING(p.fecha, 4, 7) = :mesAno AND c.categoria = :categoria", nativeQuery = true)
    Double calcularMontoTotalPorCategoriaYMes(@Param("mesAno") String mesAno, @Param("categoria") String categoria);
}