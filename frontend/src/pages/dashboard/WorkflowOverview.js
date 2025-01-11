import React, { useState, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import DashboardCard from "../../components/cards/DashboardCard";
import { IoPlayOutline } from "react-icons/io5";
import { MdOutlineAccountTree } from "react-icons/md";
import { FiEye } from "react-icons/fi";
import { ActionIcon } from "../../constants/ActionUtils";
import axiosConfig from "../../utils/axiosConfig";
import toast from "react-hot-toast";

const WorkflowOverview = () => {
  const [workflows, setWorkflows] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchWorkflows = async () => {
      try {
        const response = await axiosConfig.get("/api/workflow/my");
        setWorkflows(response.data);
      } catch (error) {
        console.error("Error fetching workflows:", error);
      }
    };

    fetchWorkflows();
  }, []);

  const handleActiveChange = async (e, workflowId) => {
    try {
      const updatedStatus = e.target.checked;

      const response = await axiosConfig.post(
        `/api/workflow/${workflowId}/toggle?isActive=${updatedStatus}`,
      );

      const updatedWorkflows = workflows.map((workflow) => {
        if (workflow.id === workflowId) {
          return response.data;
        }
        return workflow;
      });

      setWorkflows(updatedWorkflows);
      toast.success(
        `Workflow has been ${updatedStatus ? "activated" : "deactivated"}.`,
      );
    } catch (error) {
      toast.error("Failed to update workflow status.");
    }
  };

  const runCount = useMemo(() => {
    return workflows.reduce((total, workflow) => total + workflow.runs, 0);
  }, [workflows]);

  return (
    <>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <DashboardCard
          title="Total Runs"
          value={runCount}
          icon={<IoPlayOutline className="h-6 w-6 text-white" />}
          color="bg-indigo-500"
        />
        <DashboardCard
          title="Total Workflows"
          value={workflows.length}
          icon={<MdOutlineAccountTree className="h-6 w-6 text-white" />}
          color="bg-emerald-500"
        />
      </div>
      <div className="mb-6 mt-4 flex w-full min-w-0 flex-col break-words rounded-lg bg-white p-4 shadow-lg">
        <div className="overflow-x-auto">
          <table className="min-w-full overflow-hidden rounded-lg bg-white shadow-md select-none">
            <thead>
              <tr className="bg-gray-200 text-gray-600">
                <th className="px-4 py-2 text-left">Workflow Name</th>
                <th className="px-4 py-2 text-left">Trigger Action</th>
                <th className="px-4 py-2 text-left">Workflow Actions</th>
                <th className="px-4 py-2 text-left">Runs</th>
                <th className="px-4 py-2 text-left">Active</th>
                <th className="px-4 py-2 text-center">Options</th>
              </tr>
            </thead>
            <tbody>
              {workflows.map((workflow, index) => {
                return (
                  <tr
                    key={index}
                    className="border-t border-gray-200 hover:bg-gray-100"
                  >
                    <td className="px-4 py-2">{workflow.name}</td>
                    <td className="px-4 py-2">
                      <div className="flex gap-2">
                        <ActionIcon actionType={workflow.trigger.type} />
                        {workflow.trigger.type}
                      </div>
                    </td>
                    <td className="px-4 py-2">
                      <div className="flex gap-2">
                        {workflow.actions.map((actionType, idx) => (
                          <ActionIcon key={idx} actionType={actionType} />
                        ))}
                      </div>
                    </td>
                    <td className="px-4 py-2">{workflow.runs}</td>
                    <td className="px-4 py-2">
                      <label
                        htmlFor="toggle"
                        className="flex cursor-pointer items-center"
                      >
                        <div className="relative">
                          <input
                            type="checkbox"
                            id="toggle"
                            className="peer sr-only"
                            checked={workflow.isActive}
                            onChange={(e) => handleActiveChange(e, workflow.id)}
                          />
                          <div className="block h-6 w-10 rounded-full bg-gray-300"></div>
                          <div className="absolute left-1 top-1 h-4 w-4 rounded-full bg-gray-600 transition peer-checked:translate-x-full peer-checked:bg-green-500"></div>
                        </div>
                      </label>
                    </td>
                    <td className="px-4 py-2">
                      <div className="flex justify-center text-center">
                        <button
                          onClick={() => {
                            navigate(`/dashboard/workflow/${workflow.id}`);
                          }}
                        >
                          <FiEye className="h-6 w-6 text-center text-gray-500" />
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};

export default WorkflowOverview;
