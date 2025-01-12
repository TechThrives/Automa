import React, { useState, useRef, useEffect } from "react";
import { FiLink, FiPieChart, FiPlus } from "react-icons/fi";
import {
  FiMenu,
  FiHome,
  FiSettings,
  FiChevronDown,
  FiChevronUp,
  FiUser,
  FiLogOut,
  FiShoppingCart,
} from "react-icons/fi";
import { BsCashStack } from "react-icons/bs";
import { GoWorkflow } from "react-icons/go";
import { IoMdClose } from "react-icons/io";
import { useAppContext } from "../context/AppContext";
import { useNavigate } from "react-router-dom";

const Sidebar = ({ isMobileMenuOpen, toggleMobileMenu }) => {
  const savedIsExpanded = window.innerWidth > 768 ? false : true;
  const initialIsExpanded =
    localStorage.getItem("isExpanded") === "true" ? true : savedIsExpanded;

  const savedExpandedSubMenus = localStorage.getItem("expandedSubMenus");
  const initialExpandedSubMenus = savedExpandedSubMenus
    ? JSON.parse(savedExpandedSubMenus)
    : {};

  const [isExpanded, setIsExpanded] = useState(initialIsExpanded);
  const [expandedSubMenus, setExpandedSubMenus] = useState(
    initialExpandedSubMenus,
  );
  const [activeMenu, setActiveMenu] = useState(null);
  const sidebarRef = useRef(null);
  const navigate = useNavigate();

  const { handleLogout } = useAppContext();

  const menuItems = [
    { name: "Dashboard", icon: FiHome, href: "/dashboard" },
    {
      name: "Workflow",
      icon: GoWorkflow,
      subItems: [
        { name: "Overview", href: "overview", icon: FiPieChart },
        { name: "Create New", href: "workflow", icon: FiPlus },
      ],
    },
    {
      name: "Payments",
      icon: BsCashStack,
      subItems: [
        { name: "Overview", href: "/dashboard/payments", icon: FiPieChart },
        {
          name: "Buy Credits",
          href: "/dashboard/payment",
          icon: FiShoppingCart,
        },
      ],
    },
    {
      name: "Settings",
      icon: FiSettings,
      subItems: [
        { name: "Profile", href: "/dashboard/profile", icon: FiUser },
        { name: "Connect", href: "/dashboard/connect", icon: FiLink },
        { name: "Logout", onClick: handleLogout, icon: FiLogOut },
      ],
    },
  ];

  useEffect(() => {
    localStorage.setItem("isExpanded", isExpanded);
  }, [isExpanded]);

  useEffect(() => {
    localStorage.setItem("expandedSubMenus", JSON.stringify(expandedSubMenus));
  }, [expandedSubMenus]);

  const toggleSidebar = () => {
    if (window.innerWidth > 768) {
      setIsExpanded(!isExpanded);
    } else {
      toggleMobileMenu();
    }
    setActiveMenu(null);
  };

  const toggleSubMenu = (menuName) => {
    setExpandedSubMenus((prev) => ({ ...prev, [menuName]: !prev[menuName] }));
    setActiveMenu(menuName);
  };

  const handleMenuItemClick = (item) => {
    if (item.onClick) {
      item.onClick();
    }
    if (item.href) {
      navigate(item.href);
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (sidebarRef.current && !sidebarRef.current.contains(event.target)) {
        setActiveMenu(null);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 768) {
        setIsExpanded(true);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  return (
    <div
      ref={sidebarRef}
      className={`fixed inset-y-0 left-0 z-30 flex flex-col justify-between bg-white text-gray-800 transition-all duration-300 ease-in-out md:static ${isExpanded ? "w-64" : "w-20"} ${
        isMobileMenuOpen
          ? "translate-x-0"
          : "-translate-x-full md:translate-x-0"
      }`}
    >
      <div>
        <div
          className={`flex items-center ${isExpanded ? "justify-between pl-5" : "justify-center"} p-4`}
        >
          {isExpanded && <span className="text-xl font-semibold">Menu</span>}
          <button
            onClick={toggleSidebar}
            className="rounded-md p-2 hover:bg-gray-200"
            aria-label="Toggle sidebar"
          >
            {isExpanded ? <IoMdClose size={24} /> : <FiMenu size={24} />}
          </button>
        </div>
        <nav>
          <ul className="hide-scrollbar max-h-[85vh] space-y-2 overflow-y-auto py-4">
            {menuItems.map((item) => (
              <li key={item.name} className="relative">
                <div className="px-4">
                  {item.subItems ? (
                    <div>
                      <button
                        onClick={() => toggleSubMenu(item.name)}
                        className={`flex w-full items-center rounded-md p-2 hover:bg-gray-200 ${isExpanded ? "justify-between" : "justify-center"}`}
                        aria-expanded={expandedSubMenus[item.name]}
                      >
                        <div className="flex items-center">
                          <item.icon className="h-6 w-6" aria-hidden="true" />
                          {isExpanded && (
                            <span className="ml-3">{item.name}</span>
                          )}
                        </div>
                        {isExpanded &&
                          (expandedSubMenus[item.name] ? (
                            <FiChevronUp aria-hidden="true" />
                          ) : (
                            <FiChevronDown aria-hidden="true" />
                          ))}
                      </button>
                      {(isExpanded && expandedSubMenus[item.name]) ||
                      (!isExpanded && activeMenu === item.name) ? (
                        <ul
                          className={`hide-scrollbar mt-2 space-y-1 ${!isExpanded ? "fixed left-full -mt-6 ml-2 max-h-52 min-w-[200px] overflow-y-auto rounded-md bg-white p-2 shadow-lg" : ""}`}
                        >
                          {item.subItems.map((subItem) => (
                            <li key={subItem.name}>
                              <button
                                className="flex w-full items-center rounded-md py-2 pl-10 pr-4 hover:bg-gray-200"
                                onClick={() => handleMenuItemClick(subItem)}
                              >
                                <subItem.icon
                                  className="mr-2 h-5 w-5"
                                  aria-hidden="true"
                                />
                                <span>{subItem.name}</span>
                              </button>
                            </li>
                          ))}
                        </ul>
                      ) : null}
                    </div>
                  ) : (
                    <div>
                      <button
                        className={`flex w-full items-center rounded-md p-2 hover:bg-gray-200 ${isExpanded ? "justify-between" : "justify-center"}`}
                        onClick={() => handleMenuItemClick(item)}
                        aria-expanded={expandedSubMenus[item.name]}
                      >
                        <div className="flex items-center">
                          <item.icon className="h-6 w-6" aria-hidden="true" />
                          {isExpanded && (
                            <span className="ml-3">{item.name}</span>
                          )}
                        </div>
                      </button>
                    </div>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </nav>
      </div>
    </div>
  );
};

export default Sidebar;
