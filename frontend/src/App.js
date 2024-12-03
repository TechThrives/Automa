import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/dashboard/Home";
import ConnectAuth from "./pages/dashboard/ConnectAuth";
import Workflow from "./pages/dashboard/Workflow";
import SignIn from "./pages/auth/SignIn";
import SignUp from "./pages/auth/SignUp";
import DashboardLayout from "./components/DashboardLayout";
import Profile from "./pages/dashboard/Profile";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<Home />} />
          <Route path="profile" element={<Profile />} />
          <Route path="connect" element={<ConnectAuth />} />
        </Route>
        <Route path="/dashboard/workflow/:id" element={<Workflow />} />
      </Routes>
    </Router>
  );
}

export default App;
