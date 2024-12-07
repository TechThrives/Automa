import {
  useEdges,
  useNodes as useNodesHook,
  useReactFlow,
} from "@xyflow/react";
import React from "react";

const useNodes = () => {
  const nodes = useNodesHook();
  const edges = useEdges();
  const getPreviousConnectedNodes = (nodeId) => {

    // Find edges where the target matches the given node ID
    const previousEdges = edges.filter((edge) => edge.target === nodeId);

    // Get the source nodes from these edges
    const previousNodeIds = previousEdges.map((edge) => edge.source);

    // Find the actual node objects
    const previousNodes = nodes.filter((node) =>
      previousNodeIds.includes(node.id)
    );

    return previousNodes;
  };
  return { getPreviousConnectedNodes };
};

export default useNodes;
