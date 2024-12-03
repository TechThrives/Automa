import React from "react";
import { FiUsers, FiDollarSign, FiTrendingUp } from "react-icons/fi";

const DashboardCard = ({ title, value, icon, color }) => {
  return (
    <div className="bg-white overflow-hidden shadow rounded-lg">
      <div className="p-5">
        <div className="flex items-center">
          <div className={`flex-shrink-0 ${color} rounded-md p-3`}>{icon}</div>
          <div className="ml-5 w-0 flex-1">
            <dl>
              <dt className="text-sm font-medium text-gray-500 truncate">
                {title}
              </dt>
              <dd className="text-3xl font-semibold text-gray-900">{value}</dd>
            </dl>
          </div>
        </div>
      </div>
    </div>
  );
};

export default function Dashboard() {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
      <DashboardCard
        title="Total Users"
        value="71,897"
        icon={<FiUsers className="h-6 w-6 text-white" />}
        color="bg-indigo-500"
      />
      <DashboardCard
        title="Revenue"
        value="$24,000"
        icon={<FiDollarSign className="h-6 w-6 text-white" />}
        color="bg-green-500"
      />
      <DashboardCard
        title="Conversion Rate"
        value="2.5%"
        icon={<FiTrendingUp className="h-6 w-6 text-white" />}
        color="bg-red-500"
      />
    </div>
  );
}
