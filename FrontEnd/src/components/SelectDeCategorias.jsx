import { Select } from "antd";
import React from "react";

export default function SelectDeCategorias({ value, onChange }) {
  const { Option } = Select;
  const items = [
    { label: "Primera", value: "Primera" },
    { label: "Inferiores", value: "Inferiores" },
    { label: "5ta", value: "5ta" },
    { label: "6ta", value: "6ta" },
    { label: "7ma", value: "7ma" },
    { label: "8va", value: "8va" },
    { label: "9na", value: "9na" },
    { label: "10ma", value: "10ma" },
    { label: "Infantiles", key: "Infantiles" },
  ];
  return (
    <Select
      value={value}
      onChange={onChange}
      placeholder="Seleccione una categoría"
    >
      {items.map((item, index) => (
        <Option key={index} value={item.value}>
          {item.label}
        </Option>
      ))}
    </Select>
  );
}
