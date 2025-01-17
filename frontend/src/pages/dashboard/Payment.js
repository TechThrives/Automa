import React, { useState } from "react";
import toast from "react-hot-toast";
import { loadStripe } from "@stripe/stripe-js";
import axiosConfig from "../../utils/axiosConfig";
import { useLocation } from "react-router-dom";

const Payment = () => {
  const query = new URLSearchParams(useLocation().search);
  const chosenPackage = query.get("package");
  const packages = {
    "Starter": 50,
    "Pro": 300,
    "Enterprise": 500
  }
  const [credits, setCredits] = useState(packages[chosenPackage] || 50);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleCreditsChange = (e) => {
    const value = Number(e.target.value);
    setCredits(value);

    if (value < 50) {
      setError("Minimum 50 credits required");
    } else if (value > 500) {
      setError("Maximum 500 credits allowed");
    } else {
      setError("");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (error) {
      toast.error("Please enter a valid number of credits.");
      return;
    }

    setLoading(true);

    try {

      const checkoutResponse = await axiosConfig.post("/api/payment/checkout", {
        credits,
      });
      
      const stripe = await loadStripe(
        process.env.REACT_APP_STRIPE_PUBLISHABLE_KEY,
      );

      const { sessionId } = checkoutResponse.data;
      const { error } = await stripe.redirectToCheckout({ sessionId });
      if (error) {
        toast.error(error.message);
      }
    } catch (error) {
      console.error("Error creating checkout session:", error);
      if (error.response.data.details) {
        error.response.data.details.forEach((detail) => {
          toast.error(detail);
        });
      }
      toast.error("Payment failed. Please try again.");
    }

    setLoading(false);
  };

  return (
    <div className="mt-6 flex h-full w-full items-center justify-center">
      <div className="w-full max-w-md rounded-lg bg-white shadow-lg">
        <div className="border-b border-gray-200 p-6">
          <div className="flex items-center space-x-2">
            <svg
              className="h-6 w-6 text-gray-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"
              />
            </svg>
            <h2 className="text-md font-semibold text-gray-800">
              Purchase Credits
            </h2>
          </div>
        </div>

        <div className="p-6">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label
                htmlFor="credits"
                className="block text-xs font-medium text-gray-700"
              >
                Number of Credits
              </label>
              <input
                id="credits"
                type="number"
                name="credits"
                value={credits}
                onChange={handleCreditsChange}
                min="50"
                max="500"
                className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-sm shadow-sm focus:border-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-500"
              />
              {error && <p className="mt-1 text-xs text-red-500">{error}</p>}
              <div className="flex items-center justify-between">
                <p className="text-sm text-gray-500">
                  Total: ₹ {credits.toFixed(2)} INR
                </p>
                <p className="text-xs text-gray-400">Range: 50-500 credits</p>
              </div>
            </div>
          </form>
        </div>

        <div className="border-t border-gray-200 p-6">
          <button
            onClick={handleSubmit}
            disabled={error || loading}
            className="h-9 w-full rounded-md bg-teal-600 px-4 py-2 text-sm font-medium text-white hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 disabled:cursor-not-allowed disabled:bg-gray-400"
          >
            {loading ? "Processing..." : "Pay Now"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Payment;
