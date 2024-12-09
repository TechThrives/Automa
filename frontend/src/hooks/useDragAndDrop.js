import { useReactFlow } from "@xyflow/react";
import { useCallback } from "react";
import { useWorkflow } from "../context/WorkflowContext";
import { v4 } from 'uuid';

const useDragAndDrop = () => {
  const { screenToFlowPosition, setNodes } = useReactFlow();
  const{ dragNode, setSelectedNode, setIsOpen } = useWorkflow();

  const onDragOver = useCallback((event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  }, []);

  const onDrop = useCallback(
    (event) => {
      event.preventDefault();

      if (!dragNode) return;

      const position = screenToFlowPosition({
        x: event.clientX,
        y: event.clientY,
      });

      const newNode = {
        id: v4(),
        name: dragNode.name,
        type: dragNode.actionType,
        position,
        data: dragNode.data,
        output: dragNode.output,
      };

      setSelectedNode(newNode);
      setIsOpen(true);
    },
    [screenToFlowPosition, dragNode, setNodes, setIsOpen]
  );

  return { onDragOver, onDrop };
};

export default useDragAndDrop;
