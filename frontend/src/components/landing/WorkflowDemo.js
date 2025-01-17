import { useCallback, useState } from "react";
import {
  ReactFlow,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import nodeTypes from "../../nodes/NodeTypes";
import edgeTypes from "../../edges/EdgeTypes";
import Markers from "../../edges/Markers";

const WorkflowDemo = () => {
  const [activeTab, setActiveTab] = useState("getNews");

  const tabs = {
    getNews: {
      nodes: [
        { id: "1", position: { x: -200, y: 30 }, type: "RUNDAILY" },
        { id: "2", position: { x: -50, y: 50 }, type: "GETTODAYNEWS" },
        { id: "3", position: { x: 100, y: 70 }, type: "SENDMAIL" },
      ],
      edges: [
        { id: "e1-2", source: "1", target: "2", type: "NORMAL" },
        { id: "e2-3", source: "2", target: "3", type: "NORMAL" },
      ],
      label: "Get News Email",
      icon: "💻",
      desc: "Fetch News daily and sends an email.",
    },
    uploadVideo: {
      nodes: [
        { id: "1", position: { x: -220, y: 0 }, type: "RUNONCE" },
        { id: "2", position: { x: -114, y: 32 }, type: "READSHEET" },
        { id: "3", position: { x: -12, y: 84 }, type: "DOWNLOADVIDEO" },
        { id: "4", position: { x: 103, y: 31 }, type: "UPLOADVIDEO" },
      ],
      edges: [
        { id: "e1-2", source: "1", target: "2", type: "NORMAL" },
        { id: "e2-3", source: "2", target: "3", type: "NORMAL" },
        { id: "e3-4", source: "3", target: "4", type: "NORMAL" },
      ],
      label: "Upload Youtube Video",
      icon: "☁️",
      desc: "Only run once read the sheet and download the video and upload it to youtube.",
    },
  };

  const [nodes, setNodes, onNodesChange] = useNodesState(tabs[activeTab].nodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(tabs[activeTab].edges);

  const buildGraph = () => {
    const graph = {};
    edges.forEach(({ source, target }) => {
      if (!graph[source]) graph[source] = [];
      graph[source].push(target);
    });
    return graph;
  };

  const doesCreateCycle = (source, target) => {
    const graph = buildGraph();
    const visited = new Set();
    const recStack = new Set();

    const dfs = (node) => {
      if (recStack.has(node)) return true;
      if (visited.has(node)) return false;

      visited.add(node);
      recStack.add(node);

      for (const neighbor of graph[node] || []) {
        if (dfs(neighbor)) return true;
      }

      recStack.delete(node);
      return false;
    };

    if (!graph[source]) graph[source] = [];
    graph[source].push(target);

    const hasCycle = dfs(source);

    graph[source].pop();

    return hasCycle;
  };

  const onConnect = useCallback((params) => {
    params.type = "NORMAL";
    params.id = `e${params.source}-${params.target}`;
    setEdges((eds) => addEdge({ ...params }, eds));
  }, []);

  const isValidConnection = useCallback(
    (params) => {
      return !doesCreateCycle(params.source, params.target);
    },
    [edges],
  );

  const handleTabChange = (tabId) => {
    setActiveTab(tabId);
    setNodes(tabs[tabId].nodes);
    setEdges(tabs[tabId].edges);
  };

  return (
    <section className="px-4 py-16 sm:px-6 lg:px-8">
      <div className="max-w-7xl mx-auto">
        {/* Tabs */}
        <div className="mb-8">
          <div className="flex justify-center w-full gap-2">
            {Object.keys(tabs).map((tab) => (
              <button
                key={tab}
                onClick={() => handleTabChange(tab)}
                className={`flex items-center justify-center gap-2 rounded-md p-4 text-sm font-medium transition-all mx-6 my-4 ${
                  activeTab === tab
                    ? "bg-[#2D2E47] text-white shadow-lg"
                    : "bg-white text-gray-600 hover:bg-gray-50"
                }`}
              >
                <span>{tabs[tab].icon}</span>
                {tabs[tab].label}
              </button>
            ))}
          </div>

          <div className="rounded-lg bg-[#2D2E47] p-6 text-white">
            <div className="mb-3 flex items-center gap-2 text-sm text-gray-300">
              {tabs[activeTab].desc}
            </div>

            <div className="relative">
              <div className="flex h-96 w-full rounded-xl bg-white">
                <ReactFlow
                  nodes={nodes}
                  edges={edges}
                  onNodesChange={onNodesChange}
                  onEdgesChange={onEdgesChange}
                  onConnect={onConnect}
                  isValidConnection={isValidConnection}
                  nodeTypes={nodeTypes}
                  edgeTypes={edgeTypes}
                  fitView
                  preventScrolling={false}
                >
                  <Markers />
                  <Background variant="dots" gap={12} size={1} />
                </ReactFlow>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default WorkflowDemo;
