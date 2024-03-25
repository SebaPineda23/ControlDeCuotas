import React from "react";
import { Table, Button, Tooltip } from "antd";
import { DeleteOutlined, EditOutlined } from "@ant-design/icons";
import useFilters from "../hooks/useFilters";
function useTabla(data, onRowClick) {
  const { deleteSocios } = useFilters();

  const columns = [
    {
      title: "N° de socio",
      dataIndex: "id",
    },
    {
      title: "Nombre",
      dataIndex: "nombre",
    },
    {
      title: "Apellido",
      dataIndex: "apellido",
    },
    {
      title: "Dni",
      dataIndex: "dni",
    },
    {
      title: "Fecha de nacimiento",
      dataIndex: "fechaDeNacimiento",
    },
    {
      title: "Estado",
      dataIndex: "estado",
    },
    {
      title: "Acciones",
      dataIndex: "acciones",
      render: (_, record) => (
        <div className="w-20 flex items-center justify-aroud">
          <Tooltip title="Editar">
            <button
              type="primary"
              className="bg-blue-400 hover:bg-blue-900 flex justify-center mr-2 items-center p-3 rounded-xl"
              onClick={() => onRowClick(record)}
            >
              <EditOutlined />
            </button>
          </Tooltip>
          <Tooltip title="Eliminar">
            <button
              type="primary"
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

  const Tabla = () => (
    <div className="w-full">
      <Table
        className="w-full"
        columns={columns}
        dataSource={data}
        scroll={{ x: 240 }}
      />
    </div>
  );

  return Tabla;
}

export default useTabla;
