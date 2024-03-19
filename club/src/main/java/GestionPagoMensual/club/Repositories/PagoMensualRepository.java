package GestionPagoMensual.club.Repositories;

import GestionPagoMensual.club.Entitys.PagoMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoMensualRepository extends JpaRepository<PagoMensual, Long> {

    List<PagoMensual> findByFechaCambioEstadoAfter(LocalDateTime fechaLimite);


}
