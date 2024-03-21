import React from "react";
import { Table } from "antd";

function useTabla(data) {
  const columns = [
    {
      title: "N° de cliente",
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
