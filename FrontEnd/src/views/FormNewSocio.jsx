import React, { useState } from "react";
import { Form, InputNumber, Button, Input, Select } from "antd";
import { Toaster } from "react-hot-toast";
import useFilters from "../hooks/useFilters";
import BackButton from "../components/BackButton";

const { Option } = Select;

export default function FormNewSocio() {
  const { onFinish } = useFilters();
  const [categoria, setCategoria] = useState(null);

  const handleCategoriaChange = (value) => {
    setCategoria(value);
  };

  const items = [
    { label: "Primera", value: "Primera" },
    { label: "Inferiores", value: "Inferiores" },
    { label: "5ta", value: "5ta" },
    { label: "6ta", value: "6ta" },
    { label: "7ta", value: "7ta" },
    { label: "8ta", value: "8ta" },
    { label: "9ta", value: "9ta" },
    { label: "10ta", value: "10ta" },
    { label: "Inferiores", value: "Inferiores" },
  ];

  return (
    <>
      <div className="flex justify-center items-center w-11/12">
        <Form
          className="bg-white rounded-lg p-5"
          style={{ minWidth: "300px" }}
          onFinish={onFinish}
        >
          <Form.Item
            name="nombre"
            label="Nombre"
            rules={[{ required: true, message: "Por favor ingrese su nombre" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="apellido"
            label="Apellido"
            rules={[
              { required: true, message: "Por favor ingrese su apellido" },
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="email"
            label="Email"
            rules={[{ required: true, message: "Por favor ingrese su email" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="categoria"
            label="Categoria"
            rules={[
              { required: true, message: "Por favor ingrese su categoria" },
            ]}
          >
            <Select
              value={categoria}
              onChange={handleCategoriaChange}
              placeholder="Seleccione una categoría"
            >
              {items.map((item) => (
                <Option key={item.value} value={item.value}>
                  {item.label}
                </Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="dni"
            label="Documento de identidad"
            rules={[
              {
                required: true,
                message: "Por favor ingrese su documento de identidad",
              },
              {
                pattern: /^[0-9]+$/,
                message: "Por favor ingrese solo números",
              },
            ]}
          >
            <InputNumber className="w-full" />
          </Form.Item>
          <Form.Item
            name="fecha_nacimiento"
            label="Fecha de nacimiento"
            rules={[{ required: true, message: "Por favor ingrese una fecha" }]}
          >
            <Input type="date" />
          </Form.Item>
          <div className="flex justify-center">
            <div className="w-24 self-start p-1 rounded-2xl">
              <BackButton />
            </div>
            <Form.Item>
              <Button className="bg-blue-600" type="primary" htmlType="submit">
                Registrar
              </Button>
            </Form.Item>
          </div>
        </Form>
      </div>
      <Toaster position="top-center" />
    </>
  );
}
