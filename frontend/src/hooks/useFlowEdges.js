import { useCallback } from "react";
import { addEdge, useReactFlow } from "@xyflow/react";

const useFlowEdges = () => {
  const { setEdges } = useReactFlow();

  const onConnect = useCallback((params) => {
    params.type = "normalEdge";
    params.id = `${params.source}-${params.target}`;

    setEdges((eds) => addEdge({ ...params }, eds));
  }, []);

  const isValidConnection = useCallback((params) => {
    if (params.source !== params.target) return true;
  }, []);

  return {
    onConnect,
    isValidConnection,
  };
};

export default useFlowEdges;