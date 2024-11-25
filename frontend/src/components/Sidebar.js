import React, { useState } from "react";
import { useWorkflow } from "../context/WorkflowContext";

const Sidebar = () => {
  const [openGroups, setOpenGroups] = useState({
    group1: true,
    group2: false,
  });
  const { _, setType } = useWorkflow();

  const toggleGroup = (group) => {
    setOpenGroups((prev) => ({ ...prev, [group]: !prev[group] }));
  };

  const components = {
    group1: ["Component A", "Component B", "Component C", "Component D"],
    group2: ["Component E", "Component F", "Component G", "Component H"],
  };

  const onDragStart = (event, nodeType) => {
    setType(nodeType);
    event.dataTransfer.effectAllowed = "move";
  };

  return (
    <div className="w-64 bg-gray-800 text-white p-4 overflow-y-auto">
      <h1 className="text-lg font-bold mb-4">Components</h1>
      {Object.keys(components).map((group, index) => (
        <div key={index} className="mb-4">
          {/* Group Header */}
          <button
            onClick={() => toggleGroup(group)}
            className="flex justify-between items-center w-full text-left py-2 px-3 bg-gray-700 hover:bg-gray-600 rounded"
          >
            <span className="font-medium">{`Group ${index + 1}`}</span>
            <span>{openGroups[group] ? "-" : "+"}</span>
          </button>
          {/* Group Content */}
          {openGroups[group] && (
            <div className="grid grid-cols-2 gap-2 mt-2">
              {components[group].map((component, idx) => (
                <div
                  key={idx}
                  className="p-2 bg-gray-600 rounded text-center hover:bg-gray-500 cursor-pointer"
                  onDragStart={(event) => onDragStart(event, "youtubeInfo")}
                  draggable
                >
                  {component}
                </div>
              ))}
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default Sidebar;
