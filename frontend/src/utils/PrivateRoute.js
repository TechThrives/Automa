import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAppContext } from "../context/AppContext";
import Loader from "../components/Loader";

const PrivateRoute = () => {
  const { user, isLoading } = useAppContext();

  return isLoading ? <Loader /> : user ? <Outlet /> : <Navigate to="/signin" />;
};

export default PrivateRoute;