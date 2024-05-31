import React from "react";
import { Table, Tooltip } from "antd";
import { DeleteOutlined, EditOutlined, FileOutlined } from "@ant-design/icons";
import useFilters from "../hooks/useFilters";

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
        <div className="w-20 2xl:w-full flex items-center justify-around">
          <Tooltip title="Editar">
            <button
              type="button"
              className="bg-blue-400 hover:bg-blue-500 flex justify-center items-center p-3 rounded-xl"
              onClick={() => onRowClick(record)}
            >
              <EditOutlined />
            </button>
          </Tooltip>
          <Tooltip title="Historial de pago">
            <button
              type="button"
              className="bg-blue-500 flex justify-center mx-2 items-center p-3 rounded-xl text-white"
              onClick={() => handleHistorial(record)}
            >
              <FileOutlined />
            </button>
          </Tooltip>
          <Tooltip title="Eliminar">
            <button
              type="button"
              className="bg-red-500 flex justify-center items-center p-3 rounded-xl"
              onClick={() => deleteSocios(record)}
            >
              <DeleteOutlined />
            </button>
          </Tooltip>
        </div>
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
