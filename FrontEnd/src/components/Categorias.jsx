import React from "react";
import { DownOutlined } from "@ant-design/icons";
import { Dropdown, Space, Menu } from "antd";
import useFilters from "../hooks/useFilters";

export default function Categorias() {
  const { handleMenuClick } = useFilters();
  const items = [
    { label: "Primera", key: "Primera" },
    { label: "Inferiores", key: "Inferiores" },
    { label: "5ta", key: "5ta" },
    { label: "6ta", key: "6ta" },
    { label: "7ta", key: "7ta" },
    { label: "8ta", key: "8ta" },
    { label: "9ta", key: "9ta" },
    { label: "10ta", key: "10ta" },
  ];

  const menu = (
    <Menu onClick={(e) => handleMenuClick(e.key)}>
      {items.map((item) => (
        <Menu.Item key={item.key}>{item.label}</Menu.Item>
      ))}
    </Menu>
  );

  return (
    <Dropdown
      overlay={menu}
      trigger={["click"]}
      className="bg-blue-500 p-1 rounded-lg w-auto mb-2 mx-2 text-sm cursor-pointer"
    >
      <a onClick={(e) => e.preventDefault()} className="px-2 py-[6px]">
        <Space>
          Categorias
          <DownOutlined className="w-3 h-3 mr-1" />
        </Space>
      </a>
    </Dropdown>
  );
}
