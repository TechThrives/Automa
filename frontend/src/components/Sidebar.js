import React, { useState, useRef, useEffect } from "react";
import { FiLink } from "react-icons/fi";
import {
  FiMenu,
  FiHome,
  FiBarChart,
  FiSettings,
  FiChevronDown,
  FiChevronUp,
  FiUser,
  FiLogOut,
  FiPieChart,
  FiTrendingUp,
  FiClock,
} from "react-icons/fi";
import { IoMdClose } from "react-icons/io";
import { useAppContext } from "../context/AppContext";
import { useNavigate } from "react-router-dom";

const Sidebar = ({ isMobileMenuOpen, toggleMobileMenu }) => {
  const [isExpanded, setIsExpanded] = useState(
    window.innerWidth > 768 ? false : true
  );
  const [expandedSubMenus, setExpandedSubMenus] = useState({});
  const [activeMenu, setActiveMenu] = useState(null);
  const sidebarRef = useRef(null);
  const navigate = useNavigate();

  const { handleLogout } = useAppContext();

  const menuItems = [
    {
      name: "Dashboard",
      icon: FiHome,
      href: "/dashboard",
    },
    {
      name: "Analytics",
      icon: FiBarChart,
      subItems: [
        { name: "Overview", href: "/analytics/overview", icon: FiPieChart },
        { name: "Reports", href: "/analytics/reports", icon: FiTrendingUp },
        { name: "Real-time", href: "/analytics/real-time", icon: FiClock },
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

  const toggleSidebar = () => {
    if (window.innerWidth > 768) {
      setIsExpanded(!isExpanded);
    } else {
      toggleMobileMenu();
    }
    setActiveMenu(null);
  };

  const toggleSubMenu = (menuName) => {
    setExpandedSubMenus((prev) => ({
      ...prev,
      [menuName]: !prev[menuName],
    }));
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
      className={`fixed md:static inset-y-0 left-0 z-30 flex flex-col justify-between bg-white text-gray-800 transition-all duration-300 ease-in-out ${
        isExpanded ? "w-64" : "w-20"
      } ${
        isMobileMenuOpen
          ? "translate-x-0"
          : "-translate-x-full md:translate-x-0"
      }`}
    >
      <div>
        <div
          className={`flex items-center ${
            isExpanded ? "justify-between pl-5" : "justify-center"
          } p-4`}
        >
          {isExpanded && <span className="text-xl font-semibold">Menu</span>}
          <button
            onClick={toggleSidebar}
            className="p-2 rounded-md hover:bg-gray-200"
            aria-label="Toggle sidebar"
          >
            {isExpanded ? <IoMdClose size={24} /> : <FiMenu size={24} />}
          </button>
        </div>
        <nav>
          <ul className="space-y-2 py-4">
            {menuItems.map((item) => (
              <li key={item.name} className="relative">
                <div className="px-4">
                  {item.subItems ? (
                    <div>
                      <button
                        onClick={() => toggleSubMenu(item.name)}
                        className={`flex items-center w-full p-2 rounded-md hover:bg-gray-200 ${
                          isExpanded ? "justify-between" : "justify-center"
                        }`}
                        aria-expanded={expandedSubMenus[item.name]}
                      >
                        <div className="flex items-center">
                          <item.icon className="w-6 h-6" aria-hidden="true" />
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
                      {((isExpanded && expandedSubMenus[item.name]) ||
                        (!isExpanded && activeMenu === item.name)) && (
                        <ul
                          className={`mt-2 space-y-1 max-h-52 overflow-y-auto hide-scrollbar ${
                            !isExpanded
                              ? "absolute left-full top-0 ml-2 bg-white rounded-md shadow-lg p-2 min-w-[200px]"
                              : ""
                          }`}
                        >
                          {item.subItems.map((subItem) => (
                            <li key={subItem.name}>
                              <button
                                className="flex w-full items-center pl-10 pr-4 py-2 rounded-md hover:bg-gray-200"
                                onClick={() => {
                                  handleMenuItemClick(subItem);
                                }}
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
                      )}
                    </div>
                  ) : (
                    <div>
                      <button
                        className={`flex items-center w-full p-2 rounded-md hover:bg-gray-200 ${
                          isExpanded ? "justify-between" : "justify-center"
                        }`}
                        onClick={() => handleMenuItemClick(item)}
                        aria-expanded={expandedSubMenus[item.name]}
                      >
                        <div className="flex items-center">
                          <item.icon className="w-6 h-6" aria-hidden="true" />
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
