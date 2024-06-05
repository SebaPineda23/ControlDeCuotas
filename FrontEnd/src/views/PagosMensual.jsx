import React from "react";
import { Button, Form, Input } from "antd";
import SelectDeCategorias from "../components/SelectDeCategorias";
import BackButton from "../components/BackButton";
import useFilters from "../hooks/useFilters";
import { Toaster } from "react-hot-toast";

export default function PagosMensual() {
  const { handlePagos } = useFilters();

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };
  return (
    <div className="flex flex-col bg-white rounded-md w-1/3 items-center justify-center">
      <div className="self-start">
        <BackButton />
      </div>
      <Form
        name="basic"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        style={{ maxWidth: 600 }}
        initialValues={{ remember: true }}
        onFinish={handlePagos}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
        className="bg-white p-5"
      >
        <Form.Item label="Mes" name="mes">
          <Input />
        </Form.Item>
        <Form.Item label="Año" name="año">
          <Input placeholder="Por ej: 2024" />
        </Form.Item>
        <Form.Item label="Categoria" name="categoria">
          <SelectDeCategorias />
        </Form.Item>
        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          <Button
            type="primary"
            htmlType="submit"
            className="text-black bg-blue-200"
          >
            Consultar
          </Button>
        </Form.Item>
      </Form>
      <Toaster />
    </div>
  );
}
