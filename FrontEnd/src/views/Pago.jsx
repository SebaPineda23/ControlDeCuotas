import { Form, Input, Button } from "antd";
import { Toaster } from "react-hot-toast";
import BackButton from "../components/BackButton";
import useFilters from "../hooks/useFilters";

export default function Pago() {
  const { pago } = useFilters();

  const handleSubmit = (values) => {
    pago(values);
  };

  return (
    <div className="flex justify-center">
      <Form
        className="bg-white rounded-lg p-10"
        style={{ minWidth: "300px" }}
        onFinish={handleSubmit}
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
