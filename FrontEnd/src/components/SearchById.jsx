import { Input } from "antd";
import useFilters from "../hooks/useFilters";
const { Search } = Input;
export default function SearchById() {
  const { searchById } = useFilters();
  return (
    <div className="bg-white rounded-lg w-full">
      <Search
        placeholder="Buscar por n° socio"
        onSearch={searchById}
        allowClear
      />
    </div>
  );
}
