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
import WorkflowOverview from "./pages/dashboard/WorkflowOverview";
import Payment from "./pages/dashboard/Payment";
import PaymentSuccess from "./pages/dashboard/PaymentSuccess";
import Payments from "./pages/dashboard/Payments";
import Landing from "./pages/Landing";
import NotFound from "./pages/NotFound";

function App() {
  return (
    <Router>
      <AppProvider>
        <ToastContainer />
        <Routes>
          {/* Public Routes */}
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/" element={<Landing />} />
          <Route path="*" element={<NotFound />} />

          {/* Protected Routes */}
          <Route element={<PrivateRoute />}>
            <Route
              path="/connect/google/callback"
              element={<GoogleCallback />}
            />
            <Route path="/dashboard" element={<DashboardLayout />}>
              <Route index element={<Home />} />
              <Route path="profile" element={<Profile />} />
              <Route path="connect" element={<ConnectAuth />} />
              <Route path="overview" element={<WorkflowOverview />} />
              <Route path="payments" element={<Payments />} />
              <Route path="payment" element={<Payment />} />
            </Route>
            <Route path="/payment-success" element={<PaymentSuccess />} />
            <Route path="/dashboard/workflow/:id" element={<Workflow />} />
            <Route path="/dashboard/workflow" element={<Workflow />} />
          </Route>
        </Routes>
      </AppProvider>
    </Router>
  );
}

export default App;
