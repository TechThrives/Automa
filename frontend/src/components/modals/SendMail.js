import React, { useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";
import { useReactFlow } from "@xyflow/react";

const SendMail = () => {
  const { setNodes } = useReactFlow();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [data, setData] = useState(node.data);
  const [isValid, setIsValid] = useState({
    to: node.data.to !== "",
    subject: node.data.subject !== "",
    message: node.data.message !== "",
  });

  const handleChange = (e) => {
    const value = e.target.value;
    const name = e.target.name;
    setIsValid({ ...isValid, [name]: value !== "" });
    setData({ ...data, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isValid.to && isValid.subject && isValid.message) {
      const newNode = {
        ...node,
        data: { ...data },
      };
      setNodes((nds) => nds.concat(newNode));
      setIsOpen(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="mb-3 text-lg font-semibold text-gray-900">Send Mail</h2>
      <div className="mb-3">
        <label
          htmlFor="to"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          To
        </label>
        <input
          type="email"
          id="to"
          name="to"
          value={data.to}
          onChange={handleChange}
          className={`w-full rounded-md border px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValid.to ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter email address"
        />
        {!isValid.to && (
          <p className="mt-1 text-xs text-red-500">
            Please enter a valid email address.
          </p>
        )}
      </div>

      <div className="mb-3">
        <label
          htmlFor="subject"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Subject
        </label>
        <input
          type="text"
          id="subject"
          name="subject"
          value={data.subject}
          onChange={handleChange}
          className={`w-full rounded-md border px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValid.subject ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter subject"
        />
        {!isValid.subject && (
          <p className="mt-1 text-xs text-red-500">Please enter a subject.</p>
        )}
      </div>
      <div className="mb-3">
        <label
          htmlFor="message"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Message
        </label>
        <textarea
          id="message"
          name="message"
          value={data.message}
          onChange={handleChange}
          className={`w-full rounded-md border px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValid.message ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter message"
        />
        {!isValid.message && (
          <p className="mt-1 text-xs text-red-500">Please enter a message.</p>
        )}
      </div>
      <div className="flex justify-end space-x-2">
        <button
          type="submit"
          className="rounded-md bg-teal-600 px-3 py-1 text-xs text-white hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500"
          disabled={!isValid.to || !isValid.subject || !isValid.message}
        >
          Submit
        </button>
      </div>
    </form>
  );
};

export default SendMail;
