import React from "react";
import { DeleteOutlined, EditOutlined, FileOutlined } from "@ant-design/icons";
import { Tooltip } from "antd";

export default function TableButtons({
  record,
  onRowClick,
  handleHistorial,
  deleteSocios,
}) {
  return (
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
  );
}
