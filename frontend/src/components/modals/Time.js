import React, { useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";
import { useReactFlow } from "@xyflow/react";

const Time = () => {
  const { setNodes } = useReactFlow();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [dateTime, setDateTime] = useState(node.data.dateTime);
  const [isValid, setIsValid] = useState(new Date(node.data.dateTime) > new Date());

  const handleDateTimeChange = (e) => {
    const value = e.target.value;
    setIsValid(new Date(value) > new Date());
    setDateTime(value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isValid) {
      const newNode = { ...node, data: {...node.data, dateTime } };
      setNodes((nds) => nds.concat(newNode));
      setIsOpen(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="text-lg font-semibold text-gray-900 mb-3">
        Schedule Time
      </h2>
      <div className="mb-3">
        <label
          htmlFor="dateTime"
          className="block text-xs font-medium text-gray-700 mb-1"
        >
          DateTime
        </label>
        <input
          type="datetime-local"
          id="dateTime"
          name="dateTime"
          value={dateTime}
          onChange={handleDateTimeChange}
          className={`w-full px-2 py-1 text-sm border rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-blue-500 ${
            isValid ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Schedule Time"
        />
        {!isValid && (
          <p className="mt-1 text-xs text-red-500">
            Time must be in future.
          </p>
        )}
      </div>
      <div className="flex justify-end space-x-2">
        <button
          type="submit"
          className="px-3 py-1 text-xs bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
          disabled={!isValid}
        >
          Submit
        </button>
      </div>
    </form>
  );
};

export default Time;
