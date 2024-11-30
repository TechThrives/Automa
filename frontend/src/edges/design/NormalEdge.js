import React from "react";
import {
  BaseEdge,
  EdgeLabelRenderer,
  getBezierPath,
  useReactFlow,
} from "@xyflow/react";
import { MdClose } from "react-icons/md";

const NormalEdge = (props) => {
  const {
    id,
    sourceX,
    sourceY,
    sourcePosition,
    targetX,
    targetY,
    targetPosition,
  } = props;
  const { setEdges } = useReactFlow();
  const [edgePath, labelX, labelY] = getBezierPath({
    sourceX,
    sourceY,
    sourcePosition,
    targetX,
    targetY,
    targetPosition,
  });
  

  const onRemoveButtonClick = () => {
    setEdges((edges) => edges.filter((edge) => edge.id !== id));
  };

  return (
    <>
      <BaseEdge
      className="edge"
        {...props}
        path={edgePath}
      />
      <EdgeLabelRenderer>
        <div
          className="absolute group"
          style={{
            transform: `translate(-50%, -50%) translate(${labelX}px,${labelY}px)`,
          }}
        >
          <MdClose
            onClick={onRemoveButtonClick}
            className="text-red-600 font-semibold bg-white rounded-full w-3 h-3 cursor-pointer"
            pointerEvents={"all"}
          />
        </div>
      </EdgeLabelRenderer>
    </>
  );
};

export default NormalEdge;
