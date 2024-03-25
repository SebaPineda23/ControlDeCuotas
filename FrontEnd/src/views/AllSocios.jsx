import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllSocios, setEditSocio } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";
import useTabla from "../components/Tabla";
import { Toaster } from "react-hot-toast";
import { Modal } from "antd";
import EditSocio from "../components/EditSocio";
import BackButton from "../components/BackButton";

export default function AllSocios() {
  const dispatch = useDispatch();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const showModal = () => {
    setIsModalOpen(true);
  }
  const handleCancel = () => {
    setIsModalOpen(false);
  };
  const { handleAllServices } = useFilters();
  useEffect(() => {
    handleAllServices();
  }, []);
  const socios = useSelector(getAllSocios);
  const data = socios.map((socio) => ({
    key: socio.id,
    id: socio.id,
    nombre: socio.nombre,
    apellido: socio.apellido,
    dni: socio.dni,
    fechaDeNacimiento: socio.fecha_nacimiento,
    estado: socio.estado,
  }));

  const Tabla = useTabla(data, (socio) => {
    dispatch(setEditSocio(socio));
    showModal();
    console.log("Información del socio:", socio);
  });

  return (
    <div className="w-4/5 bg-gray-200 mx-5 mb-2 flex items-center justify-center rounded-lg flex-col">
      <div className="w-24 self-start p-2">
        <BackButton />
      </div>
      <Tabla />
      <Modal
        title="Editar datos del socio"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
      >
        <EditSocio />
      </Modal>

      <Toaster />
    </div>
  );
}
