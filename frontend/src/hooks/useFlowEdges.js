import { useCallback } from "react";
import { applyEdgeChanges, addEdge } from "@xyflow/react";
import { MarkerType } from "@xyflow/react";
import { useWorkflow } from "../context/WorkflowContext";

export const useFlowEdges = () => {
  const { setEdges } = useWorkflow();

  const onEdgesChange = useCallback(
    (changes) => setEdges((eds) => applyEdgeChanges(changes, eds)),
    []
  );

  const onConnect = useCallback(
    (params) =>
      setEdges((eds) =>
        addEdge({ ...params, markerEnd: { type: MarkerType.Arrow } }, eds)
      ),
    []
  );

  return {
    onEdgesChange,
    onConnect,
  };
};
