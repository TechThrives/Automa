import React from "react";
import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  ReactFlowProvider,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import { nodeTypes } from "../../nodes/NodeTypes";
import { useFlowNodes } from "../../hooks/useFlowNodes";
import { useFlowEdges } from "../../hooks/useFlowEdges";
import { useDragAndDrop } from "../../hooks/useDragAndDrop";
import { useNodeConnections } from "../../hooks/useNodeConnections";
import { useReconnect } from "../../hooks/useReconnect";
import { WorkflowProvider, useWorkflow } from "../../context/WorkflowContext";
import Sidebar from "../../components/Sidebar";
import Modal from "../../components/modals/Modal";

const Flow = () => {
  const { nodes, edges, setSelectedNode, setIsOpen, isOpen } = useWorkflow();
  const { onNodesChange } = useFlowNodes();
  const { onEdgesChange, onConnect } = useFlowEdges();

  // Drag and Drop Node
  const { onDragOver, onDrop } = useDragAndDrop();

  // Auto Connect Near Nodes
  const { onNodeDrag, onNodeDragStop } = useNodeConnections();

  // Delete Edge on Reconnect
  const { onReconnectStart, onReconnect, onReconnectEnd } = useReconnect();

  const onNodeClick = (e, node) => {
    console.log(node);
    setSelectedNode(node);
    setIsOpen(true);
  };

  return (
    <div className="flex h-screen">
      {isOpen && <Modal />}
      <Sidebar />
      <div className="flex-1 bg-gray-100 p-6">
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          onConnect={onConnect}
          // onNodeDrag={onNodeDrag}
          // onNodeDragStop={onNodeDragStop}
          onDrop={onDrop}
          onNodeClick={onNodeClick}
          onDragOver={onDragOver}
          onReconnectStart={onReconnectStart}
          onReconnect={onReconnect}
          onReconnectEnd={onReconnectEnd}
          nodeTypes={nodeTypes}
          fitView
        >
          <Controls />
          <MiniMap />
          <Background variant="dots" gap={12} size={1} />
        </ReactFlow>
      </div>
    </div>
  );
};

const Workflow = () => (
  <ReactFlowProvider>
    <WorkflowProvider>
      <Flow />
    </WorkflowProvider>
  </ReactFlowProvider>
);

export default Workflow;
