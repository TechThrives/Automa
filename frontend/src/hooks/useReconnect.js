import { useCallback } from "react";
import { reconnectEdge } from "@xyflow/react";
import { useWorkflow } from "../context/WorkflowContext";

export const useReconnect = () => {

  const { setEdges, edgeReconnectSuccessful} = useWorkflow();

  const onReconnectStart = useCallback(() => {
    edgeReconnectSuccessful.current = false;
  }, []);

  const onReconnect = useCallback(
    (oldEdge, newConnection) => {
      edgeReconnectSuccessful.current = true;
      setEdges((els) => reconnectEdge(oldEdge, newConnection, els));
    },
    [setEdges]
  );

  const onReconnectEnd = useCallback(
    (_, edge) => {
      if (!edgeReconnectSuccessful.current) {
        setEdges((eds) => eds.filter((e) => e.id !== edge.id));
      }

      edgeReconnectSuccessful.current = true;
    },
    [edgeReconnectSuccessful, setEdges]
  );

  return { onReconnectStart, onReconnect, onReconnectEnd };
};
