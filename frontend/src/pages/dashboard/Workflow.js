import React, { useCallback } from "react";
import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  ReactFlowProvider,
  useEdgesState,
  useNodesState,
  getOutgoers,
  getIncomers,
  getConnectedEdges,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import { nodeTypes } from "../../nodes/NodeTypes";
import { edgeTypes } from "../../edges/EdgeTypes";
import { useFlowEdges } from "../../hooks/useFlowEdges";
import { useDragAndDrop } from "../../hooks/useDragAndDrop";
import { useNodeConnections } from "../../hooks/useNodeConnections";
import { useReconnect } from "../../hooks/useReconnect";
import { WorkflowProvider, useWorkflow } from "../../context/WorkflowContext";
import ComponentSidebar from "../../components/ComponentSidebar";
import Modal from "../../components/modals/Modal";
import Markers from "../../edges/Markers";

const Flow = () => {
  const { setSelectedNode, setIsOpen, isOpen } = useWorkflow();
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const { onConnect, isValidConnection } = useFlowEdges();

  // Drag and Drop Node
  const { onDragOver, onDrop } = useDragAndDrop();

  // Auto Connect Near Nodes
  const { onNodeDrag, onNodeDragStop } = useNodeConnections();

  // Delete Edge on Reconnect
  const { onReconnectStart, onReconnect, onReconnectEnd } = useReconnect();

  const onNodeContextMenu = (e, node) => {
    e.preventDefault();
    setSelectedNode(node);
    setIsOpen(true);
  };

  const onNodesDelete = useCallback(
    (deleted) => {
      setEdges(
        deleted.reduce((acc, node) => {
          const connectedEdges = getConnectedEdges([node], edges);
          const remainingEdges = acc.filter(
            (edge) => !connectedEdges.includes(edge)
          );
          return remainingEdges;
        }, edges)
      );
    },
    [nodes, edges]
  );

  return (
    <div className="flex h-screen">
      {isOpen && <Modal />}
      <ComponentSidebar />
      <div className="flex-1 bg-gray-100 h-full p-6">
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          onConnect={onConnect}
          // onNodeDrag={onNodeDrag}
          // onNodeDragStop={onNodeDragStop}
          onDrop={onDrop}
          // onNodeClick={onNodeClick}
          onDragOver={onDragOver}
          // onReconnectStart={onReconnectStart}
          // onReconnect={onReconnect}
          // onReconnectEnd={onReconnectEnd}
          nodeTypes={nodeTypes}
          edgeTypes={edgeTypes}
          isValidConnection={isValidConnection}
          onNodeContextMenu={onNodeContextMenu}
          onNodesDelete={onNodesDelete}
          fitView
        >
          <Markers />
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
