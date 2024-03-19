import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar'
import { Button } from 'antd';
import { useEffect } from "react";
import useFilters from "../hooks/useFilters";
export default function Home() {
  const { handleAllServices } = useFilters();
  useEffect(() => {
    handleAllServices();
  }, []);
  const navigate = useNavigate();

  const goForm = () => navigate("/nuevoSocio");
  const goAllSocios = () => navigate("/allSocios");

  return (
    <div className="flex flex-col">
      <div>
        <SearchBar />
      </div>
      <div className="mt-5">
        <Button onClick={goForm} className="bg-blue-700" type="primary">
          Agregar socio
        </Button>
        <Button
          onClick={goAllSocios}
          className="bg-blue-700 mx-2"
          type="primary"
        >
          Todos los socios
        </Button>
      </div>
    </div>
  );
}
