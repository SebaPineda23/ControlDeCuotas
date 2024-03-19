import { Table } from "antd";
import { useSelector } from "react-redux";
import { getAllSocios } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";
import { useEffect } from "react";
export default function AllSocios() {
  const columns = [
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
  const { handleAllServices } = useFilters();
  useEffect(() => {
    handleAllServices();
  }, []);
  const socios = useSelector(getAllSocios);
  const data = socios.map((socio) => ({
    key: socio.id,
    nombre: socio.nombre,
    apellido: socio.apellido,
    dni: socio.dni,
    fechaDeNacimiento: socio.fecha_nacimiento,
    estado: socio.estado,
  }));

  return (
    <div className="w-full mx-5 mb-2">
      <Table columns={columns} dataSource={data} />
    </div>
  );
}
