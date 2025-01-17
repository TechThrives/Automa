import React, { useState } from "react";
import toast from "react-hot-toast";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import axiosConfig from "../../utils/axiosConfig";
import { useAppContext } from "../../context/AppContext";
import { Link, Navigate } from "react-router-dom";

export default function SignUp() {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    password: "",
    confirmPassword: "",
  });

  const { user } = useAppContext();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    try {
      const response = await axiosConfig.post("/api/auth/sign-up", formData);
      if (response.data) {
        toast.success(response.data.message);
      }
    } catch (error) {
      toast.error(error.response.data.message);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  return user ? (
    <Navigate to="/dashboard" />
  ) : (
    <div className="flex min-h-screen bg-gray-50">
      <div className="mx-auto flex w-full max-w-md flex-col p-4 sm:p-6 lg:p-8">
        <div className="mb-6 mt-6 flex flex-col items-center justify-center gap-2">
          <img
            src="/logo-full.png"
            alt="Automa Logo"
            className="h-12 w-12 rounded-xl"
          />
          <span className="text-lg font-semibold">Automa</span>
        </div>

        <h1 className="mb-4 text-center text-xl font-semibold">Sign Up</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-1">
              <label
                htmlFor="firstName"
                className="block text-xs font-medium text-gray-700"
              >
                First Name
              </label>
              <input
                id="firstName"
                name="firstName"
                type="text"
                value={formData.firstName}
                onChange={handleInputChange}
                className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-[12px] shadow-sm focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
                placeholder="Enter First Name"
              />
            </div>
            <div className="space-y-1">
              <label
                htmlFor="lastName"
                className="block text-xs font-medium text-gray-700"
              >
                Last Name
              </label>
              <input
                id="lastName"
                name="lastName"
                type="text"
                value={formData.lastName}
                onChange={handleInputChange}
                className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-[12px] shadow-sm focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
                placeholder="Enter Last Name"
              />
            </div>
          </div>

          <div className="space-y-1">
            <label
              htmlFor="email"
              className="block text-xs font-medium text-gray-700"
            >
              Email
            </label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleInputChange}
              className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-[12px] shadow-sm focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="Enter Email"
            />
          </div>

          <div className="space-y-1">
            <label
              htmlFor="phoneNumber"
              className="block text-xs font-medium text-gray-700"
            >
              Phone Number
            </label>
            <input
              id="phoneNumber"
              name="phoneNumber"
              type="tel"
              value={formData.phoneNumber}
              onChange={handleInputChange}
              className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-[12px] shadow-sm focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
              placeholder="Enter Phone Number"
            />
          </div>

          <div className="space-y-1">
            <label
              htmlFor="password"
              className="block text-xs font-medium text-gray-700"
            >
              Password
            </label>
            <div className="relative">
              <input
                id="password"
                name="password"
                type={showPassword ? "text" : "password"}
                value={formData.password}
                onChange={handleInputChange}
                className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 pr-10 text-[12px] shadow-sm focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
                placeholder="Enter Password"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-2 top-2 text-gray-400 hover:text-gray-600"
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? (
                  <FaEyeSlash className="h-4 w-4" />
                ) : (
                  <FaEye className="h-4 w-4" />
                )}
              </button>
            </div>
          </div>

          <div className="space-y-1">
            <label
              htmlFor="confirmPassword"
              className="block text-xs font-medium text-gray-700"
            >
              Confirm Password
            </label>
            <div className="relative">
              <input
                id="confirmPassword"
                name="confirmPassword"
                type={showConfirmPassword ? "text" : "password"}
                value={formData.confirmPassword}
                onChange={handleInputChange}
                className="h-9 w-full rounded-md border border-gray-300 bg-white px-3 py-2 pr-10 text-[12px] shadow-sm focus:border-teal-500 focus:outline-none focus:ring-2 focus:ring-teal-500"
                placeholder="Confirm Password"
              />
              <button
                type="button"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="absolute right-2 top-2 text-gray-400 hover:text-gray-600"
                aria-label={
                  showConfirmPassword
                    ? "Hide confirm password"
                    : "Show confirm password"
                }
              >
                {showConfirmPassword ? (
                  <FaEyeSlash className="h-4 w-4" />
                ) : (
                  <FaEye className="h-4 w-4" />
                )}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="h-9 w-full rounded-md bg-teal-600 px-4 py-2 text-sm font-medium text-white hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2"
          >
            Sign Up
          </button>

          <p className="text-center text-xs text-gray-600">
            Already have an account?{" "}
            <Link to="/signin" className="text-teal-600 hover:underline">
              Sign In Now
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
