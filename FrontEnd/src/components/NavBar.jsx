import { CloseOutlined, MenuOutlined } from "@ant-design/icons";
import { useState } from "react";
import SearchById from "./SearchById";
import SearchByName from "./SearchByName";
import { useNavigate } from "react-router-dom";
import { Button } from "antd";

export default function NavBar() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();
  const goForm = () => navigate("/nuevoSocio");
  const goPago = () => navigate("/pago");
  const menuItems = [
    {
      key: "searchById",
      component: <SearchById />,
    },
    {
      key: "searchByName",
      component: <SearchByName />,
    },
  ];
  return (
    <nav className="bg-gray-700 bg-opacity-95 flex justify-around items-center px-4 py-2 text-white w-full sticky top-0 z-50">
      <div className="flex items-center space-x-4 p-5 w-full justify-center">
        <div className="hidden sm:flex">
          <Button
            onClick={goForm}
            className="bg-blue-500 hover:bg-gray-900 mb-2 w-full mx-2"
            type="primary"
          >
            Agregar socio
          </Button>
          <Button
            onClick={goPago}
            className="bg-blue-500 hover:bg-gray-900 w-full mb-2 mx-2"
            type="primary"
          >
            Registrar Pago
          </Button>
        </div>
        <div className="hidden sm:flex space-x-4">
          {menuItems.map((item) => (
            <div key={item.key} className="flex items-center">
              <span className="ml-2">{item.component}</span>
            </div>
          ))}
        </div>
      </div>
      <div className="sm:hidden ">
        <button
          onClick={() => setIsMenuOpen(!isMenuOpen)}
          className="block text-white focus:outline-none"
        >
          {isMenuOpen ? (
            <CloseOutlined className="text-4xl mr-5" />
          ) : (
            <MenuOutlined className="text-4xl mr-5" />
          )}
        </button>
      </div>
      {isMenuOpen && (
        <div className="sm:hidden absolute right-0 top-14 bg-gray-700 w-full">
          <div className="flex flex-col space-y-2 py-2 px-4">
            {menuItems.map((item) => (
              <div className="flex items-center">
                <span className="ml-2 w-full">{item.component}</span>
              </div>
            ))}
            <div className="flex">
              <Button
                onClick={goForm}
                className="bg-blue-700 hover:bg-gray-900 mb-2 w-full mx-2"
                type="primary"
              >
                Agregar socio
              </Button>
              <Button
                onClick={goPago}
                className="bg-blue-700 hover:bg-gray-900 w-full mb-2 mx-2"
                type="primary"
              >
                Registrar Pago
              </Button>
            </div>
          </div>
        </div>
      )}
    </nav>
  );
}
