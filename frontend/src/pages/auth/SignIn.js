import React, { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { Link, Navigate, useNavigate } from "react-router-dom";
import axiosConfig from "../../utils/axiosConfig";
import toast from "react-hot-toast";
import { useAppContext } from "../../context/AppContext";

export default function SignIn() {
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const { user } = useAppContext();

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axiosConfig.post("/api/auth/sign-in", formData);
      if (response.data) {
        localStorage.setItem("jwtToken", response.data.jwtToken);
        toast.success("Signed in successfully");
        navigate("/dashboard");
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

        <h1 className="mb-4 text-center text-xl font-semibold">Sign In</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
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
              required
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
                required
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

          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <input
                id="remember-me"
                name="remember-me"
                type="checkbox"
                className="h-4 w-4 rounded border-gray-300 text-teal-600 focus:ring-teal-500"
              />
              <label
                htmlFor="remember-me"
                className="ml-2 block text-xs text-gray-900"
              >
                Remember me
              </label>
            </div>
            <div className="text-xs">
              <Link
                to="/forgot-password"
                className="font-medium text-teal-600 hover:text-teal-500"
              >
                Forgot your password?
              </Link>
            </div>
          </div>

          <button
            type="submit"
            className="h-9 w-full rounded-md bg-teal-600 px-4 py-2 text-sm font-medium text-white hover:bg-teal-700 focus:outline-none focus:ring-2 focus:ring-teal-500 focus:ring-offset-2"
          >
            Sign In
          </button>

          <p className="text-center text-xs text-gray-600">
            Don't have an account?{" "}
            <Link to="/signup" className="text-teal-600 hover:underline">
              Sign Up Now
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
