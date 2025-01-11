import React, { useEffect, useState } from "react";
import axiosConfig from "../utils/axiosConfig";
import { toast } from "react-hot-toast";
import { FiX, FiXCircle } from "react-icons/fi";

const Notifications = ({ setIsNotificationOpen }) => {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await axiosConfig.get("/api/notification/all");
        setNotifications(response.data);
      } catch (error) {
        console.error("Error fetching notifications:", error);
      }
    };

    fetchNotifications();
  }, []);

  const handleDeleteNotification = async (id) => {
    try {
      await axiosConfig.delete(`/api/notification/${id}`);

      setNotifications((prevNotifications) =>
        prevNotifications.filter((notification) => notification.id !== id)
      );

      toast.success("Notification deleted successfully");
    } catch (error) {
      console.error("Error deleting notification:", error);
    }
  };

  return (
    <div className="absolute right-6 top-16 z-20 mt-2 w-80 max-w-xs rounded-md bg-white shadow-lg overflow-hidden">
      <div className="flex items-center justify-between text-gray-900 px-4 py-2 rounded-t-md">
        <p className="text-sm font-medium">Notifications</p>
        <FiX
          className="h-6 w-6 cursor-pointer"
          aria-hidden="true"
          onClick={() => {
            setIsNotificationOpen(false);
          }}
        />
      </div>
      <div className="max-h-72 overflow-y-auto hide-scrollbar bg-white">
        {notifications.length === 0 ? (
          <p className="text-center text-sm font-semibold text-gray-500 py-10">
            No notifications
          </p>
        ) : (
          notifications.map((notification) => (
            <div
              key={notification.id}
              className="flex items-center justify-between px-4 py-4 border-b border-gray-200 hover:bg-gray-100 cursor-pointer"
            >
              <div className="flex items-center gap-2">
                <img
                  className="h-10 w-10 rounded-full object-cover"
                  src="/assets/settings.png"
                  alt="avatar"
                />
                <div className="text-sm text-gray-700">
                  <span className="font-semibold">{notification.title}</span>
                  <p className="text-xs text-gray-500">{notification.message}</p>
                </div>
              </div>
              <FiXCircle
                className="h-6 w-6 cursor-pointer text-red-500 hover:text-red-700"
                aria-hidden="true"
                onClick={(e) => {
                  e.stopPropagation();
                  handleDeleteNotification(notification.id);
                }}
              />
            </div>
          ))
        )}
      </div>

      {notifications.length > 0 && (
      <div
        className="block py-3 text-center text-sm font-semibold text-gray-600 cursor-pointer hover:bg-gray-100"
        onClick={() => {
        }}
      >
        See all notifications
      </div>
      )}
    </div>
  );
};

export default Notifications;
