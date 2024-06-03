import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getAllSocios, setEditSocio } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";
import Tabla from "../components/Tabla";
import { Toaster } from "react-hot-toast";
import { Modal } from "antd";
import EditSocio from "../components/EditSocio";
import Historial from "../components/Historial";

export default function AllSocios() {
  const dispatch = useDispatch();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isHistorialModalOpen, setIsHistorialModalOpen] = useState(false);
  const { handleAllSocios, historialDePago, allPagos } = useFilters();
  const socios = useSelector(getAllSocios);
  useEffect(() => {
    handleAllSocios();
    allPagos();
  }, []);

  const handleEditSocio = (socio) => {
    dispatch(setEditSocio(socio));
    setIsEditModalOpen(true);
  };

  const handleHistorial = (historial) => {
    setIsHistorialModalOpen(true);
    historialDePago(historial);
  };

  return (
    <div className="w-full mt-32 sm:mt-2 bg-gray-200 mb-2 flex items-center justify-center rounded-lg flex-col">
      <Tabla
        data={socios}
        onRowClick={handleEditSocio}
        handleHistorial={handleHistorial}
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
      <Toaster />
    </div>
  );
}
