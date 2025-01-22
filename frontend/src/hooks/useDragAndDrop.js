import { useReactFlow } from "@xyflow/react";
import { useCallback } from "react";
import { useWorkflow } from "../context/WorkflowContext";
import { v4 } from "uuid";
import { isAction, isTrigger } from "../constants/ActionUtils";
import toast from "react-hot-toast";
import useNodes from "./useNodes";

const useDragAndDrop = () => {
  const { screenToFlowPosition, setNodes } = useReactFlow();
  const { getName } = useNodes();
  const { dragNode, setSelectedNode, setIsOpen, hasTrigger } = useWorkflow();

  const onDragOver = useCallback((event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  }, []);

  const onDrop = useCallback(
    (event) => {
      event.preventDefault();

      if (!dragNode) return;

      if (hasTrigger && isTrigger(dragNode.actionType)) {
        toast.error("Workflow already has a trigger");
        return;
      }

      if (!hasTrigger && isAction(dragNode.actionType)) {
        toast.error("Workflow must have a trigger before adding actions");
        return;
      }

      const position = screenToFlowPosition({
        x: event.clientX,
        y: event.clientY,
      });

      const newNode = {
        id: v4(),
        name: getName(dragNode.actionType),
        type: dragNode.actionType,
        position,
        data: dragNode.data,
        output: dragNode.output,
      };

      setSelectedNode(newNode);
      setIsOpen(true);
    },
    [screenToFlowPosition, dragNode, setNodes, setIsOpen],
  );

  return { onDragOver, onDrop };
};

export default useDragAndDrop;
