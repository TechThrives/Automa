package com.automa.utils;

import com.automa.entity.flow.Flow;

import java.util.*;

public class WorkflowUtils {

    // Builds the graph from the existing flow requests
    private static Map<UUID, List<UUID>> buildGraph(List<Flow> existingFlows) {
        Map<UUID, List<UUID>> graph = new HashMap<>();
        for (Flow flow : existingFlows) {
            graph.computeIfAbsent(flow.getSource().getId(), k -> new ArrayList<>()).add(flow.getTarget().getId());
        }
        return graph;
    }

    // Checks if adding an edge (from source to target) creates a cycle in the graph
    private static boolean hasCycle(Map<UUID, List<UUID>> graph, UUID source, UUID target) {
        Set<UUID> visited = new HashSet<>();
        Set<UUID> recStack = new HashSet<>();

        // Depth-first search to detect cycle
        boolean cycleDetected = dfs(graph, source, target, visited, recStack);

        // Remove the temporary edge for cleanup
        graph.get(source).remove(target);

        return cycleDetected;
    }

    // DFS utility to check for cycle
    private static boolean dfs(Map<UUID, List<UUID>> graph, UUID node, UUID target, Set<UUID> visited, Set<UUID> recStack) {
        if (recStack.contains(node)) return true; // Cycle detected
        if (visited.contains(node)) return false;

        visited.add(node);
        recStack.add(node);

        for (UUID neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (neighbor.equals(target) || dfs(graph, neighbor, target, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }

    // Determines if a new connection from source to target is valid (i.e., does not create a cycle)
    public static boolean isValidConnection(UUID source, UUID target, List<Flow> existingFlows) {
        Map<UUID, List<UUID>> graph = buildGraph(existingFlows);
        graph.computeIfAbsent(source, k -> new ArrayList<>()).add(target);

        return !hasCycle(graph, source, target);
    }
}
