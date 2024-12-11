import { useCallback } from "react";
import { addEdge, useEdges, useReactFlow } from "@xyflow/react";
import { v4 } from "uuid";

const useFlowEdges = () => {
  const { setEdges } = useReactFlow();
  const edges = useEdges();

  // Helper function to build the graph from existing edges
  const buildGraph = () => {
    const graph = {};
    edges.forEach(({ source, target }) => {
      if (!graph[source]) graph[source] = [];
      graph[source].push(target);
    });
    return graph;
  };

  // Helper function to check if adding an edge creates a cycle
  const doesCreateCycle = (source, target) => {
    const graph = buildGraph();
    const visited = new Set();
    const recStack = new Set();

    const dfs = (node) => {
      if (recStack.has(node)) return true; // Cycle detected
      if (visited.has(node)) return false;

      visited.add(node);
      recStack.add(node);

      for (const neighbor of graph[node] || []) {
        if (dfs(neighbor)) return true;
      }

      recStack.delete(node);
      return false;
    };

    // Temporarily add the new edge to check for a cycle
    if (!graph[source]) graph[source] = [];
    graph[source].push(target);

    const hasCycle = dfs(source);

    // Remove the temporary edge
    graph[source].pop();

    return hasCycle;
  };

  const onConnect = useCallback((params) => {
    params.type = "NORMAL";
    params.id = v4();
    setEdges((eds) => addEdge({ ...params }, eds));
  }, []);

  const isValidConnection = useCallback((params) => {
    return !doesCreateCycle(params.source, params.target);
  }, [edges]);

  return {
    onConnect,
    isValidConnection,
  };
};

export default useFlowEdges;