import React, { useEffect, useRef } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import { toast } from "react-hot-toast";
import axiosConfig from "../../utils/axiosConfig";

const PaymentSuccess = () => {
  const navigate = useNavigate();
  const query = new URLSearchParams(useLocation().search);
  const sessionId = query.get("session_id");

  const isRequestSent = useRef(false);

  const createInvoice = async (sessionId) => {
    try {
      const response = await axiosConfig.post(
        `/api/payment/process-credit-purchase?sessionId=${sessionId}`
      );
      if (response.data) {
        toast.success(response.data.message);
        setTimeout(() => {
          navigate("/dashboard/payments");
        }, 3000);
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "An error occurred");
      navigate("/dashboard");
    }
  };

  useEffect(() => {
    if (sessionId && !isRequestSent.current) {
      isRequestSent.current = true;
      createInvoice(sessionId);
    } else if (!sessionId) {
      navigate("/dashboard");
    }
  }, [sessionId, navigate]);

  return (
    <div className="min-h-md mt-8 flex items-center">
      <div className="mx-auto flex max-w-sm flex-col rounded-lg border bg-white p-5 text-center">
        <img
          src="/assets/success.gif"
          className="mx-auto h-40 w-40"
          alt="Success"
        />
        <div className="text-xl font-bold">Payment Successful</div>
        <div className="text-md mt-2 font-bold">
          Your Credits will be added to your account soon.
        </div>
        <div className="mt-5 text-base">
          For any further queries, please contact us at{" "}
          <Link to="mailto: user@example.com">user@example.com</Link>
        </div>
      </div>
    </div>
  );
};

export default PaymentSuccess;
