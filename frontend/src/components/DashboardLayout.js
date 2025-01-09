import React, { useState, useEffect } from "react";
import Sidebar from "./Sidebar";
import { FiMenu } from "react-icons/fi";
import { Navigate, Outlet } from "react-router-dom";
import { useAppContext } from "../context/AppContext";

const DashboardLayout = () => {
  const location = window.location.pathname;
  const { user, isLoading } = useAppContext();
  const headerTitle = location.split("dashboard/")[1] || "Dashboard";
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [header, setHeader] = useState({
    title: headerTitle.charAt(0).toUpperCase() + headerTitle.slice(1),
  });

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  useEffect(() => {
    const headerTitle = location.split("dashboard/")[1] || "Dashboard";
    setHeader({
      title: headerTitle.charAt(0).toUpperCase() + headerTitle.slice(1),
    });
  }, []);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 768) {
        setIsMobileMenuOpen(false);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  return isLoading ? (
    <>"Loading ...."</>
  ) : user ? (
    <div className="flex h-screen overflow-hidden bg-white">
      <Sidebar
        isMobileMenuOpen={isMobileMenuOpen}
        toggleMobileMenu={toggleMobileMenu}
      />

      {/* Main Content */}
      <div className="flex flex-1 flex-col overflow-hidden">
        <header className="z-10 bg-white shadow-sm">
          <div className="mx-auto max-w-7xl px-4 py-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-semibold text-gray-900">
                {header.title}
              </h1>
              <button
                onClick={toggleMobileMenu}
                className="rounded-md p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500 md:hidden"
              >
                <span className="sr-only">Open sidebar</span>
                <FiMenu className="h-6 w-6" aria-hidden="true" />
              </button>
              <div className="flex items-center">
                <img
                  className="h-8 w-8 rounded-full"
                  src={user.profileImageUrl}
                  alt=""
                />
                <div className="flex flex-col font-semibold">
                <span className="ml-2">{user.firstName} {user.lastName}</span>
                <span className="ml-2 text-sm font-normal">Credits: {user.credits}</span>
                </div>
              </div>
            </div>
          </div>
        </header>
        <main className="flex-1 overflow-y-auto bg-gray-100 p-4">
          <div className="mx-auto max-w-7xl">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  ) : (
    <Navigate to="/signin" />
  );
};

export default DashboardLayout;
