import { useNavigate } from 'react-router-dom';
import { Button } from "antd";
import SearchById from "../components/SearchById";
import SearchByName from "../components/SearchByName";
export default function Home() {
  const navigate = useNavigate();

  const goForm = () => navigate("/nuevoSocio");
  const goAllSocios = () => navigate("/allSocios");
  const goPago = () => navigate("/pago");

  return (
    <div className="flex flex-col w-3/4 items-center justify-center">
      <div className="w-full flex flex-col items-center">
        <div className="w-full sm:w-1/2 xl:w-1/3 my-3">
          <SearchById />
        </div>
        <div className="w-full sm:w-1/2 xl:w-1/3 my-3">
          <SearchByName />
        </div>
      </div>
      <div className="mt-5 flex flex-col xl:flex-row items-center">
        <Button
          onClick={goForm}
          className="bg-blue-700 hover:bg-gray-900 mb-2 w-full"
          type="primary"
        >
          Agregar socio
        </Button>
        <Button
          onClick={goAllSocios}
          className="bg-blue-700 mx-2 hover:bg-gray-900 mb-2 w-full"
          type="primary"
        >
          Todos los socios
        </Button>
        <Button
          onClick={goPago}
          className="bg-blue-700 hover:bg-gray-900 w-full mb-2"
          type="primary"
        >
          Registrar Pago
        </Button>
      </div>
    </div>
  );
}
