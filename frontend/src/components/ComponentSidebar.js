import React, { useEffect, useState } from "react";
import { useWorkflow } from "../context/WorkflowContext";
import { FiChevronDown, FiChevronRight } from "react-icons/fi";
import axiosConfig from "../utils/axiosConfig";
import { toast } from "react-hot-toast";
import { getIcon } from "../constants/ActionUtils";

const ComponentSidebar = () => {
  const [openGroups, setOpenGroups] = useState({});
  const [trigger, setTrigger] = useState({});
  const [actions, setActions] = useState({});
  const { setDragNode, hasTrigger } = useWorkflow();

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
    <div className="hidden w-64 bg-white p-4 text-gray-800 md:block">
      <h1 className="sticky top-0 mb-4 text-center text-lg font-bold">
        Components
      </h1>
      <div className="hide-scrollbar h-[90%] overflow-y-auto">
        <h2 className="text-md mb-3 font-semibold text-gray-900">Triggers</h2>
        {Object.entries(trigger).map(([group, items], index) => (
          <div key={group} className="mb-4 bg-gray-100">
            <button
              onClick={() => toggleGroup(group)}
              className="flex w-full items-center justify-between rounded px-3 py-2 text-left transition-colors duration-200 hover:bg-gray-200"
              aria-expanded={openGroups[group]}
            >
              <span className="font-medium capitalize">{group}</span>
              {openGroups[group] ? (
                <FiChevronDown className="h-5 w-5" aria-hidden="true" />
              ) : (
                <FiChevronRight className="h-5 w-5" aria-hidden="true" />
              )}
            </button>

            {openGroups[group] && (
              <div className="grid grid-cols-2 gap-2 overflow-hidden p-2 transition-all duration-200">
                {items.map((component, idx) => {
                  const IconComponent = getIcon(component.actionType);
                  return (
                    <div
                      key={idx}
                      className={`flex select-none flex-col items-center space-x-2 rounded bg-gray-100 p-2 text-sm transition-colors duration-200 hover:bg-gray-200 ${
                        hasTrigger ? "cursor-not-allowed" : "cursor-pointer"
                      }`}
                      onDragStart={(event) => onDragStart(event, component)}
                      draggable={!hasTrigger}
                    >
                      <IconComponent className="h-6 w-6" aria-hidden="true" />
                      <span className="text-center text-xs">
                        {component.name}
                      </span>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        ))}

        <h2 className="text-md mb-3 font-semibold text-gray-900">Actions</h2>
        {Object.entries(actions).map(([group, items], index) => (
          <div key={group} className="mb-4 bg-gray-100">
            <button
              onClick={() => toggleGroup(group)}
              className="flex w-full items-center justify-between rounded px-3 py-2 text-left transition-colors duration-200 hover:bg-gray-200"
              aria-expanded={openGroups[group]}
            >
              <span className="font-medium capitalize">{group}</span>
              {openGroups[group] ? (
                <FiChevronDown className="h-5 w-5" aria-hidden="true" />
              ) : (
                <FiChevronRight className="h-5 w-5" aria-hidden="true" />
              )}
            </button>

            {openGroups[group] && (
              <div className="grid grid-cols-2 gap-2 overflow-hidden p-2 transition-all duration-200">
                {items.map((component, idx) => {
                  const IconComponent = getIcon(component.actionType);
                  return (
                    <div
                      key={idx}
                      className={`flex select-none flex-col items-center space-x-2 rounded bg-gray-100 p-2 text-sm transition-colors duration-200 hover:bg-gray-200 ${
                        !hasTrigger ? "cursor-not-allowed" : "cursor-pointer"
                      }`}
                      onDragStart={(event) => onDragStart(event, component)}
                      draggable={hasTrigger}
                    >
                      <IconComponent className="h-6 w-6" aria-hidden="true" />
                      <span className="text-center text-xs">
                        {component.name}
                      </span>
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
