import AllSocios from "./views/AllSocios";
import FormNewSocio from "./views/FormNewSocio";
import Home from "./views/Home";
import { Routes, Route } from "react-router-dom";
import Pago from "./views/Pago";
import FilteredSociosById from "./views/FilteredSociosById";
import FilteredSociosByName from "./views/FilteredSociosByName";
function App() {
  return (
    <div className="bg-hero-pattern-2 object-cover w-full h-screen flex items-center justify-center">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/nuevoSocio" element={<FormNewSocio />} />
        <Route path="/allSocios" element={<AllSocios />} />
        <Route path="/pago" element={<Pago />} />
        <Route path="/socio/:id" element={<FilteredSociosById />} />
        <Route path="/socio/name/:value" element={<FilteredSociosByName />} />
      </Routes>
    </div>
  );
}

export default App;
