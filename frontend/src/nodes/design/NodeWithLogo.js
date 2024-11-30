import { memo } from "react";
import { Handle, Position, useReactFlow } from "@xyflow/react";

const NodeWithLogo = (props) => {
  const { nodeInfo, id } = props;
  const { setNodes } = useReactFlow();
  const { icon: Icon, text, iconColor } = nodeInfo;

  const onRemoveButtonClick = () => {
    setNodes((nodes) => nodes.filter((node) => node.id !== id));
  };

  return (
    <div className="flex justify-center items-center flex-col p-1 rounded-lg bg-white node">
      <Icon className={`text-xl ${iconColor}`} />
      <p className="text-[6px] font-bold text-gray-800 text-wrap w-12 text-center">
        {text}
      </p>

      <Handle type="target" className="!bg-teal-500" position={Position.Left} />
      <Handle
        type="source"
        className="!bg-lime-500"
        position={Position.Right}
      />
    </div>
  );
};

export default memo(NodeWithLogo);
