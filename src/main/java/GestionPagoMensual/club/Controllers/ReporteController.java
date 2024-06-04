package GestionPagoMensual.club.Controllers;

import GestionPagoMensual.club.Services.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("adm_clubes/generacionPDF")
@CrossOrigin("*")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> descargarPdf(@RequestParam String categoria, @RequestParam String mesAno) {
        try {
            // Generar el PDF con los datos de los clientes y montos totales
            byte[] pdfBytes = reporteService.generarPdfDeClientesYMontosTotales(categoria, mesAno);

            // Configuración de los encabezados de la respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "reporte.pdf");

            // Devolver el PDF como un array de bytes en la respuesta HTTP
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Manejo de excepciones en caso de error durante la generación del PDF
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}