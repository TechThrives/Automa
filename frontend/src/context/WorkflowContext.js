import { createContext, useContext, useRef, useState } from "react";

const WorkflowContext = createContext([null, (_) => {}]);

export const WorkflowProvider = ({ children }) => {
  const [dragNode, setDragNode] = useState({});
  const [isOpen, setIsOpen] = useState(false);
  const [selectedNode, setSelectedNode] = useState(null);
  const edgeReconnectSuccessful = useRef(true);

  return (
    <WorkflowContext.Provider
      value={{
        dragNode,
        setDragNode,
        isOpen,
        setIsOpen,
        selectedNode,
        setSelectedNode,
        edgeReconnectSuccessful,
      }}
    >
      {children}
    </WorkflowContext.Provider>
  );
};

export default WorkflowContext;

export const useWorkflow = () => {
  return useContext(WorkflowContext);
};
