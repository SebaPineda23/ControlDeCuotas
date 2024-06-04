package GestionPagoMensual.club.dto;

import GestionPagoMensual.club.Entitys.Cliente;

import java.util.List;

public class ClienteMontoTotalDTO {
    private List<Cliente> clientes;
    private Double montoTotalFinal;

    public ClienteMontoTotalDTO(List<Cliente> clientes, Double montoTotalFinal) {
        this.clientes = clientes;
        this.montoTotalFinal = montoTotalFinal;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Double getMontoTotalFinal() {
        return montoTotalFinal;
    }

    public void setMontoTotalFinal(Double montoTotalFinal) {
        this.montoTotalFinal = montoTotalFinal;
    }
}