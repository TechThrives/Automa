import React from "react";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="border-gray-200 bg-gray-50 text-sm">
      <div className="mx-auto max-w-7xl px-4 py-4">
        <div className="flex flex-col items-center justify-between md:flex-row">
          <div className="flex items-center space-x-6">
            <Link to="/" className="text-gray-500 hover:text-gray-900">
              Terms
            </Link>
            <Link to="/" className="text-gray-500 hover:text-gray-900">
              Privacy
            </Link>
            <Link to="/" className="text-gray-500 hover:text-gray-900">
              Security
            </Link>
          </div>
          <p className="mt-4 text-sm text-gray-400 md:mt-0">
            &copy; {new Date().getFullYear()} Automa. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
}

export default Footer
