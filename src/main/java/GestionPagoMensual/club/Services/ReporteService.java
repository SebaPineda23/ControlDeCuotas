package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.dto.ClientesYTotal;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteService {

    private final ClienteService clienteService;

    public ReporteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public byte[] generarPdfDeClientesYMontosTotales(String categoria, String monthOfPayment) throws IOException {
        // Obtener datos
        ClientesYTotal datos = clienteService.getClientesByPagoMesAndCategoria(monthOfPayment, categoria);

        // Generar HTML
        String html = generarHtml(datos, categoria, monthOfPayment);

        // Convertir HTML a PDF
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private String generarHtml(ClientesYTotal datos, String categoria, String monthOfPayment) {
        StringBuilder html = new StringBuilder();
        html.append("<html>")
                .append("<head><style>")
                .append("table {width: 100%; border-collapse: collapse;}")
                .append("table, th, td {border: 1px solid black;}")
                .append("th, td {padding: 10px; text-align: left;}")
                .append("</style></head>")
                .append("<body>")
                .append("<h1>Reporte de Clientes y Montos Totales</h1>")
                .append("<h2>Categoría: ").append(categoria).append("</h2>")
                .append("<h2>Mes y Año: ").append(monthOfPayment).append("</h2>")
                .append("<table>")
                .append("<tr><th>Cliente</th><th>Monto Total</th></tr>");

        // Suponiendo que ClientesYTotal contiene una lista de clientes
        List<Cliente> listaClientes = datos.getClientes();
        for (Cliente cliente : listaClientes) {
            // Sumar los montos de los pagos del cliente
            double montoTotalCliente = cliente.getCronogramaPagos().stream()
                    .mapToDouble(pago -> pago.getMonto())
                    .sum();
            // Agregar el símbolo de peso ($) al monto total
            String montoConPeso = "$ " + montoTotalCliente;
            html.append("<tr>")
                    .append("<td>").append(cliente.getNombre()).append(" ").append(cliente.getApellido()).append("</td>")
                    .append("<td>").append(montoConPeso).append("</td>")
                    .append("</tr>");
        }

        // Sumar los montos totales de todos los clientes
        double montoTotal = datos.getMontoTotal();
        // Agregar el símbolo de peso ($) al monto total final
        String montoTotalConPeso = "$ " + montoTotal;
        html.append("<tr>")
                .append("<td><b>Total</b></td>")
                .append("<td><b>").append(montoTotalConPeso).append("</b></td>")
                .append("</tr>")
                .append("</table>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}
