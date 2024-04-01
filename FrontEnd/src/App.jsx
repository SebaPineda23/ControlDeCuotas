import FormNewSocio from "./views/FormNewSocio";
import Home from "./views/Home";
import { Routes, Route, Navigate } from "react-router-dom";
import Pago from "./views/Pago";
import FilteredSociosById from "./views/FilteredSociosById";
import FilteredSociosByName from "./views/FilteredSociosByName";
import Login from "./views/Login";
import { useState } from "react";
function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };
  const handleLogout = () => {
    setIsLoggedIn(false);
  };
  return (
    <div className="bg-hero-pattern-2 object-cover w-full h-full min-h-screen flex items-center justify-center">
      <Routes>
        <Route
          path="/"
          element={
            isLoggedIn ? (
              <Navigate to="/inicio" />
            ) : (
              <Login onLogin={handleLogin} />
            )
          }
        />
        <Route
          path="/inicio"
          element={
            isLoggedIn ? <Home onLogout={handleLogout} /> : <Navigate to="/" />
          }
        />
        <Route
          path="/nuevoSocio"
          element={isLoggedIn ? <FormNewSocio /> : <Navigate to="/" />}
        />
        <Route
          path="/pago"
          element={isLoggedIn ? <Pago /> : <Navigate to="/" />}
        />
        <Route
          path="/socio/:id"
          element={isLoggedIn ? <FilteredSociosById /> : <Navigate to="/" />}
        />
        <Route
          path="/socios/name/:value"
          element={isLoggedIn ? <FilteredSociosByName /> : <Navigate to="/" />}
        />
      </Routes>
    </div>
  );
}

export default App;
