import { Form, Input, Button } from "antd";
import { Toaster } from "react-hot-toast";
import useFilters from "../hooks/useFilters";
import BackButton from "../components/BackButton";

export default function FormNewSocio() {
  const { onFinish } = useFilters();
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
