import { Input } from "antd";
const { Search } = Input;

const onSearch = (value, _e) => console.log(value);
export default function SearchBar() {
  return (
    <div className="bg-white rounded-lg">
      <Search
        placeholder="Buscar un socio"
        onSearch={onSearch}
        allowClear
        style={{
          width: 200,
        }}
      />
    </div>
  );
}
