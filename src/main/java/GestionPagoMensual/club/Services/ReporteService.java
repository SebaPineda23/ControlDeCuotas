package GestionPagoMensual.club.Services;

import GestionPagoMensual.club.Entitys.Cliente;
import GestionPagoMensual.club.Entitys.PagoMensual;
import GestionPagoMensual.club.dto.ClientesYTotal;
import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
                .append("<tr><th>Cliente</th><th>Total</th></tr>");

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
                .append("<td><b>Monto Final</b></td>")
                .append("<td><b>").append(montoTotalConPeso).append("</b></td>")
                .append("</tr>")
                .append("</table>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }

    public ByteArrayResource generarExcelDeClientesYMontosTotales(
            String url, String username, String password,
            String categoria, String monthOfPayment) throws IOException, SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Obtener datos
            ClientesYTotal datos = clienteService.getClientesByPagoMesAndCategoria(monthOfPayment, categoria);

            // Crear el libro de trabajo y la hoja
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Datos");

            // Estilo para texto en negrita
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            CellStyle boldStyle = workbook.createCellStyle();
            boldStyle.setFont(boldFont);

            // Agregar encabezados para la tabla
            Row headerRow = sheet.createRow(0);
            Cell clienteHeaderCell = headerRow.createCell(0);
            clienteHeaderCell.setCellValue("Cliente");
            clienteHeaderCell.setCellStyle(boldStyle); // Aplicar estilo en negrita a la celda "Cliente"

            headerRow.createCell(1).setCellValue("Total");
            sheet.setColumnWidth(0, 8000);

            // Suponiendo que ClientesYTotal contiene una lista de clientes
            List<Cliente> listaClientes = datos.getClientes();
            int rowCount = 1;
            for (Cliente cliente : listaClientes) {
                // Sumar los montos de los pagos del cliente
                double montoTotalCliente = cliente.getCronogramaPagos().stream()
                        .mapToDouble(pago -> pago.getMonto())
                        .sum();
                // Agregar el símbolo de peso ($) al monto total
                String montoConPeso = "$ " + montoTotalCliente;

                Row row = sheet.createRow(rowCount++);
                Cell clienteCell = row.createCell(0);
                clienteCell.setCellValue(cliente.getNombre() + " " + cliente.getApellido());

                Cell montoCell = row.createCell(1);
                montoCell.setCellValue(montoConPeso);
            }

            // Sumar los montos totales de todos los clientes
            double montoTotal = datos.getMontoTotal();
            // Agregar el símbolo de peso ($) al monto total final
            String montoTotalConPeso = "$ " + montoTotal;

            // Crear una fila adicional para el monto total final
            Row totalRow = sheet.createRow(rowCount);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Monto Final");
            totalLabelCell.setCellStyle(boldStyle); // Aplicar estilo en negrita al texto "Monto Final"

            Cell totalValueCell = totalRow.createCell(1);
            totalValueCell.setCellValue(montoTotalConPeso);
            totalValueCell.setCellStyle(boldStyle); // Aplicar estilo en negrita al valor del monto total final

            // Escribir el libro de trabajo en un flujo de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // Convertir el flujo de bytes en un recurso descargable
            byte[] excelBytes = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            return resource;
        }
    }
}
