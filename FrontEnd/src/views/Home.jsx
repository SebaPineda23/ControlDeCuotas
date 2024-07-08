import { Toaster } from "react-hot-toast";
import NavBar from "../components/NavBar";
import AllSocios from "./AllSocios";
import { Button } from "antd";
import useFilters from "../hooks/useFilters";

export default function Home({ onLogout }) {
  const { handleExtract } = useFilters();
  return (
    <div className="flex flex-col w-full min-h-screen h-full items-center">
      <NavBar onLogout={onLogout} />
      <div className="mt-5 w-11/12">
        <AllSocios />
        <div className="w-full flex items-center justify-end">
          <Button
            onClick={handleExtract}
            className="bg-blue-500 hover:bg-gray-900 w-auto mb-2 mx-2 text-white"
          >
            Extraer datos
          </Button>
        </div>
      </div>
      <Toaster />
    </div>
  );
}
