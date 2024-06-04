package GestionPagoMensual.club.dto;

import GestionPagoMensual.club.Entitys.Cliente;

import java.util.List;

public class ClientesYTotal {
    private List<Cliente> clientes;
    private double montoTotal;

    public ClientesYTotal(List<Cliente> clientes, double montoTotal) {
        this.clientes = clientes;
        this.montoTotal = montoTotal;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}

