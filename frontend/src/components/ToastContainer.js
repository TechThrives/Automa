import React from "react";
import { Toaster } from "react-hot-toast";

const ToastContainer = () => {
  return (
    <Toaster
      position="top-right"
      reverseOrder={false}
      containerClassName=""
      containerStyle={{}}
      toastOptions={{
        className: "text-sm md:text-md select-none",
        duration: 3000,
      }}
    />
  );
};

export default ToastContainer;