import axios from "axios";
import {useSelector, useDispatch} from "react-redux";
import { setAllSocios } from "../redux/setSocios";

const useFilters = () => {
    const dispatch = useDispatch();
    
    const handleAllServices = async () => {
        try {
            const { data } = await axios("http://localhost:8080/adm_clubes/clientes");
            if (data) {
                dispatch(setAllSocios(data));
            }
        } catch (error) {
            console.log(error.message);
        }
    };
    return {
        handleAllServices,
    }
}
export default useFilters