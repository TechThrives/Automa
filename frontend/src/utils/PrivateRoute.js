import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAppContext } from "../context/AppContext";

const PrivateRoute = () => {
  const { user, isLoading } = useAppContext();

  return isLoading ? <>"Loading ...."</> : user ? <Outlet /> : <Navigate to="/signin" />;
};

export default PrivateRoute;