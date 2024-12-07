import React, { useEffect, useRef, useState } from "react";
import { useSearchParams } from "react-router-dom";
import axiosConfig from "../../utils/axiosConfig";

// Utility to capitalize the first letter and replace underscores
const formatMessage = (message) => {
  if (!message) return "Something went wrong. Please try again.";
  return message
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
};

const GoogleCallback = () => {
  const [data, setData] = useState(null); // Start with null to differentiate between loading and error states
  const [searchParams] = useSearchParams();
  const hasRun = useRef(false);

  useEffect(() => {
    if (hasRun.current) return; // Prevent double execution in Strict Mode
    hasRun.current = true;

    const getAuthorized = async () => {
      try {
        const response = await axiosConfig.get(
          `api/google/callback?${searchParams.toString()}`
        );
        setData(response.data);
      } catch (error) {
        // Ensure safe access to error response
        const errorMessage =
          error.response?.data?.message || "Unable to process your request.";
        setData({ message: errorMessage });
      }
    };

    getAuthorized();
  }, [searchParams]);

  return (
    <div className="text-gray-800 h-screen flex justify-center items-center bg-gray-50">
      {/* Loading State */}
      {!data && <p className="text-lg font-medium">Loading, please wait...</p>}

      {/* Display the formatted message */}
      {data && (
        <p className="text-lg font-semibold text-center">
          {formatMessage(data.message)}
        </p>
      )}
    </div>
  );
};

export default GoogleCallback;
