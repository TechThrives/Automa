import React from "react";
import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div class="grid h-screen place-content-center px-4">
      <div class="text-center">
        <h1 class="mt-6 text-2xl font-bold tracking-tight text-gray-800 sm:text-4xl">
          Uh-oh!
        </h1>
        <p class="mt-4 text-gray-500">
          We can't find that page. Go to{" "}
          <Link to="/" class="font-medium text-teal-600 hover:text-teal-500">
            Home
          </Link>
          .
        </p>
      </div>
    </div>
  );
};

export default NotFound;
