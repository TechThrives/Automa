import { useState } from "react";
import { MdClose, MdMenu } from "react-icons/md";
import { Link } from "react-router-dom";
import { useAppContext } from "../../context/AppContext";

const Header = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { user } = useAppContext();

  return (
    <header className="fixed left-0 right-0 top-0 z-50 border-b border-gray-100 bg-white/80 backdrop-blur-sm">
      <nav className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          <div className="flex items-center">
            <Link
              to="/"
              className="flex gap-2 text-xl font-semibold text-gray-800"
            >
              <img src="/logo.png" alt="Automa" className="h-8" />
              Automa
            </Link>
          </div>

          <div className="hidden items-center space-x-8 md:flex">
            <Link to="#" className="text-sm text-gray-600 hover:text-gray-800">
              Product
            </Link>
            <Link to="#" className="text-sm text-gray-600 hover:text-gray-800">
              Solutions
            </Link>
            <Link to="#" className="text-sm text-gray-600 hover:text-gray-800">
              Docs
            </Link>
          </div>

          <div className="hidden items-center space-x-4 md:flex">
            {user ? (
              <Link
                to="/dashboard"
                className="rounded-md bg-teal-500 px-4 py-2 text-sm text-white hover:bg-teal-600"
              >
                Dashboard
              </Link>
            ) : (
              <>
                <Link
                  to="/signin"
                  className="rounded-md px-4 py-2 text-sm text-gray-600 hover:text-gray-800"
                >
                  Sign in
                </Link>
                <Link
                  to="/signup"
                  className="rounded-md bg-teal-500 px-4 py-2 text-sm text-white hover:bg-teal-600"
                >
                  Get started free
                </Link>
              </>
            )}
          </div>

          <button
            onClick={() => setIsOpen(!isOpen)}
            className="rounded-md p-2 text-gray-600 hover:text-gray-800 md:hidden"
          >
            <span className="sr-only">Open menu</span>
            {isOpen ? (
              <MdClose className="h-6 w-6" />
            ) : (
              <MdMenu className="h-6 w-6" />
            )}
          </button>
        </div>

        {isOpen && (
          <div className="md:hidden">
            <div className="space-y-1 px-2 pb-3 pt-2">
              <Link
                to="#"
                className="block rounded-md px-3 py-2 text-base text-gray-600 hover:bg-teal-200 hover:text-gray-800"
              >
                Product
              </Link>
              <Link
                to="#"
                className="block rounded-md px-3 py-2 text-base text-gray-600 hover:bg-teal-200 hover:text-gray-800"
              >
                Solutions
              </Link>
              <Link
                to="#"
                className="block rounded-md px-3 py-2 text-base text-gray-600 hover:bg-teal-200 hover:text-gray-800"
              >
                Docs
              </Link>
            </div>
            <div className="flex flex-col space-y-2 px-2 pb-3 pt-2 text-center">
              <Link
                to="/signin"
                className="w-full rounded-md px-3 py-2 text-base text-gray-600 hover:bg-teal-200 hover:text-gray-800"
              >
                Sign in
              </Link>
              <Link
                to="/signup"
                className="w-full rounded-md bg-teal-500 px-3 py-2 text-white hover:bg-teal-600"
              >
                Get started free
              </Link>
            </div>
          </div>
        )}
      </nav>
    </header>
  );
};

export default Header;
