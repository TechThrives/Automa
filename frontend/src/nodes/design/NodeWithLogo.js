import { memo } from "react";
import { Handle, Position } from "@xyflow/react";

const NodeWithLogo = ({ nodeInfo, nodeData }) => {
  const { icon: Icon, text, iconColor} = nodeInfo;
  return (
    <>
      <div className="flex justify-center items-center flex-col bg-white p-1 rounded-lg">
      <Icon className={`text-xl ${iconColor}`} />
        <p className="text-[6px] font-bold text-gray-800 text-wrap w-12 text-center">{text}</p>
      </div>

      <Handle type="target" className="!bg-teal-500" position={Position.Left} />
      <Handle type="source" className="!bg-lime-500" position={Position.Right} />
    </>
  );
};

export default memo(NodeWithLogo);
