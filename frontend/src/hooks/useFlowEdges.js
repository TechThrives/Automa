import { useCallback } from "react";
import { addEdge, useReactFlow } from "@xyflow/react";

export const useFlowEdges = () => {
  const { setEdges, getNode } = useReactFlow();

  const onConnect = useCallback((params) => {
    const sourceNode = getNode(params.source);
    params.type = "normalEdge";
    params.id = `${params.source}-${params.target}`;

    setEdges((eds) => addEdge({ ...params, input: sourceNode.output }, eds));
  }, []);

  const isValidConnection = useCallback((params) => {
    if (params.source !== params.target) return true;
  }, []);

  return {
    onConnect,
    isValidConnection,
  };
};
