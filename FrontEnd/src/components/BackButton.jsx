import { ArrowLeftOutlined } from "@ant-design/icons";

export default function BackButton() {
  const handleGoBack = () => {
    window.history.back();
  };
  return (
    <div className="flex w-full items-center justify-center">
      <button onClick={handleGoBack}>
        <ArrowLeftOutlined /> Volver
      </button>
    </div>
  );
}
