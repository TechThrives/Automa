import React from "react";
import Header from "../components/landing/Header";
import Hero from "../components/landing/Hero";
import WorkflowDemo from "../components/landing/WorkflowDemo";
import Features from "../components/landing/Features";
import Integrations from "../components/landing/Integrations";
import Footer from "../components/landing/Footer";
import Pricing from "../components/landing/Pricing";

const Landing = () => {
  return (
    <div className="hide-scrollbar h-screen overflow-auto">
      <Header />
      <main>
        <Hero />
        <WorkflowDemo />
        <Features />
        <Integrations />
        <Pricing />
      </main>
      <Footer />
    </div>
  );
};

export default Landing;
