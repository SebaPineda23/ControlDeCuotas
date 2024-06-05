import React from "react";
import { Table } from "antd";
import useFilters from "../hooks/useFilters";
import TableButtons from "./TableButons";

function Tabla({ data, onRowClick, handleHistorial }) {
  const { deleteSocios } = useFilters();
  const formatFecha = (fecha) => {
    const partes = fecha.split("-");
    const nuevaFecha = partes[2] + "/" + partes[1] + "/" + partes[0];
    return nuevaFecha;
  };
  const columns = [
    {
      title: "N° de socio",
      dataIndex: "id",
      sorter: (a, b) => a.id - b.id,
    },
    {
      title: "Nombre",
      dataIndex: "nombre",
      defaultSortOrder: "ascend",
      sorter: (a, b) => a.nombre.length - b.nombre.length,
    },
    {
      title: "Apellido",
      dataIndex: "apellido",
    },
    {
      title: "Email",
      dataIndex: "email",
    },
    {
      title: "Dni",
      dataIndex: "dni",
    },
    {
      title: "Categoria",
      dataIndex: "categoria",
    },
    {
      title: "Fecha de nacimiento",
      dataIndex: "fecha_nacimiento",
      render: (fecha) => formatFecha(fecha),
    },
    {
      title: "Estado",
      dataIndex: "estado",
      render: (estado) => (
        <span
          style={{
            color: "white",
            background: estado === "PAGO" ? "green" : "red",
            padding: "5px",
            borderRadius: "5px",
          }}
        >
          {estado}
        </span>
      ),
    },
    {
      title: "Acciones",
      dataIndex: "acciones",
      render: (_, record) => (
        <TableButtons
          record={record}
          onRowClick={onRowClick}
          handleHistorial={handleHistorial}
          deleteSocios={deleteSocios}
        />
      ),
    },
  ];
  return (
    <div className="w-full">
      <Table
        columns={columns}
        dataSource={data.map((item) => ({
          ...item,
          key: item.id,
        }))}
        scroll={{ x: 240 }}
        pagination={{ pageSize: 7 }}
      />
    </div>
  );
}

export default Tabla;
