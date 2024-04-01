import React from "react";
import { Table } from "antd";
import { useSelector } from "react-redux";
import { getHistorial } from "../redux/setSocios";

export default function Historial() {
  const formatFecha = (fecha) => {
    const partes = fecha.split("-");
    const nuevaFecha = partes[2] + "/" + partes[1] + "/" + partes[0];
    return nuevaFecha;
  };
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
      render: (fecha) => formatFecha(fecha),
    },
    {
      title: "Monto",
      dataIndex: "monto",
      key: "monto",
      render: (monto) => (
        <span>
          {"$" + monto.toLocaleString("es-AR", { minimumFractionDigits: 2 })}
        </span>
      ),
    },
  ];
  const data = useSelector(getHistorial);
  return (
    <div>
      <Table columns={columns} dataSource={data} rowKey={data.id} />
    </div>
  );
}
