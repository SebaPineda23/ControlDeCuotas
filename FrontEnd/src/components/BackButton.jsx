import { ArrowLeftOutlined } from "@ant-design/icons";

export default function BackButton() {
  const handleGoBack = () => {
    window.history.back();
  };
  return (
    <div className="flex w-20 items-start m-1 justify-center p-1">
      <button onClick={handleGoBack}>
        <ArrowLeftOutlined /> Volver
      </button>
    </div>
  );
}
