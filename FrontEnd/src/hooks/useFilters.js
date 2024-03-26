import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import {
  getAllSocios,
  setAllSocios,
  setFilterSocios,
} from "../redux/setSocios";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";


const useFilters = () => {
  const dispatch = useDispatch();
  const notificarExito = (mensaje) => toast.success(mensaje);
  const notificarError = (error) => toast.error(`${error.response.data}`);
  const notifyError = (mensaje) => toast.error(mensaje);
  const socios = useSelector(getAllSocios);

  const navigate = useNavigate();

  const handleAllServices = async () => {
    try {
      const { data } = await axios.get(
        "https://controldecuotas.onrender.com/clientes"
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
          "https://controldecuotas.onrender.com/clientes/" + value
        );
        if (response) {
          const dataAsArray = [{ ...response.data }];
          dispatch(setFilterSocios(dataAsArray));
          navigate(`/socio/${value}`);
        }
      } catch (error) {
        notificarError(error);
      }
    } else {
      notifyError("El campo de búsqueda está vacío");
    }
  };
  const searchByName = async (value) => {
    if (value.trim() !== "") {
      try {
        const response = await axios.get(
          "https://controldecuotas.onrender.com/buscarCliente?letras=" + value
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
        "https://controldecuotas.onrender.com/clientes",
        values
      );
      if (response.data) {
        notificarExito("Usuario creado con exito");
        setTimeout(() => {
          navigate("/");
        }, 2000);
      }
    } catch (error) {
      notificarError(error);
    }
  };
  const deleteSocios = async (value) => {
    try {
      await axios.delete(
        "https://controldecuotas.onrender.com/clientes/" + value.id
      );
      const updatedSocios = socios.filter((socio) => socio.id !== value.id);
      dispatch(setAllSocios(updatedSocios));
      notificarExito("Usuario eliminado con éxito");
    } catch (error) {
      notificarError(error);
    }
  };
  const edit = async (values) => {
    console.log(values);
    try {
      const response = await axios.put(
        "https://controldecuotas.onrender.com/clientes/" + values.id,
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
  return {
    handleAllServices,
    searchById,
    searchByName,
    notificarExito,
    notificarError,
    onFinish,
    deleteSocios,
    edit,
  };
};
export default useFilters;
