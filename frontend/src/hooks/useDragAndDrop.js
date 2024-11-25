import { useReactFlow } from "@xyflow/react";
import { useCallback } from "react";
import { useWorkflow } from "../context/WorkflowContext";

export const useDragAndDrop = () => {
  const { screenToFlowPosition } = useReactFlow();
  const{ type, setSelectedNode, setIsOpen, setNodes } = useWorkflow();

  const getNodeData = (type) => {
    switch (type) {
      case "youtubeInfo":
        return { url: "" };
      case "twitterInfo":
        return { profile: "" };
      default:
        return { label: "Node" };
    }
  };

  const onDragOver = useCallback((event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  }, []);

  const onDrop = useCallback(
    (event) => {
      event.preventDefault();

      if (!type) return;

      const position = screenToFlowPosition({
        x: event.clientX,
        y: event.clientY,
      });

      const newNode = {
        id: `node-${Date.now()}`,
        type,
        position,
        nodeData: getNodeData(type),
      };

      setNodes((nds) => nds.concat(newNode));
      setSelectedNode(newNode);
      setIsOpen(true);
    },
    [screenToFlowPosition, type, setNodes, setIsOpen]
  );

  return { onDragOver, onDrop };
};
