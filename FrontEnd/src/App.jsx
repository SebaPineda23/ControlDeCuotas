import FormNewSocio from "./views/FormNewSocio";
import Home from "./views/Home";
import { Routes, Route, Navigate } from "react-router-dom";
import Pago from "./views/Pago";
import FilteredSociosById from "./views/FilteredSociosById";
import FilteredSociosByName from "./views/FilteredSociosByName";
import Login from "./views/Login";
import { useSelector } from "react-redux";
import { getAccess } from "./redux/setUsuario";
function App() {
  const access = useSelector(getAccess);
  return (
    <div className="bg-hero-pattern-2 bg-cover bg-center w-full h-full min-h-screen flex items-center justify-center">
      <Routes>
        <Route
          path="/"
          element={access === true ? <Navigate to="/inicio" /> : <Login />}
        />
        <Route
          path="/inicio"
          element={access === true ? <Home /> : <Navigate to="/" />}
        />
        <Route
          path="/nuevoSocio"
          element={access === true ? <FormNewSocio /> : <Navigate to="/" />}
        />
        <Route
          path="/pago"
          element={access === true ? <Pago /> : <Navigate to="/" />}
        />
        <Route
          path="/socio/:id"
          element={
            access === true ? <FilteredSociosById /> : <Navigate to="/" />
          }
        />
        <Route
          path="/socios/name/:value"
          element={
            access === true ? <FilteredSociosByName /> : <Navigate to="/" />
          }
        />
      </Routes>
    </div>
  );
}

export default App;
