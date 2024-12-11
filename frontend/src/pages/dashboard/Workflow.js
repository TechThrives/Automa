import React, { useCallback, useEffect, useState } from "react";
import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  ReactFlowProvider,
  useEdgesState,
  useNodesState,
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
  const [name, setName] = useState("");
  const { onConnect, isValidConnection } = useFlowEdges();
  const [rfInstance, setRfInstance] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();

  const { setHasTrigger } = useWorkflow();

  const getWorkflow = async () => {
    try {
      const response = await axiosConfig.get(`/api/workflow/${id}`);
      if (response.data) {
        const { nodes, edges, name } = response.data;

        setNodes(nodes);
        setEdges(edges);
        setName(name);

        const hasTriggerNode = nodes.some((node) => isTrigger(node.type));
        setHasTrigger(hasTriggerNode);
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to load workflow.");
      setNodes([]);
      setEdges([]);
      setName("");
      setHasTrigger(false);
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
          if (removedNode && isTrigger(removedNode.type)) {
            const remainingNodes = nodes.filter(
              (node) => node.id !== change.id,
            );
            const hasAnotherTrigger = remainingNodes.some((node) =>
              isTrigger(node.type),
            );
            setHasTrigger(hasAnotherTrigger);
          }
        }
      }
    });

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
            (edge) => !connectedEdges.includes(edge),
          );
          return remainingEdges;
        }, edges),
      );
    },
    [nodes, edges],
  );

  const onSaveWorkflow = useCallback(async () => {
    if (rfInstance) {
      const flow = rfInstance.toObject();
      try {
        const data = {
          ...(validate(id) && { id }),
          ...flow,
          name,
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
  }, [rfInstance, name]);


  return (
    <div className="flex h-screen">
      {isOpen && <Modal />}
      <ComponentSidebar />
      <div className="h-full flex-1 bg-gray-100 p-6">
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
          <Panel position="top-center">
            <div className="flex items-center justify-center">
              <div className="flex items-center">
                <input
                  type="text"
                  placeholder="Name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="rounded-md bg-transparent p-2 text-center font-semibold text-gray-800 outline-none"
                />
              </div>
            </div>
          </Panel>
          <Panel position="top-right">
            <button
              className="rounded-md bg-teal-500 px-2 py-1 text-white"
              onClick={onSaveWorkflow}
            >
              Save
            </button>
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
