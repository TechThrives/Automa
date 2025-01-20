package com.automa.utils;

import com.automa.entity.action.ActionGroup;
import com.automa.entity.action.ActionType;
import com.automa.entity.flow.Flow;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkflowUtils {

    // Builds the graph from the existing flows
    private static Map<UUID, Set<UUID>> buildGraph(List<Flow> existingFlows) {
        Map<UUID, Set<UUID>> graph = new HashMap<>();
        for (Flow flow : existingFlows) {
            graph.computeIfAbsent(flow.getSource().getId(), k -> new HashSet<>()).add(flow.getTarget().getId());
        }
        return graph;
    }

    // Checks if adding an edge (from source to target) creates a cycle in the graph
    private static boolean hasCycle(Map<UUID, Set<UUID>> graph, UUID source, UUID target) {
        // Temporarily add the edge to the graph
        graph.computeIfAbsent(source, k -> new HashSet<>()).add(target);

        // Perform cycle detection using a visited set
        Set<UUID> visited = new HashSet<>();
        Set<UUID> recStack = new HashSet<>();

        for (UUID node : graph.keySet()) {
            if (dfs(graph, node, visited, recStack)) {
                return true;
            }
        }

        // Cleanup: Remove the temporary edge
        graph.get(source).remove(target);
        return false;
    }

    // DFS utility to detect cycles
    private static boolean dfs(Map<UUID, Set<UUID>> graph, UUID node, Set<UUID> visited, Set<UUID> recStack) {
        if (recStack.contains(node))
            return true; // Cycle detected
        if (visited.contains(node))
            return false;

        visited.add(node);
        recStack.add(node);

        for (UUID neighbor : graph.getOrDefault(node, Collections.emptySet())) {
            if (dfs(graph, neighbor, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }

    // Determines if a new connection from source to target is valid
    public static boolean isValidConnection(UUID source, UUID target, List<Flow> existingFlows) {
        Map<UUID, Set<UUID>> graph = buildGraph(existingFlows);
        return !hasCycle(graph, source, target);
    }

    public static List<ActionType> findActionTypesBySchedule() {
        List<ActionType> actionTypes = new ArrayList<>();
        for (ActionType action : ActionType.values()) {
            if (ActionGroup.SCHEDULER.equals(action.getActionGroup())) {
                actionTypes.add(action);
            }
        }
        return actionTypes;
    }

    public static ArrayList<HashMap<String, Object>> replaceVariableWithData(
        HashMap<String, Object> data,
        HashMap<String, ArrayList<HashMap<String, Object>>> workflowOutput) {
    ArrayList<HashMap<String, Object>> result = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    List<String> keys = new ArrayList<>(data.keySet());

    for (Map.Entry<String, ArrayList<HashMap<String, Object>>> workflowEntry : workflowOutput.entrySet()) {
        String workflowKey = workflowEntry.getKey();
        ArrayList<HashMap<String, Object>> workflowItems = workflowEntry.getValue();
        
        for (HashMap<String, Object> item : workflowItems) {
            HashMap<String, Object> resultItem = new HashMap<>(data);
            
            for (String key : keys) {
                String value = data.get(key).toString();
                Matcher matcher = pattern.matcher(value);
                
                if (matcher.find()) {
                    String placeholder = matcher.group(1);
                    String[] parts = placeholder.split("\\.");
                    
                    if (parts.length >= 2 && parts[0].equals(workflowKey)) {
                        try {
                            Object resolvedValue = resolveNestedValue(item, Arrays.copyOfRange(parts, 2, parts.length), placeholder);
                            if (resolvedValue != null) {
                                resultItem.put(key, resolvedValue);
                            }
                        } catch (RuntimeException e) {
                            throw new RuntimeException(
                                String.format("Invalid path '%s' for key '%s': %s", 
                                placeholder, key, e.getMessage())
                            );
                        }
                    }
                }
            }
            
            if (!resultItem.isEmpty()) {
                result.add(resultItem);
            }
        }
    }
    
    return result;
}

private static Object resolveNestedValue(Map<String, Object> map, String[] keys, String fullPath) {
    Object current = map;
    StringBuilder currentPath = new StringBuilder();
    
    for (int i = 0; i < keys.length; i++) {
        String key = keys[i];
        currentPath.append(key);
        
        if (current instanceof Map) {
            Map<?, ?> currentMap = (Map<?, ?>) current;
            if (!currentMap.containsKey(key)) {
                throw new RuntimeException(
                    String.format("Path element '%s' not found in workflow data", key)
                );
            }
            current = currentMap.get(key);
        } else {
            throw new RuntimeException(
                String.format("Cannot access '%s' as it's not a map or doesn't exist", key)
            );
        }
        
        if (i < keys.length - 1) {
            currentPath.append(".");
        }
    }
    
    return current;
}

    public static boolean runNow(HashMap<String, Object> data, ActionType actionType) {
        switch (actionType) {
            case RUNONCE:
                try {
                    if ((Boolean) data.get("active") == true) {
                        String dateTimeString = (String) data.get("dateTime");
                        LocalDateTime inputDateTime = LocalDateTime.parse(dateTimeString,
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
                        if (Math.abs(ChronoUnit.MINUTES.between(currentDateTime, inputDateTime)) == 0) {
                            return true;
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error parsing date time: " + e.getMessage());
                }
                return false;

            case RUNDAILY:
                try {
                    if ((Boolean) data.get("active") == true) {
                        String timeString = (String) data.get("time");
                        LocalTime inputTime = LocalTime.parse(timeString);
                        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Kolkata"));
                        if (Math.abs(ChronoUnit.MINUTES.between(currentTime, inputTime)) == 0) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing time: " + e.getMessage());
                }
                return false;
                
            default:
                break;
        }
        return false;

    }
}
