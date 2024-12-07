import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/dashboard/Home";
import ConnectAuth from "./pages/dashboard/ConnectAuth";
import Workflow from "./pages/dashboard/Workflow";
import SignIn from "./pages/auth/SignIn";
import SignUp from "./pages/auth/SignUp";
import DashboardLayout from "./components/DashboardLayout";
import Profile from "./pages/dashboard/Profile";
import GoogleCallback from "./pages/dashboard/GoogleCallback";
import { AppProvider } from "./context/AppContext";
import PrivateRoute from "./utils/PrivateRoute";
import ToastContainer from "./components/ToastContainer";

function App() {
  return (
    <Router>
      <AppProvider>
        <ToastContainer />
        <Routes>
          {/* Public Routes */}
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup" element={<SignUp />} />

          {/* Protected Routes */}
          <Route element={<PrivateRoute />}>
            <Route path="/connect/google/callback" element={<GoogleCallback />} />
            <Route path="/dashboard" element={<DashboardLayout />}>
              <Route index element={<Home />} />
              <Route path="profile" element={<Profile />} />
              <Route path="connect" element={<ConnectAuth />} />
            </Route>
            <Route path="/dashboard/workflow/:id" element={<Workflow />} />
          </Route>
        </Routes>
      </AppProvider>
    </Router>
  );
}

export default App;
