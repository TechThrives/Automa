import { useCallback } from "react";
import { MarkerType, useReactFlow, useStoreApi } from "@xyflow/react";
import { useWorkflow } from "../context/WorkflowContext";

export const useNodeConnections = () => {
  const { setEdges } = useWorkflow();
  const store = useStoreApi();
  const { getInternalNode } = useReactFlow();
  const MIN_DISTANCE = 150;

  const getClosestEdge = useCallback((node) => {
    const { nodeLookup } = store.getState();
    const internalNode = getInternalNode(node.id);

    const closestNode = Array.from(nodeLookup.values()).reduce(
      (res, n) => {
        if (n.id !== internalNode.id) {
          const dx =
            n.internals.positionAbsolute.x -
            internalNode.internals.positionAbsolute.x;
          const dy =
            n.internals.positionAbsolute.y -
            internalNode.internals.positionAbsolute.y;
          const d = Math.sqrt(dx * dx + dy * dy);

          if (d < res.distance && d < MIN_DISTANCE) {
            res.distance = d;
            res.node = n;
          }
        }

        return res;
      },
      {
        distance: Number.MAX_VALUE,
        node: null,
      }
    );

    if (!closestNode.node) {
      return null;
    }

    const closeNodeIsSource =
      closestNode.node.internals.positionAbsolute.x <
      internalNode.internals.positionAbsolute.x;

    return {
      id: closeNodeIsSource
        ? `${closestNode.node.id}-${node.id}`
        : `${node.id}-${closestNode.node.id}`,
      source: closeNodeIsSource ? closestNode.node.id : node.id,
      target: closeNodeIsSource ? node.id : closestNode.node.id,
    };
  }, []);

  const onNodeDrag = useCallback(
    (_, node) => {
      const closeEdge = getClosestEdge(node);

      setEdges((es) => {
        const nextEdges = es.filter((e) => e.className !== "temp");

        if (
          closeEdge &&
          !nextEdges.find(
            (ne) =>
              ne.source === closeEdge.source && ne.target === closeEdge.target
          )
        ) {
          closeEdge.className = "temp";
          closeEdge.markerEnd ={ type: MarkerType.Arrow };
          nextEdges.push(closeEdge);
        }

        return nextEdges;
      });
    },
    [getClosestEdge, setEdges]
  );

  const onNodeDragStop = useCallback(
    (_, node) => {
      const closeEdge = getClosestEdge(node);

      setEdges((es) => {
        const nextEdges = es.filter((e) => e.className !== "temp");

        if (
          closeEdge &&
          !nextEdges.find(
            (ne) =>
              ne.source === closeEdge.source && ne.target === closeEdge.target
          )
        ) {
          closeEdge.markerEnd ={ type: MarkerType.Arrow };
          nextEdges.push(closeEdge);
        }

        return nextEdges;
      });
    },
    [getClosestEdge, setEdges]
  );

  return { onNodeDrag, onNodeDragStop };
};
