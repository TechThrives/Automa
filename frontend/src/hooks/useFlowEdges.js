import { useCallback } from "react";
import { addEdge, useReactFlow } from "@xyflow/react";
import { v4 } from 'uuid';

const useFlowEdges = () => {
  const { setEdges } = useReactFlow();

  const onConnect = useCallback((params) => {
    params.type = "NORMAL";
    params.id = v4();

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