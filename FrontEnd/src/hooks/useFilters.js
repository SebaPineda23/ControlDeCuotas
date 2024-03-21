import axios from "axios";
import { useDispatch} from "react-redux";
import { setAllSocios,setFilterSocios } from "../redux/setSocios";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

const useFilters = () => {
    const dispatch = useDispatch();
    const notificarExito = (mensaje) => toast.success(mensaje);
    const notificarError = (error) => toast.error(`${error.response.data}`);
    const notifyError = (mensaje) => toast.error(mensaje);

    const navigate = useNavigate()

    const handleAllServices = async () => {
        try {
            const { data } = await axios.get("http://localhost:8080/adm_clubes/clientes");
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
                    "http://localhost:8080/adm_clubes/clientes/" + value
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
                const response = await axios.get("http://localhost:8080/adm_clubes/clientes/buscarCliente?letras="+value);
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
    }
    
    
    return {
        handleAllServices,
        searchById,
        searchByName,
        notificarExito,
        notificarError,
    }
}
export default useFilters