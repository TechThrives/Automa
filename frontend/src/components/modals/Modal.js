import React, { useState } from "react";
import YoutubeInfo from "./YoutubeInfo";
import { useWorkflow } from "../../context/WorkflowContext";
import { IoCloseSharp } from "react-icons/io5";

const Modal = () => {
  const { selectedNode, setIsOpen, setSelectedNode } = useWorkflow();
  const handleClose = () => {
    setIsOpen(false);
    setSelectedNode(null);
  };
  return (
    <div className="fixed inset-0 bg-black bg-opacity-80 flex items-center justify-center p-4 z-40">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-sm">
        <IoCloseSharp className="mt-4 mr-4 float-right" onClick={handleClose}/>
        {selectedNode.type === "youtubeInfo" && <YoutubeInfo />}
      </div>
    </div>
  );
};

export default Modal;
