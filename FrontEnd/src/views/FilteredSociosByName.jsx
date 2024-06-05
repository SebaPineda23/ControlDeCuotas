import { useDispatch, useSelector } from "react-redux";
import { getSociosFiltered } from "../redux/setSocios";
import { Modal, Table } from "antd";
import BackButton from "../components/BackButton";
import TableButtons from "../components/TableButons";
import { useState } from "react";
import { setEditSocio } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";
import EditSocio from "../components/EditSocio";
import Historial from "../components/Historial";

export default function FilteredSociosByName() {
  const dispatch = useDispatch();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isHistorialModalOpen, setIsHistorialModalOpen] = useState(false);
  const socios = useSelector(getSociosFiltered);
  const { deleteSocios } = useFilters();

  const onRowClick = (socio) => {
    dispatch(setEditSocio(socio));
    setIsEditModalOpen(true);
  };

  const handleHistorial = (historial) => {
    setIsHistorialModalOpen(true);
    historialDePago(historial);
  };
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
      dataIndex: "fechaDeNacimiento",
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
  const data = socios.map((socio) => ({
    key: socio.id,
    id: socio.id,
    nombre: socio.nombre,
    apellido: socio.apellido,
    dni: socio.dni,
    fechaDeNacimiento: socio.fecha_nacimiento,
    estado: socio.estado,
    email: socio.email,
    categoria: socio.categoria,
  }));
  const formatFecha = (fecha) => {
    const partes = fecha.split("-");
    const nuevaFecha = partes[2] + "/" + partes[1] + "/" + partes[0];
    return nuevaFecha;
  };
  return (
    <div className="w-full bg-gray-200 mx-5 mb-2 flex flex-col items-center justify-center rounded-xl">
      <div className="w-full flex items-start">
        <BackButton />
      </div>
      <Table
        columns={columns}
        dataSource={data}
        scroll={{ x: 240 }}
        className="w-full"
      />
      <Modal
        title="Editar datos del socio"
        open={isEditModalOpen}
        onCancel={() => setIsEditModalOpen(false)}
        footer={null}
      >
        <EditSocio />
      </Modal>
      <Modal
        title="Historial de pago"
        open={isHistorialModalOpen}
        onCancel={() => setIsHistorialModalOpen(false)}
        footer={null}
      >
        <Historial />
      </Modal>
    </div>
  );
}
