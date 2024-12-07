import React, { useState, useEffect, createContext, useContext } from "react";
import { useNavigate } from "react-router-dom";
import axiosConfig from "../utils/axiosConfig";

export const AppContext = createContext({});

export function AppProvider({ children }) {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [user, setUser] = useState(null);

  const checkAuth = async () => {
    setIsLoading(true);
    const token = localStorage.getItem('jwtToken');
    if (token) {
      try {
        const response = await axiosConfig.get("/api/user/me"
        );
        if (response.data) {
          setUser(response.data);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        setUser(null);
      } 
    }
    setIsLoading(false);
  };

  useEffect(() => {
    checkAuth();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    setUser(null);
    navigate("/signin");
  };

  if (isLoading) {
    return "Loading...";
  }

  return (
    <AppContext.Provider value={{ user, setUser, isLoading, setIsLoading, handleLogout }}>
      {children}
    </AppContext.Provider>
  );
}

export const useAppContext = () => {
  return useContext(AppContext);
};