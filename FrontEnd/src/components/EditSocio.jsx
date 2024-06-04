import React, { useEffect } from "react";
import { Button, Form, Input } from "antd";
import { useSelector } from "react-redux";
import { getFilterSocios } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";
import Categorias from "./Categorias"; // Ajusta la ruta si es necesario

export default function EditSocio() {
  const [form] = Form.useForm();
  const socio = useSelector(getFilterSocios);
  const { edit } = useFilters();

  useEffect(() => {
    form.setFieldsValue({
      id: socio.id,
      nombre: socio.nombre,
      apellido: socio.apellido,
      email: socio.email,
      dni: socio.dni,
      fecha_nacimiento: socio.fecha_nacimiento,
      categoria: socio.categoria,
    });
  }, [socio, form]);

  return (
    <div className="flex justify-center items-center w-11/12">
      <Form
        form={form}
        className="bg-white rounded-lg p-5"
        style={{ minWidth: "300px" }}
        onFinish={edit}
      >
        <Form.Item label="id" name="id">
          <span>{socio.id}</span>
        </Form.Item>
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
          rules={[{ required: true, message: "Por favor ingrese su apellido" }]}
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
          name="dni"
          label="Documento de identidad"
          rules={[
            {
              required: true,
              message: "Por favor ingrese su documento de identidad",
            },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="fecha_nacimiento"
          label="Fecha de nacimiento"
          rules={[{ required: true, message: "Por favor ingrese una fecha" }]}
        >
          <Input type="date" />
        </Form.Item>
        <Form.Item
          name="categoria"
          label="Categoria"
          rules={[
            { required: true, message: "Por favor ingrese su categoria" },
          ]}
        >
          <Categorias />
        </Form.Item>
        <div className="flex justify-center">
          <Form.Item>
            <Button className="bg-blue-600" type="primary" htmlType="submit">
              Registrar
            </Button>
          </Form.Item>
        </div>
      </Form>
    </div>
  );
}
