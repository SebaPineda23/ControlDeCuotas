import { Button, Checkbox, Form, Input } from "antd";
import React from "react";
import { useSelector } from "react-redux";
import { getFilterSocios } from "../redux/setSocios";
import useFilters from "../hooks/useFilters";

export default function EditSocio() {
  const infoSocio = useSelector(getFilterSocios);
  const { edit } = useFilters();

  return (
    <div className="flex justify-center items-center w-11/12">
      <Form
        className="bg-white rounded-lg p-5"
        style={{ minWidth: "300px" }}
        onFinish={edit}
        initialValues={{
          id: infoSocio.id,
          nombre: infoSocio.nombre,
          apellido: infoSocio.apellido,
          dni: infoSocio.dni,
          fecha_nacimiento: infoSocio.fechaDeNacimiento,
        }}
      >
        <Form.Item label="id" name="id">
          {infoSocio.id}
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
