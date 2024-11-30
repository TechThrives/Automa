import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ConnectAuth from "./pages/dashboard/ConnectAuth";
import Workflow from "./pages/dashboard/Workflow";
import SignIn from "./pages/auth/SignIn";
import SignUp from "./pages/auth/SignUp";

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/dashboard/connect" element={<ConnectAuth />} />
          <Route path="/dashboard/workflow/:id" element={<Workflow />} />
        </Routes>
    </Router>
  );
}

export default App;
