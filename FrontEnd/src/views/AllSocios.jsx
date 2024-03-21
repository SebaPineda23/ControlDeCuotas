import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { getAllSocios } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";
import useTabla from "../components/Tabla";
import { Toaster } from "react-hot-toast";

export default function AllSocios() {
  const { handleAllServices, notificarExito, notificarError } = useFilters();
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

  const Tabla = useTabla(data);

  return (
    <div className="w-4/5 bg-gray-200 mx-5 mb-2 flex items-center justify-center rounded-lg flex-col">
      <Tabla />
      <Toaster />
    </div>
  );
}
