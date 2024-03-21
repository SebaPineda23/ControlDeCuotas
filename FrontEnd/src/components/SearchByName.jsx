import { Input } from "antd";
import useFilters from "../hooks/useFilters";
const { Search } = Input;
export default function SearchByName() {
  const { searchByName } = useFilters();
  return (
    <div className="bg-white rounded-lg w-full">
      <Search
        placeholder="Buscar por nombre/apellido"
        onSearch={searchByName}
        allowClear
        rules={[{ required: true, message: "Por favor ingrese un dato" }]}
      />
    </div>
  );
}
