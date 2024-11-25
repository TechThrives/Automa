import { useCallback } from "react";
import { applyNodeChanges } from "@xyflow/react";
import { useWorkflow } from "../context/WorkflowContext";

export const useFlowNodes = () => {
  const { setNodes } = useWorkflow();

  const onNodesChange = useCallback(
    (changes) => setNodes((nds) => applyNodeChanges(changes, nds)),
    []
  );

  return { onNodesChange };
};
