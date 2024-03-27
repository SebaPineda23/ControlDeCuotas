import React from "react";
import { Table } from "antd";
import { useSelector } from "react-redux";
import { getHistorial } from "../redux/setSocios";

export default function Historial() {
  const columns = [
    {
      title: "Id de pago",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "Fecha",
      dataIndex: "fecha",
      key: "fecha",
    },
    {
      title: "Monto",
      dataIndex: "monto",
      key: "monto",
    },
  ];
  const data = useSelector(getHistorial);
  console.log(data);
  return (
    <div>
      <Table columns={columns} dataSource={data} rowKey={data.id} />
    </div>
  );
}
