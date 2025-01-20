import React, { useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";
import { useReactFlow } from "@xyflow/react";
import useNodes from "../../hooks/useNodes";

const RunOnce = () => {
  const { setNodes } = useReactFlow();
  const { getName } = useNodes();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [dateTime, setDateTime] = useState(node.data.dateTime);
  const [isValid, setIsValid] = useState(
    new Date(node.data.dateTime) > new Date(),
  );

  const handleDateTimeChange = (e) => {
    const value = e.target.value;
    setIsValid(new Date(value) > new Date());
    setDateTime(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isValid) {
      const name = node.name || getName(node.type);
      const newNode = {
        ...node,
        name: name,
        data: { ...node.data, dateTime, active: true },
      };
      setNodes((nds) => nds.concat(newNode));
      setIsOpen(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="mb-3 text-lg font-semibold text-gray-900">Run Once</h2>
      <div className="mb-3">
        <label
          htmlFor="dateTime"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          DateTime
        </label>
        <input
          type="datetime-local"
          id="dateTime"
          name="dateTime"
          value={dateTime}
          onChange={handleDateTimeChange}
          className={`w-full rounded-md border px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValid ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Schedule Time"
        />
        {!isValid && (
          <p className="mt-1 text-xs text-red-500">Time must be in future.</p>
        )}
      </div>
      <div className="flex justify-end space-x-2">
        <button
          type="submit"
          className="rounded-md bg-teal-600 px-3 py-1 text-xs text-white hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500"
          disabled={!isValid}
        >
          Submit
        </button>
      </div>
    </form>
  );
};

export default RunOnce;
