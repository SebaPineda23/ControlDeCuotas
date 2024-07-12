import React from "react";
import { Table } from "antd";
import { useSelector } from "react-redux";
import { getMontoTotal, getSociosFiltered } from "../redux/setSocios";
import BackButton from "./BackButton";
import { DownloadOutlined } from "@ant-design/icons";
import useFilters from "../hooks/useFilters";
import { Toaster } from "react-hot-toast";

export default function TablaDeSociosPagos() {
  const { downloadDataPDF } = useFilters();
  const columns = [
    {
      title: "Id",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "Nombre",
      dataIndex: "nombre",
      key: "nombre",
    },
    {
      title: "Apellido",
      dataIndex: "apellido",
      key: "apellido",
    },
    {
      title: "DNI",
      dataIndex: "dni",
      key: "dni",
    },
    {
      title: "Categoria",
      dataIndex: "categoria",
      key: "categoria",
    },
    {
      title: "Pago",
      key: "pago",
      render: (text, record) => {
        const cronograma = record.cronogramaPagos;
        let suma = 0;
        for (let i = 0; i < cronograma.length; i++) {
          suma += cronograma[i].monto;
        }
        return "$" + suma.toLocaleString();
      },
    },
  ];
  const data = useSelector(getSociosFiltered);
  const montoTotal = useSelector(getMontoTotal);
  return (
    <div className="bg-white rounded-xl w-[70%]">
      <BackButton />
      <Table columns={columns} dataSource={data} rowKey="dni" />
      <div className="flex w-full justify-between px-3 py-3 items-center">
        <h3 className="text-base">
          Monto total: ${montoTotal.toLocaleString()}
        </h3>
        <div>
          {/* <button
            onClick={downloadDataPDF}
            className="bg-blue-600 text-white p-1 rounded-xl mx-2"
          >
            Descargar datos en PDF <DownloadOutlined />
          </button> */}
          <button
            onClick={downloadDataExcel}
            className="bg-blue-600 text-white p-1 rounded-xl"
          >
            Descargar datos en Excel <DownloadOutlined />
          </button>
        </div>
      </div>
      <Toaster />
    </div>
  );
}
