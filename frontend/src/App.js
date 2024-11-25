import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ConnectAuth from "./pages/dashboard/ConnectAuth";
import Workflow from "./pages/dashboard/Workflow";

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/dashboard/connect" element={<ConnectAuth />} />
          <Route path="/dashboard/workflow/:id" element={<Workflow />} />
        </Routes>
    </Router>
  );
}

export default App;
