import { memo } from "react";
import { Handle, Position } from "@xyflow/react";
import { ActionIcon, isTrigger } from "../../constants/ActionUtils";

const NodeWithLogo = (props) => {
  const { id, type } = props;

  return (
    <div className="node flex flex-col items-center justify-center rounded-lg bg-white p-1">
      <ActionIcon actionType={type} />
      <p className="w-12 text-wrap text-center text-[6px] font-bold text-gray-800">
        {type}
      </p>

      {!isTrigger(type) && (
        <Handle
          type="target"
          className="!bg-teal-500"
          position={Position.Left}
        />
      )}
      <Handle
        type="source"
        className="!bg-lime-500"
        position={Position.Right}
      />
    </div>
  );
};

export default memo(NodeWithLogo);
