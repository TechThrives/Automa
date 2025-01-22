import React, { useEffect, useState } from "react";
import RunOnce from "./RunOnce";
import { useWorkflow } from "../../context/WorkflowContext";
import { IoCloseSharp } from "react-icons/io5";
import SendMail from "./SendMail";
import useNodes from "../../hooks/useNodes";
import { Actions, Triggers } from "../../constants/ActionUtils";
import RunDaily from "./RunDaily";
import ReadSheet from "./ReadSheet";

const Modal = () => {
  const { selectedNode, setIsOpen, setSelectedNode } = useWorkflow();
  const { getPreviousConnectedNodes } = useNodes();
  const [previousNodesData, setPreviousNodesData] = useState({});

  useEffect(() => {
    if (selectedNode) {
      const previousNodes = getPreviousConnectedNodes(selectedNode.id);
      const previousNodeData = previousNodes.reduce((acc, node) => {
        return { ...acc, [node.name]: [ ...node.output ] };
      }, {});
      setPreviousNodesData(previousNodeData);
    }
  }, [selectedNode]);
  const handleClose = () => {
    setIsOpen(false);
    setSelectedNode(null);
  };
  return (
    <div className="fixed inset-0 z-40 flex flex-col items-center justify-center bg-black bg-opacity-80 p-4">
      <p className="text-white">
        {Object.entries(previousNodesData).length > 0 &&
          JSON.stringify(previousNodesData, null, 2)}
      </p>
      <div className="w-full max-w-sm rounded-lg bg-white shadow-xl">
        <IoCloseSharp className="float-right mr-4 mt-4" onClick={handleClose} />
        {selectedNode.type === Triggers.RUNONCE && <RunOnce />}
        {selectedNode.type === Triggers.RUNDAILY && <RunDaily />}
        {selectedNode.type === Actions.SENDMAIL && <SendMail />}
        {selectedNode.type === Actions.READSHEET && <ReadSheet />}
      </div>
    </div>
  );
};

export default Modal;
