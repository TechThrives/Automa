import { useEdges, useNodes as useNodesHook } from "@xyflow/react";

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
      previousNodeIds.includes(node.id),
    );

    return previousNodes;
  };

  const getName = (type) => {
    const allNodes = nodes.filter((node) => node.type === type);
  
    if (allNodes.length === 0) {
      return type;
    } else {
      return type + allNodes.length;
    }
  };

  return { getPreviousConnectedNodes, getName };
};

export default useNodes;
