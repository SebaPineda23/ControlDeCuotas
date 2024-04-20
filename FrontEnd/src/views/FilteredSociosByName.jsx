import { useSelector } from "react-redux";
import { getSociosFiltered } from "../redux/setSocios";
import { Table } from "antd";
import BackButton from "../components/BackButton";

export default function FilteredSociosByName() {
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
    </div>
  );
}
