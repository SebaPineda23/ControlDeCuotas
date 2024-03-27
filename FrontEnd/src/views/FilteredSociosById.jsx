import { useSelector } from "react-redux";
import { getSociosFiltered } from "../redux/setSocios";
import { Toaster } from "react-hot-toast";
import { Table } from "antd";
import BackButton from "../components/BackButton";

export default function FilteredSociosById() {
  const socios = useSelector(getSociosFiltered);
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
  const data = socios.map((socio) => ({
    key: socio.id,
    id: socio.id,
    nombre: socio.nombre,
    apellido: socio.apellido,
    dni: socio.dni,
    fechaDeNacimiento: socio.fecha_nacimiento,
    estado: socio.estado,
  }));

  return (
    <div className="w-full bg-gray-200 mx-5 mb-2 flex flex-col items-center justify-center rounded-xl">
      <div className="w-full flex items-start">
        <BackButton />
      </div>
      <Table columns={columns} dataSource={data} className="w-full" />
      <Toaster />
    </div>
  );
}
