import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import {
  getAllSocios,
  setAllPagos,
  setAllSocios,
  setFilterSocios,
  setHistorial,
} from "../redux/setSocios";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  login,
  logout,
  setAccess,
  setPassword,
  setUsername,
} from "../redux/setUsuario";

const useFilters = () => {
  const dispatch = useDispatch();
  const notificarExito = (mensaje) => toast.success(mensaje);
  const notificarError = (error) => toast.error(`${error.response.data}`);
  const notifyError = (mensaje) => toast.error(mensaje);
  const socios = useSelector(getAllSocios);
  const navigate = useNavigate();
  const cambioDeEstado = async (id, estado) => {
    let contador = 0;

    const realizarPeticion = async () => {
      try {
        const response = await axios.put(
          `https://adm_clubes/clientes/cliente/${id}/estadoNoPago`
        );
        console.log("Petición realizada con éxito:", response.data);
      } catch (error) {
        console.error("Error al realizar la petición:", error);
      }
    };

    const aumentarContador = () => {
      contador++;
      console.log("Contador:", contador);
      if (contador % 32 === 0 && estado === "PAGO") {
        realizarPeticion();
      }
    };

    const intervalo = setInterval(aumentarContador, 86400000);

    if (!intervalo && estado === "PAGO") {
      aumentarContador();
    }
  };

  const handleAllSocios = async () => {
    try {
      const { data } = await axios.get(
        "https://controldecuotas.onrender.com/adm_clubes/clientes"
      );
      dispatch(setAllSocios(data));
      notificarExito("Socios cargados exitosamente");
    } catch (error) {
      notificarError(error);
    }
  };
  const searchById = async (value) => {
    if (value.trim() !== "") {
      try {
        const response = await axios.get(
          "https://controldecuotas.onrender.com/adm_clubes/clientes/" + value
        );
        if (response) {
          const dataAsArray = [{ ...response.data }];
          dispatch(setFilterSocios(dataAsArray));
          navigate(`/socio/${value}`);
        }
      } catch (error) {
        if (error.response.status === 400) {
          notifyError("No se encuentra un usuario con esa Id");
        }
      }
    } else {
      notifyError("El campo de búsqueda está vacío");
    }
  };
  const searchByName = async (value) => {
    if (value.trim() !== "") {
      try {
        const response = await axios.get(
          "https://controldecuotas.onrender.com/adm_clubes/clientes/buscarCliente?letras=" +
            value
        );
        if (response) {
          dispatch(setFilterSocios(response.data));
          navigate(`/socios/name/${value}`);
        }
      } catch (error) {
        notifyError("Ocurrió un error al realizar la búsqueda");
      }
    } else {
      notifyError("El campo de búsqueda está vacío");
    }
  };
  const onFinish = async (values) => {
    try {
      const response = await axios.post(
        "https://controldecuotas.onrender.com/adm_clubes/clientes",
        values
      );
      if (response.data) {
        notificarExito("Usuario creado con exito");
        setTimeout(() => {
          navigate("/inicio");
        }, 2000);
      }
    } catch (error) {
      notifyError(error.response.data.error);
    }
  };
  const handleLogin = async (values) => {
    try {
      const response = await axios.post(
        "https://controldecuotas.onrender.com/adm_clubes/usuario/login",
        values
      );
      dispatch(setUsername(response.data.username));
      dispatch(setPassword(response.data.password));
      dispatch(setAccess(response.data.access));
      dispatch(login());
    } catch (error) {
      console.log(error);
      notificarError(error);
    }
  };
  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
  };
  const pago = async (values) => {
    try {
      const response = await axios.post(
        `https://controldecuotas.onrender.com/adm_clubes/pago_mensuales/${values.cliente_id}/pagos`,
        values
      );
      if (response.data) {
        notificarExito("Pago realizado con exito");
        setTimeout(() => {
          cambioDeEstado(values.cliente_id, "PAGO");
          navigate("/inicio");
        }, 2000);
      }
    } catch (error) {
      notificarError(error);
    }
  };
  const deleteSocios = async (value) => {
    try {
      await axios.delete(
        "https://controldecuotas.onrender.com/adm_clubes/clientes/" + value.id
      );
      const updatedSocios = socios.filter((socio) => socio.id !== value.id);
      dispatch(setAllSocios(updatedSocios));
      notificarExito("Usuario eliminado con éxito");
    } catch (error) {
      notificarError(error);
    }
  };
  const edit = async (values) => {
    try {
      const response = await axios.put(
        "https://controldecuotas.onrender.com/adm_clubes/clientes/" + values.id,
        values
      );
      if (response.data) {
        notificarExito("Usuario editado con éxito");
      }
      setTimeout(() => {
        window.location.reload();
      }, 200);
    } catch (error) {
      notificarError(error);
    }
  };
  const historialDePago = async (value) => {
    try {
      const response = await axios.get(
        `https://controldecuotas.onrender.com/adm_clubes/pago_mensuales/cliente/${value.id}/pagos`
      );
      if (response.data) {
        dispatch(setHistorial(response.data));
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        notificarError(error);
        dispatch(setHistorial([]));
      } else {
        notificarError(error);
      }
    }
  };
  const allPagos = async () => {
    try {
      const response = await axios.get(
        "https://controldecuotas.onrender.com/adm_clubes/pago_mensuales"
      );
      if (response) {
        dispatch(setAllPagos(response.data));
      }
    } catch (error) {
      notificarError(error);
    }
  };
  return {
    handleAllSocios,
    searchById,
    searchByName,
    notificarExito,
    notificarError,
    onFinish,
    deleteSocios,
    edit,
    historialDePago,
    pago,
    allPagos,
    handleLogin,
    handleLogout,
  };
};
export default useFilters;
