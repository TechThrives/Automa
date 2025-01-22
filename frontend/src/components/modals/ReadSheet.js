import React, { useEffect, useState } from "react";
import { useWorkflow } from "../../context/WorkflowContext";
import { useReactFlow } from "@xyflow/react";
import axiosConfig from "../../utils/axiosConfig";
import { toast } from "react-hot-toast";

const ReadSheet = () => {
  const { setNodes } = useReactFlow();
  const { selectedNode: node, setIsOpen } = useWorkflow();
  const [data, setData] = useState(node.data);
  const [spreadsheets, setSpreadsheets] = useState([]);
  const [sheets, setSheets] = useState([]);

  useEffect(() => {
    const getSpreadsheets = async () => {
      try {
        const response = await axiosConfig.get("/api/action/spreadsheets/all");
        setSpreadsheets(response.data.files);
      } catch (error) {
        console.error("Error fetching spreadsheets:", error);
      }
    };

    getSpreadsheets();
    if (data.spreadsheetId !== "") getSheets(data.spreadsheetId);
  }, []);

  const getSheets = async (spreadsheetId) => {
    try {
      const response = await axiosConfig.get(
        `/api/action/spreadsheets/${spreadsheetId}/sheets`,
      );
      setSheets(response.data.sheets);
    } catch (error) {
      console.error("Error fetching sheets:", error);
    }
  };

  const handleSpreadsheetChange = (e) => {
    const value = e.target.value;
    setData({ ...data, spreadsheetId: value, sheetName: "" });
    getSheets(value);
  };

  const handleSheetChange = (e) => {
    const value = e.target.value;
    setData({ ...data, sheetName: value });
  };

  const handleChange = (e) => {
    const value = e.target.value;
    const name = e.target.name;
    setData({ ...data, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (data.spreadsheetId == "" || data.sheetName == "") {
      toast.error("Please select a spreadsheet and sheet.");
      return;
    }

    if (data.from > data.to || data.from <= 0 || data.to <= 0) {
      toast.error("Please enter valid range.");
      return;
    }

    const newNode = {
      ...node,
      data: { ...data },
    };
    
    setNodes((nds) => nds.concat(newNode));
    setIsOpen(false);
  };


  return (
    <form onSubmit={handleSubmit} className="p-4">
      <h2 className="mb-3 text-lg font-semibold text-gray-900">Read Sheet</h2>
      <div className="mb-3">
        <label
          htmlFor="spreadsheet"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Spreadsheet
        </label>
        <select
          id="spreadsheet"
          name="spreadsheet"
          value={data.spreadsheetId}
          onChange={(e) => handleSpreadsheetChange(e)}
          className="focus:shadow-outline-teal block w-full rounded-md border border-gray-300 px-3 py-2 text-xs leading-5 text-gray-900 focus:border-teal-500"
        >
          <option value="">Select a spreadsheet</option>
          {spreadsheets.map((spreadsheet) => (
            <option key={spreadsheet.id} value={spreadsheet.id}>
              {spreadsheet.name}
            </option>
          ))}
        </select>
      </div>
      <div className="mb-3">
        <label
          htmlFor="sheetName"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Sheet
        </label>
        <select
          id="sheetName"
          name="sheetName"
          value={data.sheetName}
          onChange={(e) => handleSheetChange(e)}
          className="focus:shadow-outline-teal block w-full rounded-md border border-gray-300 px-3 py-2 text-xs leading-5 text-gray-900 focus:border-teal-500"
        >
          <option value="">Select a sheet</option>
          {sheets.map((sheet) => (
            <option key={sheet.id} value={sheet.name}>
              {sheet.name}
            </option>
          ))}
        </select>
      </div>
      <div className="mb-3 flex justify-between gap-2">
        <div className="">
          <label
            htmlFor="from"
            className="mb-1 block text-xs font-medium text-gray-700"
          >
            From
          </label>
          <input
            type="number"
            id="from"
            name="from"
            min={1}
            value={data.from}
            onChange={handleChange}
            className="w-full rounded-md border border-gray-300 px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500"
            placeholder="From row"
          />
        </div>
        <div className="">
          <label
            htmlFor="to"
            className="mb-1 block text-xs font-medium text-gray-700"
          >
            To
          </label>
          <input
            type="number"
            id="to"
            name="to"
            min={1}
            value={data.to}
            onChange={handleChange}
            className="w-full rounded-md border border-gray-300 px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500"
            placeholder="To row"
          />
        </div>
      </div>
      <div className="mb-3">
        <label
          htmlFor="filterCol"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Filter Column
        </label>
        <input
          type="text"
          id="filterCol"
          name="filterCol"
          value={data.filterCol}
          onChange={handleChange}
          className="w-full rounded-md border border-gray-300 px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500"
          placeholder="Column to filter on"
        />
      </div>

      <div className="mb-3">
        <label
          htmlFor="filterVal"
          className="mb-1 block text-xs font-medium text-gray-700"
        >
          Filter Value
        </label>
        <input
          type="text"
          id="filterVal"
          name="filterVal"
          value={data.filterVal}
          onChange={handleChange}
          className="w-full rounded-md border border-gray-300 px-2 py-1 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-teal-500"
          placeholder="Value to filter on"
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

export default ReadSheet;
