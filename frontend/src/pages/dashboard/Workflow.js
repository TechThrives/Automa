import React, { useCallback, useEffect, useState } from "react";
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
  Panel,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import nodeTypes from "../../nodes/NodeTypes";
import edgeTypes from "../../edges/EdgeTypes";
import useFlowEdges from "../../hooks/useFlowEdges";
import useDragAndDrop from "../../hooks/useDragAndDrop";
import useNodeConnections from "../../hooks/useNodeConnections";
import useReconnect from "../../hooks/useReconnect";
import { WorkflowProvider, useWorkflow } from "../../context/WorkflowContext";
import ComponentSidebar from "../../components/ComponentSidebar";
import Modal from "../../components/modals/Modal";
import Markers from "../../edges/Markers";
import axiosConfig from "../../utils/axiosConfig";
import toast from "react-hot-toast";
import { useNavigate, useParams } from "react-router-dom";
import { validate } from "uuid";
import { isTrigger } from "../../constants/ActionUtils";

const Flow = () => {
  const { setSelectedNode, setIsOpen, isOpen } = useWorkflow();
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const { onConnect, isValidConnection } = useFlowEdges();
  const [rfInstance, setRfInstance] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();

  const { hasTrigger, setHasTrigger } = useWorkflow();

  const getWorkflow = async () => {
    try {
      const response = await axiosConfig.get(`/api/workflow/${id}`);
      if (response.data) {
        setNodes(response.data.nodes);
        setEdges(response.data.edges);
      }
    } catch (error) {
      toast.error(error.response.data.message);
      setNodes([]);
      setEdges([]);
    }
  };

  useEffect(() => {
    if (validate(id)) getWorkflow();
  }, [id]);

  // Drag and Drop Node
  const { onDragOver, onDrop } = useDragAndDrop();

  // Auto Connect Near Nodes
  const { onNodeDrag, onNodeDragStop } = useNodeConnections();

  // Delete Edge on Reconnect
  const { onReconnectStart, onReconnect, onReconnectEnd } = useReconnect();

  const handleNodesChange = (changes) => {
    changes.forEach((change) => {
      if (change.type === "add" || change.type === "remove") {
        if (change.type === "add" && isTrigger(change.item.type)) {
          setHasTrigger(true);
        }

        if (change.type === "remove") {
          const removedNode = nodes.find((node) => node.id === change.id);
          if (isTrigger(removedNode.type)) {
            setHasTrigger(false);
          }
          const remainingNodes = nodes.filter(
            (node) => node.id !== change.id
          )
          if(remainingNodes.some((node) => isTrigger(node.type))) {
            setHasTrigger(true);
          }
        }
      }
    });

    console.log(changes, hasTrigger);
    onNodesChange(changes);
  };

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

  const onSaveWorkflow = useCallback(async () => {
    if (rfInstance) {
      const flow = rfInstance.toObject();
      try {
        const data = {
          ...flow,
          ...(validate(id) && { id }),
        };

        const response = await axiosConfig.post("/api/workflow/save", data);
        if (response.data) {
          toast.success("Workflow saved successfully");
          navigate("/dashboard/workflow/" + response.data.id);
        }
      } catch (error) {
        toast.error(error.response.data.message);
      }
    }
  }, [rfInstance]);

  return (
    <div className="flex h-screen">
      {isOpen && <Modal />}
      <ComponentSidebar />
      <div className="flex-1 bg-gray-100 h-full p-6">
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={handleNodesChange}
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
          onInit={setRfInstance}
          fitView
        >
          <Markers />
          <Controls />
          <MiniMap />
          <Background variant="dots" gap={12} size={1} />
          <Panel position="top-right">
            <button onClick={onSaveWorkflow}>save</button>
          </Panel>
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
