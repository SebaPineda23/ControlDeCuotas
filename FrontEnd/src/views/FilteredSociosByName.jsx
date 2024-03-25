import { useSelector } from "react-redux";
import { getSociosFiltered } from "../redux/setSocios";
import useTabla from "../components/Tabla";

export default function FilteredSociosByName() {

  const socios = useSelector(getSociosFiltered);
  console.log(socios);
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
    <div className="w-4/5 bg-gray-200 mx-5 mb-2 flex items-center justify-center">
      <Tabla />
    </div>
  );
}
