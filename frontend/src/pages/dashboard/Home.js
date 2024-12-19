import React from "react";
import DashboardCard from "../../components/cards/DashboardCard";
import { FiUsers, FiDollarSign, FiTrendingUp } from "react-icons/fi";

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
