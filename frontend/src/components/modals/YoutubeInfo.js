import React, { useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";

const YoutubeInfo = () => {
  const { setNodes } = useWorkflow();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [url, setUrl] = useState(node.nodeData.url);
  const [isValid, setIsValid] = useState(node.nodeData.url.trim().length >= 3);

  const handleUrlChange = (e) => {
    const value = e.target.value;
    setUrl(value);
    setIsValid(value.trim().length >= 3);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isValid) {
      setNodes((nodes) => {
        const restNodes = nodes.filter((e) => e.id !== node.id);
        restNodes.push({ ...node, nodeData: { url } });
        return restNodes;
      });
      setIsOpen(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="text-lg font-semibold text-gray-900 mb-3">
        Enter Your Information
      </h2>
      <div className="mb-3">
        <label
          htmlFor="name"
          className="block text-xs font-medium text-gray-700 mb-1"
        >
          Name
        </label>
        <input
          type="text"
          id="url"
          name="url"
          value={url}
          onChange={handleUrlChange}
          className={`w-full px-2 py-1 text-sm border rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-blue-500 ${
            isValid ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter your url"
        />
        {!isValid && (
          <p className="mt-1 text-xs text-red-500">
            Name must be at least 3 characters long
          </p>
        )}
      </div>
      <div className="flex justify-end space-x-2">
        <button
          type="button"
          className="px-3 py-1 text-xs bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500"
          onClick={() => setIsOpen(false)}
        >
          Cancel
        </button>
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

export default YoutubeInfo;
