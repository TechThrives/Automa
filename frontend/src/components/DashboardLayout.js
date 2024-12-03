import React, { useState, useEffect } from "react";
import Sidebar from "./Sidebar";
import { FiMenu } from "react-icons/fi";
import { Outlet } from "react-router-dom";

const DashboardLayout = () => {
  const location = window.location.pathname;
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

  return (
    <div className="flex h-screen bg-white overflow-hidden">
      <Sidebar
        isMobileMenuOpen={isMobileMenuOpen}
        toggleMobileMenu={toggleMobileMenu}
      />

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="bg-white shadow-sm z-10">
          <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-semibold text-gray-900">
                {header.title}
              </h1>
              <button
                onClick={toggleMobileMenu}
                className="md:hidden p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500"
              >
                <span className="sr-only">Open sidebar</span>
                <FiMenu className="h-6 w-6" aria-hidden="true" />
              </button>
            </div>
          </div>
        </header>
        <main className="flex-1 overflow-y-auto bg-gray-100 p-4">
          <div className="max-w-7xl mx-auto">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};

export default DashboardLayout;
