import React, { useEffect, useState } from "react";
import { useWorkflow } from "../context/WorkflowContext";
import { FiChevronDown, FiChevronRight } from "react-icons/fi";
import axiosConfig from "../utils/axiosConfig";
import { toast } from "react-hot-toast";
import IconMapping from "../constants/IconMapping";

const ComponentSidebar = () => {
  const [openGroups, setOpenGroups] = useState({});
  const [trigger, setTrigger] = useState({});
  const [actions, setActions] = useState({});
  const { setDragNode } = useWorkflow();

  const getTriggers = async () => {
    try {
      const response = await axiosConfig.get("/api/info/group/triggers");
      if (response.data) {
        setTrigger(response.data);
      }
    } catch (error) {
      toast.error(error.response.data.message);
    }
  };

  const getActions = async () => {
    try {
      const response = await axiosConfig.get("/api/info/group/actions");
      if (response.data) {
        setActions(response.data);
      }
    } catch (error) {
      toast.error(error.response.data.message);
    }
  };

  useEffect(() => {
    getTriggers();
    getActions();
  }, []);

  const toggleGroup = (group) => {
    setOpenGroups((prev) => ({ ...prev, [group]: !prev[group] }));
  };

  const onDragStart = (event, node) => {
    setDragNode(node);
    event.dataTransfer.effectAllowed = "move";
  };

  return (
    <div className="w-64 bg-white text-gray-800 p-4 hidden md:block">
      <h1 className="text-lg font-bold mb-4 sticky top-0 text-center">
        Components
      </h1>
      <div className="h-[90%] overflow-y-auto hide-scrollbar">
        <h2 className="text-md font-semibold text-gray-900 mb-3">Triggers</h2>
        {Object.entries(trigger).map(([group, items], index) => (
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
                {items.map((component, idx) => {
                  const IconComponent = IconMapping[component.actionType];
                  return (
                    <div
                      key={idx}
                      className="p-2 bg-gray-100 rounded text-sm flex flex-col items-center space-x-2 hover:bg-gray-200 cursor-pointer transition-colors duration-200"
                      onDragStart={(event) =>
                        onDragStart(event, component)
                      }
                      draggable
                    >
                      <IconComponent className="w-6 h-6" />
                      <span className="text-center text-xs">{component.name}</span>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        ))}

        <h2 className="text-md font-semibold text-gray-900 mb-3">Actions</h2>
        {Object.entries(actions).map(([group, items], index) => (
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
                {items.map((component, idx) => {
                  const IconComponent = IconMapping[component.actionType];
                  return (
                    <div
                      key={idx}
                      className="p-2 bg-gray-100 rounded text-sm flex flex-col items-center space-x-2 hover:bg-gray-200 cursor-pointer transition-colors duration-200"
                      onDragStart={(event) =>
                        onDragStart(event, component)
                      }
                      draggable
                    >
                      <IconComponent className="w-6 h-6" />
                      <span className="text-center text-xs">{component.name}</span>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ComponentSidebar;
