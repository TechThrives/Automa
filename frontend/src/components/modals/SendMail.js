import React, { useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";
import { useReactFlow } from "@xyflow/react";

const SendMail = () => {
  const { setNodes } = useReactFlow();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [to, setTo] = useState(node.data.to);
  const [subject, setSubject] = useState(node.data.subject);
  const [message, setMessage] = useState(node.data.message);
  const [isValidTo, setIsValidTo] = useState(node.data.to !== "");
  const [isValidSubject, setIsValidSubject] = useState(node.data.subject !== "");
  const [isValidMessage, setIsValidMessage] = useState(node.data.message !== "");

  const handleToChange = (e) => {
    const value = e.target.value;
    setIsValidTo(value !== "");
    setTo(value);
  };

  const handleSubjectChange = (e) => {
    const value = e.target.value;
    setIsValidSubject(value !== "");
    setSubject(value);
  }

  const handleMessageChange = (e) => {
    const value = e.target.value;
    setIsValidMessage(value !== "");
    setMessage(value);
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isValidTo && isValidSubject && isValidMessage) {
      const newNode = { ...node, data: {...node.data, to, subject, message } };
      setNodes((nds) => nds.concat(newNode));
      setIsOpen(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="text-lg font-semibold text-gray-900 mb-3">
        Send Mail
      </h2>
      <div className="mb-3">
        <label
          htmlFor="to"
          className="block text-xs font-medium text-gray-700 mb-1"
        >
          To
        </label>
        <input
          type="email"
          id="to"
          name="to"
          value={to}
          onChange={handleToChange}
          className={`w-full px-2 py-1 text-sm border rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValidTo ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter email address"
        />
        {!isValidTo && (
          <p className="mt-1 text-xs text-red-500">
            Please enter a valid email address.
          </p>
        )}
      </div>

      <div className="mb-3">
        <label
          htmlFor="subject"
          className="block text-xs font-medium text-gray-700 mb-1"
        >
          Subject
        </label>
        <input
          type="text"
          id="subject"
          name="subject"
          value={subject}
          onChange={handleSubjectChange}
          className={`w-full px-2 py-1 text-sm border rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValidSubject ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter subject"
        />
        {!isValidSubject && (
          <p className="mt-1 text-xs text-red-500">
            Please enter a subject.
          </p>
        )}
      </div>
      <div className="mb-3">
        <label
          htmlFor="message"
          className="block text-xs font-medium text-gray-700 mb-1"
        >
          Message
        </label>
        <textarea
          id="message"
          name="message"
          value={message}
          onChange={handleMessageChange}
          className={`w-full px-2 py-1 text-sm border rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500 ${
            isValidMessage ? "border-gray-300" : "border-red-500"
          }`}
          placeholder="Enter message"
        />
        {!isValidMessage && (
          <p className="mt-1 text-xs text-red-500">
            Please enter a message.
          </p>
        )}
      </div>
      <div className="flex justify-end space-x-2">
        <button
          type="submit"
          className="px-3 py-1 text-xs bg-teal-600 text-white rounded-md hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500"
          disabled={!isValidTo || !isValidSubject || !isValidMessage}
        >
          Submit
        </button>
      </div>
    </form>
  );
};

export default SendMail;
