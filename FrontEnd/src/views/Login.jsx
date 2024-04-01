import React from "react";
import { Button, Checkbox, Form, Input } from "antd";
import { useNavigate } from "react-router-dom";
export default function Login({ onLogin }) {
  const navigate = useNavigate();
  const onFinish = (values) => {
    if (
      values.username === "prueba@gmail.com" &&
      values.password === "prueba12345"
    ) {
      onLogin();
      navigate("/inicio");
    } else {
      console.log("El usuario no existe o la contraseña se incorrecta");
    }
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
            message: "Ingrese un email!",
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
            message: "Ingrese una contrasseña!",
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
    </Form>
  );
}
