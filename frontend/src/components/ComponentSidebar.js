import React, { useState } from "react";
import { useWorkflow } from "../context/WorkflowContext";
import {
  FiChevronDown,
  FiChevronRight,
  FiYoutube,
  FiZap,
} from "react-icons/fi";

const ComponentSidebar = () => {
  const [openGroups, setOpenGroups] = useState({
    trigger: true,
    youtubeAction: false,
  });
  const { setType } = useWorkflow();

  const toggleGroup = (group) => {
    setOpenGroups((prev) => ({ ...prev, [group]: !prev[group] }));
  };

  const components = {
    trigger: [
      { name: "Trigger A", type: "input", icon: FiZap },
      { name: "Trigger B", type: "triggerB", icon: FiZap },
      { name: "Trigger C", type: "triggerC", icon: FiZap },
    ],
    youtubeAction: [
      { name: "Get Video Info", type: "youtubeInfo", icon: FiYoutube },
      { name: "Save Video Info", type: "youtubeInfo", icon: FiYoutube },
      { name: "Process Video", type: "youtubeInfo", icon: FiYoutube },
    ],
    youtubeActions: [
      { name: "Get Video Info", type: "youtubeInfo", icon: FiYoutube },
      { name: "Save Video Info", type: "youtubeInfo", icon: FiYoutube },
      { name: "Process Video", type: "youtubeInfo", icon: FiYoutube },
    ],
    youtubeActiosn: [
      { name: "Get Video Info", type: "youtubeInfo", icon: FiYoutube },
      { name: "Save Video Info", type: "youtubeInfo", icon: FiYoutube },
      { name: "Process Video", type: "youtubeInfo", icon: FiYoutube },
    ],
  };

  const onDragStart = (event, nodeType) => {
    setType(nodeType);
    event.dataTransfer.effectAllowed = "move";
  };

  return (
    <div className="w-64 bg-white text-gray-800 p-4 hidden md:block">
      <h1 className="text-lg font-bold mb-4 sticky top-0">Components</h1>
      <div className="h-[90%] overflow-y-auto hide-scrollbar">
        {Object.entries(components).map(([group, items], index) => (
          <div key={group} className="mb-4 bg-gray-100">
            <button
              onClick={() => toggleGroup(group)}
              className="flex justify-between items-center w-full text-left py-2 px-3 hover:bg-gray-200 rounded transition-colors duration-200"
            >
              <span className="font-medium capitalize">{group}</span>
              {openGroups[group] ? (
                <FiChevronDown className="w-5 h-5" />
              ) : (
                <FiChevronRight className="w-5 h-5" />
              )}
            </button>

            {openGroups[group] && (
              <div className="grid grid-cols-2 gap-2 overflow-hidden transition-all duration-200 p-2">
                {items.map((component, idx) => (
                  <div
                    key={idx}
                    className="p-2 bg-gray-100 rounded text-sm flex flex-col items-center space-x-2 hover:bg-gray-200 cursor-pointer transition-colors duration-200"
                    onDragStart={(event) => onDragStart(event, component.type)}
                    draggable
                  >
                    <component.icon className="w-6 h-6" />
                    <span className="text-center">{component.name}</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ComponentSidebar;
