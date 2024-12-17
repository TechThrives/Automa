import React, { useEffect, useState } from "react";
import RunOnce from "./RunOnce";
import { useWorkflow } from "../../context/WorkflowContext";
import { IoCloseSharp } from "react-icons/io5";
import SendMail from "./SendMail";
import useNodes from "../../hooks/useNodes";
import { Actions, Triggers } from "../../constants/ActionUtils";
import RunDaily from "./RunDaily";

const Modal = () => {
  const { selectedNode, setIsOpen, setSelectedNode } = useWorkflow();
  const { getPreviousConnectedNodes } = useNodes();
  const [previousNodesData, setPreviousNodesData] = useState({});

  useEffect(() => {
    if (selectedNode) {
      const previousNodes = getPreviousConnectedNodes(selectedNode.id);
      const previousNodeData = previousNodes.reduce((acc, node) => {
        return { ...acc, [node.name]: node.output };
      }, {});
      setPreviousNodesData(previousNodeData);
    }
  }, [selectedNode]);
  const handleClose = () => {
    setIsOpen(false);
    setSelectedNode(null);
  };
  return (
    <div className="fixed inset-0 bg-black bg-opacity-80 flex flex-col items-center justify-center p-4 z-40">
      <p className="text-white">
        {Object.entries(previousNodesData).length > 0 &&
          JSON.stringify(previousNodesData, null, 2)}
      </p>
      <div className="bg-white rounded-lg shadow-xl w-full max-w-sm">
        <IoCloseSharp className="mt-4 mr-4 float-right" onClick={handleClose} />
        {selectedNode.type === Triggers.RUNONCE && <RunOnce />}
        {selectedNode.type === Triggers.RUNDAILY && <RunDaily />}
        {selectedNode.type === Actions.SENDMAIL && <SendMail />}
      </div>
    </div>
  );
};

export default Modal;
