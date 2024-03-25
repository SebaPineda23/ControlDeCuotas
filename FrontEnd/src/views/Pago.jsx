import { Form, Input, Button } from "antd";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import BackButton from "../components/BackButton";
export default function Pago() {
  const notify = () => toast.success("Pago registrado con éxito");
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      const response = await axios.post(
        `http://localhost:8080/adm_clubes/pago_mensuales/${values.cliente_id}/pagos`,
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
    <div className="flex justify-center">
      <Form
        className="bg-white rounded-lg p-5"
        style={{ minWidth: "300px" }}
        onFinish={onFinish}
      >
        <Form.Item
          name="cliente_id"
          label="Numero de socio"
          rules={[
            { required: true, message: "Por favor ingrese el numero de socio" },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="monto"
          label="Monto"
          rules={[{ required: true, message: "Por favor ingrese el monto" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="fecha"
          label="Fecha de Pago"
          rules={[{ required: true, message: "Por favor ingrese una fecha" }]}
        >
          <Input type="date" />
        </Form.Item>
        <div className="flex justify-center flex-row w-full">
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
      <Toaster position="top-center" />
    </div>
  );
}
