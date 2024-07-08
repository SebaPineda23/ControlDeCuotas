package GestionPagoMensual.club.Controllers;


import GestionPagoMensual.club.Services.ReporteService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@RequestMapping("/adm_clubes/excel")
class ExcelController {
    @Autowired
    private Environment env;
    @Autowired
    private ReporteService reporteService;

    @GetMapping("/exportar_Todas_las_tablas")
    public ByteArrayResource exportToExcel() {
        try {
            String url = System.getenv("SPRING_DATASOURCE_URL");
            String username = System.getenv("SPRING_DATASOURCE_USERNAME");
            String password = System.getenv("SPRING_DATASOURCE_PASSWORD");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {

                // Consulta para la tabla clientes
                String clientesSql = "SELECT * FROM cliente";
                try (Statement clientesStatement = connection.createStatement();
                     ResultSet clientesResultSet = clientesStatement.executeQuery(clientesSql)) {

                    // Consulta para la tabla pago_mensual
                    String pagoMensualSql = "SELECT * FROM pago_mensual";
                    try (Statement pagoMensualStatement = connection.createStatement();
                         ResultSet pagoMensualResultSet = pagoMensualStatement.executeQuery(pagoMensualSql)) {

                        // Crear el libro de trabajo y la hoja
                        Workbook workbook = new XSSFWorkbook();
                        Sheet sheet = workbook.createSheet("Datos");

                        // Estilo de celda para el texto en negrita
                        Font boldFont = workbook.createFont();
                        boldFont.setBold(true);

                        // Estilo para las celdas de encabezado (nombres de tablas)
                        CellStyle headerStyle = workbook.createCellStyle();
                        headerStyle.setFont(boldFont);

                        // Agregar encabezado para la tabla clientes
                        ResultSetMetaData clientesMetaData = clientesResultSet.getMetaData();
                        int clientesColumnCount = clientesMetaData.getColumnCount();
                        Row clientesHeaderRow = sheet.createRow(0);
                        for (int i = 1; i <= clientesColumnCount; i++) {
                            Cell cell = clientesHeaderRow.createCell(i - 1);
                            cell.setCellValue(clientesMetaData.getColumnName(i));
                            cell.setCellStyle(headerStyle); // Aplicar estilo de encabezado
                        }

                        // Agregar datos para la tabla clientes
                        int clientesRowCount = 1;
                        while (clientesResultSet.next()) {
                            Row row = sheet.createRow(clientesRowCount++);
                            for (int i = 1; i <= clientesColumnCount; i++) {
                                Cell cell = row.createCell(i - 1);
                                cell.setCellValue(clientesResultSet.getString(i));
                            }
                        }

                        // Agregar encabezado para la tabla pago_mensual
                        ResultSetMetaData pagoMensualMetaData = pagoMensualResultSet.getMetaData();
                        int pagoMensualColumnCount = pagoMensualMetaData.getColumnCount();
                        Row pagoMensualHeaderRow = sheet.createRow(clientesRowCount);
                        for (int i = 1; i <= pagoMensualColumnCount; i++) {
                            Cell cell = pagoMensualHeaderRow.createCell(i - 1);
                            cell.setCellValue(pagoMensualMetaData.getColumnName(i));
                            cell.setCellStyle(headerStyle); // Aplicar estilo de encabezado
                        }

                        // Agregar datos para la tabla pago_mensual
                        int pagoMensualRowCount = clientesRowCount + 1;
                        while (pagoMensualResultSet.next()) {
                            Row row = sheet.createRow(pagoMensualRowCount++);
                            for (int i = 1; i <= pagoMensualColumnCount; i++) {
                                Cell cell = row.createCell(i - 1);
                                cell.setCellValue(pagoMensualResultSet.getString(i));
                            }
                        }

                        // Establecer el ancho de las columnas
                        for (int i = 0; i < clientesColumnCount; i++) {
                            sheet.setColumnWidth(i, 6000); // Establecer ancho de columna en unidades de 1/256th of a character width
                        }

                        // Escribir el libro de trabajo en un flujo de bytes
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        workbook.write(outputStream);
                        workbook.close();

                        // Convertir el flujo de bytes en un recurso descargable
                        byte[] excelBytes = outputStream.toByteArray();
                        return new ByteArrayResource(excelBytes);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/reporte_Mensual")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam("categoria") String categoria,
            @RequestParam("mesAno") String monthOfPayment) {
        try {
            // Obtener las variables de entorno
            String url = System.getenv("SPRING_DATASOURCE_URL");
            String username = System.getenv("SPRING_DATASOURCE_USERNAME");
            String password = System.getenv("SPRING_DATASOURCE_PASSWORD");

            // Verificar si las variables de entorno están presentes
            if (url == null || username == null || password == null) {
                throw new RuntimeException("Las variables de entorno para la base de datos no están configuradas.");
            }

            // Generar el archivo Excel como un ByteArrayResource
            ByteArrayResource resource = reporteService.generarExcelDeClientesYMontosTotales(url, username, password, categoria, monthOfPayment);

            // Construir la respuesta con el archivo Excel
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=ReporteClientes.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            // Imprimir un mensaje de error para depuración
            System.err.println("Error al generar el archivo Excel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(new byte[0]));  // Retorna un recurso vacío para evitar una respuesta nula
        } catch (RuntimeException e) {
            e.printStackTrace();
            // Imprimir un mensaje de error para depuración
            System.err.println("Error de ejecución: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(new byte[0]));  // Retorna un recurso vacío para evitar una respuesta nula
        }
    }
}
    //Hay que configurar el entorno para que funcione en deploy, pero funciona de forma local

//    @PostMapping("/import")
//    public String importFromExcel(@RequestParam("file") MultipartFile excelFile) {
//        try (InputStream inputStream = excelFile.getInputStream();
//             Workbook workbook = new XSSFWorkbook(inputStream)) {
//
//            // Obtener la URL, nombre de usuario y contraseña de las variables de entorno
//            String url = System.getenv("SPRING_DATASOURCE_URL");
//            String username = System.getenv("SPRING_DATASOURCE_USERNAME");
//            String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
//
//            try (Connection connection = DriverManager.getConnection(url, username, password)) {
//                // Desactivar autocommit
//                connection.setAutoCommit(false);
//
//                Sheet sheet = workbook.getSheetAt(0);
//
//                String sql = "INSERT INTO cliente (id, apellido, categoria, dni, email, estado, fecha_cambio_estado, fecha_nacimiento, nombre, pago) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//                PreparedStatement statement = connection.prepareStatement(sql);
//
//                for (Row row : sheet) {
//                    if (row.getRowNum() == 0) {
//                        continue; // Skip header row
//                    }
//
//                    try {
//                        Long id = (long) getNumericCellValue(row.getCell(0), 0);
//                        String apellido = getStringCellValue(row.getCell(1), 1);
//                        String categoria = getStringCellValue(row.getCell(2), 2);
//                        String dni = getStringCellValue(row.getCell(3), 3);
//                        String email = getStringCellValue(row.getCell(4), 4);
//                        int estado = (int) getNumericCellValue(row.getCell(5), 5);
//                        String fechaCambioEstado = getStringCellValue(row.getCell(6), 6);
//                        String fechaNacimiento = getStringCellValue(row.getCell(7), 7);
//                        String nombre = getStringCellValue(row.getCell(8), 8);
//                        double pago = getNumericCellValue(row.getCell(9), 9);
//
//                        statement.setLong(1, id);
//                        statement.setString(2, apellido);
//                        statement.setString(3, categoria);
//                        statement.setString(4, dni);
//                        statement.setString(5, email);
//                        statement.setInt(6, estado);
//
//                        if (fechaCambioEstado.isEmpty()) {
//                            statement.setNull(7, java.sql.Types.TIMESTAMP);
//                        } else {
//                            statement.setString(7, fechaCambioEstado);
//                        }
//
//                        statement.setString(8, fechaNacimiento);
//                        statement.setString(9, nombre);
//                        statement.setDouble(10, pago);
//
//                        statement.addBatch();
//                    } catch (IllegalStateException | NumberFormatException e) {
//                        return "Error en la fila " + (row.getRowNum() + 1) + ", columna " + (e.getMessage());
//                    }
//                }
//
//                statement.executeBatch();
//                connection.commit();
//
//                statement.close();
//
//                // Restablecer autocommit a true
//                connection.setAutoCommit(true);
//
//                return "Datos importados en la tabla cliente con éxito";
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return "Error al conectar a la base de datos";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error al importar datos desde el archivo Excel";
//        }
//    }
//
//    private double getNumericCellValue(Cell cell, int columnIndex) {
//        if (cell == null) {
//            throw new IllegalStateException("La celda en la columna " + columnIndex + " está vacía");
//        }
//        switch (cell.getCellType()) {
//            case NUMERIC:
//                return cell.getNumericCellValue();
//            case STRING:
//                try {
//                    return Double.parseDouble(cell.getStringCellValue());
//                } catch (NumberFormatException e) {
//                    throw new IllegalStateException("No se puede convertir el valor de la celda en la columna " + columnIndex + " a un número");
//                }
//            case BLANK:
//                return 0.0; // O un valor por defecto apropiado para tu caso
//            default:
//                throw new IllegalStateException("El tipo de celda en la columna " + columnIndex + " no es numérico");
//        }
//    }
//    private String getStringCellValue(Cell cell, int columnIndex) {
//        if (cell == null) {
//            throw new IllegalStateException("celda vacía en la columna " + columnIndex);
//        }
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                return String.valueOf(cell.getNumericCellValue());
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            case BLANK:
//                return "";
//            default:
//                throw new IllegalStateException("tipo de celda no compatible en la columna " + columnIndex);
//        }
//    }


