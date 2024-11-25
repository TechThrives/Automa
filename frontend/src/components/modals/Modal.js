import React, { useState } from "react";
import YoutubeInfo from "./YoutubeInfo";
import { useWorkflow } from "../../context/WorkflowContext";

const Modal = () => {
  const { selectedNode } = useWorkflow();
  return (
    <div className="fixed inset-0 bg-black bg-opacity-80 flex items-center justify-center p-4 z-40">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-sm">
        {selectedNode.type === "youtubeInfo" && <YoutubeInfo />}
      </div>
    </div>
  );
};

export default Modal;
