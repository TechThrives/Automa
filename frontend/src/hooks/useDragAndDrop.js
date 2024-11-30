import { useReactFlow } from "@xyflow/react";
import { useCallback } from "react";
import { useWorkflow } from "../context/WorkflowContext";

export const useDragAndDrop = () => {
  const { screenToFlowPosition, setNodes } = useReactFlow();
  const{ type, setSelectedNode, setIsOpen } = useWorkflow();

  const initNodeData = (type) => {
    switch (type) {
      case "youtubeInfo":
        return { url: "" };
      case "twitterInfo":
        return { profile: "" };
      default:
        return { label: "Node" };
    }
  };

  const initNodeOutput = (type) => {
    switch (type) {
      case "youtubeInfo":
        return { title: "", description: "", viewCount: "", likeCount: "", commentCount: "" };
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
        data: initNodeData(type),
        output: initNodeOutput(type),
      };

      setSelectedNode(newNode);
      setIsOpen(true);
    },
    [screenToFlowPosition, type, setNodes, setIsOpen]
  );

  return { onDragOver, onDrop };
};
