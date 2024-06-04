import React from "react";
import { DownOutlined } from "@ant-design/icons";
import { Dropdown, Space } from "antd";
export default function Categorias() {
  const items = [
    {
      label: <p>Primera</p>,
      key: "0",
    },
    {
      label: <p>Inferiores</p>,
      key: "1",
    },
    {
      label: <p>5ta</p>,
      key: "2",
    },
    {
      label: <p>6ta</p>,
      key: "3",
    },
    {
      label: <p>7ta</p>,
      key: "4",
    },
    {
      label: <p>8ta</p>,
      key: "5",
    },
    {
      label: <p>9ta</p>,
      key: "6",
    },
    {
      label: <p>10ta</p>,
      key: "7",
    },
    {
      label: <p>Inferiores</p>,
      key: "8",
    },
  ];
  return (
    <Dropdown
      menu={{
        items,
      }}
      trigger={["click"]}
      className="bg-blue-500 p-1 rounded-lg w-auto mb-2 mx-2 text-sm cursor-pointer"
    >
      <a onClick={(e) => e.preventDefault()} className="px-2 py-[6px]">
        <Space>
          Categorias
          <DownOutlined className="w-3 h-3" />
        </Space>
      </a>
    </Dropdown>
  );
}
