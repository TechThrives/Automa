import React, { useState, useEffect, useMemo } from "react";
import DashboardCard from "../../components/cards/DashboardCard";
import { IoPlayOutline } from "react-icons/io5";
import { BsCashStack } from "react-icons/bs";
import { MdCurrencyRupee, MdOutlineAccountTree } from "react-icons/md";
import axiosConfig from "../../utils/axiosConfig";
import { toast } from "react-hot-toast";
import { FiCreditCard } from "react-icons/fi";

const Payments = () => {
  const [payments, setPayments] = useState([]);

  useEffect(() => {
    const fetchPayments = async () => {
      try {
        const response = await axiosConfig.get("/api/payment/my");
        setPayments(response.data);
      } catch (error) {
        console.error("Error fetching payments:", error);
        toast.error("Error fetching payments");
      }
    };

    fetchPayments();
  }, []);

  const totalCreditsPurchased = useMemo(() => {
    return payments.reduce((total, payment) => total + payment.creditsPurchased, 0);
  }, [payments]);

  const totalGrandTotal = useMemo(() => {
    return payments.reduce((total, payment) => total + payment.grandTotal, 0);
  }, [payments]);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const options = { year: "numeric", month: "long", day: "numeric" };
    return date.toLocaleDateString("en-US", options);
  };

  return (
    <>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <DashboardCard
          title="Total Credits Purchased"
          value={totalCreditsPurchased}
          icon={<FiCreditCard className="h-6 w-6 text-white" />}
          color="bg-indigo-500"
        />
        <DashboardCard
          title="Total Payments"
          value={payments.length}
          icon={<BsCashStack className="h-6 w-6 text-white" />}
          color="bg-emerald-500"
        />
        <DashboardCard
          title="Total Grand Total"
          value={totalGrandTotal}
          icon={<MdCurrencyRupee className="h-6 w-6 text-white" />}
          color="bg-red-500"
        />
      </div>
      <div className="mb-6 mt-4 bg- flex w-full min-w-0 flex-col break-words rounded-lg bg-white p-4 shadow-lg">
        <div className="overflow-x-auto">
          <table className="min-w-full select-none overflow-hidden rounded-lg bg-white shadow-md">
            <thead>
              <tr className="bg-gray-200 text-gray-600">
                <th className="px-4 py-2 text-left">Credits Purchased</th>
                <th className="px-4 py-2 text-left">Grand Total</th>
                <th className="px-4 py-2 text-left">Date</th>
                <th className="px-4 py-2 text-left">Method</th>
                <th className="px-4 py-2 text-left">Status</th>
              </tr>
            </thead>
            <tbody>
            {payments.length === 0 && (
                <tr>
                  <td className="px-4 py-2 text-center font-medium text-gray-600" colSpan="5">
                    No payments found.
                  </td>
                </tr>
              )}
              {payments.map((payment, index) => {
                return (
                  <tr
                    key={index}
                    className="border-t border-gray-200 hover:bg-gray-100"
                  >
                    <td className="px-4 py-2">{payment.creditsPurchased}</td>
                    <td className="px-4 py-2">₹ {payment.grandTotal} INR</td>
                    <td className="px-4 py-2">{formatDate(payment.date)}</td>
                    <td className="px-4 py-2">{payment.method}</td>
                    <td className="px-4 py-2">{payment.status}</td>
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

export default Payments;
