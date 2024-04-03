import React from "react";
import { Button, Form, Input } from "antd";
import useFilters from "../hooks/useFilters";
import { Toaster } from "react-hot-toast";

export default function Login() {
  const { handleLogin } = useFilters();
  const onFinish = (values) => {
    handleLogin(values);
  };
  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };
  return (
    <Form
      className="bg-white p-8 rounded-xl flex items-center flex-col justify-center"
      name="basic"
      labelCol={{
        span: 8,
      }}
      wrapperCol={{
        span: 16,
      }}
      style={{
        maxWidth: 600,
      }}
      initialValues={{
        remember: true,
      }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      autoComplete="off"
    >
      <Form.Item
        label="Usuario"
        name="username"
        rules={[
          {
            required: true,
            message: "Ingrese un usuario",
          },
        ]}
        className="w-full"
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Contraseña"
        name="password"
        rules={[
          {
            required: true,
            message: "Ingrese una contraseña",
          },
        ]}
      >
        <Input.Password />
      </Form.Item>

      {/* <Form.Item
        name="remember"
        valuePropName="checked"
        wrapperCol={{
          offset: 8,
          span: 16,
        }}
        className="w-full"
      >
        <Checkbox>Recordar</Checkbox>
      </Form.Item> */}

      <Form.Item
        wrapperCol={{
          offset: 8,
          span: 16,
        }}
        className="w-full"
      >
        <Button type="primary" htmlType="submit" className="bg-blue-400">
          Iniciar sesion
        </Button>
      </Form.Item>
      <Toaster />
    </Form>
  );
}
