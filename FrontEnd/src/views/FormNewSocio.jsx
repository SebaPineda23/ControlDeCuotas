import { Form, Input, Button } from "antd";
import { useNavigate } from "react-router-dom";
import toast, { Toaster } from "react-hot-toast";
import axios from "axios";

export default function FormNewSocio() {
  const notify = () => toast.success("Socio registrado con éxito");
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/adm_clubes/clientes",
        values
      );
      if (response.data) {
        notify();
        setTimeout(() => {
          navigate("/");
        }, 2000);
      }
    } catch (error) {
      console.log("Algo falló", error);
    }
  };

  return (
    <>
      <div className="flex justify-center">
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
