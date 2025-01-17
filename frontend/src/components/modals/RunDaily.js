import React, { useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";
import { useReactFlow } from "@xyflow/react";

const RunDaily = () => {
  const { setNodes } = useReactFlow();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [time, setTime] = useState(node.data.time);

  const handleTimeChange = (e) => {
    const value = e.target.value;
    setTime(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const newNode = { ...node, data: { ...node.data, time, active: true } };
    setNodes((nds) => nds.concat(newNode));
    setIsOpen(false);
  };
  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="mb-3 text-lg font-semibold text-gray-900">Run Daily</h2>
      <div className="mb-3">
        <label
          htmlFor="time"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Time
        </label>
        <input
          type="time"
          id="time"
          name="time"
          value={time}
          onChange={handleTimeChange}
          className={`w-full rounded-md border px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500`}
          placeholder="Select Time"
        />
      </div>
      <div className="flex justify-end space-x-2">
        <button
          type="submit"
          className="rounded-md bg-teal-600 px-3 py-1 text-xs text-white hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500"
        >
          Submit
        </button>
      </div>
    </form>
  );
};

export default RunDaily;
