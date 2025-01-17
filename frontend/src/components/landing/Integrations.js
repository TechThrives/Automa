import React from "react";
import {
  IoIosMail,
  IoIosCloud,
  IoIosCalendar,
  IoIosChatbubbles,
  IoIosContacts,
  IoIosDocument,
  IoIosKey,
  IoIosLink,
  IoIosMap,
  IoIosNotifications,
  IoIosPaper,
  IoIosSettings,
  IoIosStats,
  IoIosTimer,
  IoIosPeople,
  IoIosCamera,
} from "react-icons/io";

const integrations = [
  { name: "GoogleMail", icon: IoIosMail, color: "text-red-500" },
  { name: "GoogleDrive", icon: IoIosCloud, color: "text-blue-500" },
  { name: "GoogleCalendar", icon: IoIosCalendar, color: "text-green-500" },
  { name: "GoogleChat", icon: IoIosChatbubbles, color: "text-teal-500" },
  { name: "GoogleContacts", icon: IoIosContacts, color: "text-purple-500" },
  { name: "GoogleDocs", icon: IoIosDocument, color: "text-yellow-500" },
  { name: "GoogleSecurity", icon: IoIosKey, color: "text-gray-600" },
  { name: "GoogleLinks", icon: IoIosLink, color: "text-blue-700" },
  { name: "GoogleMaps", icon: IoIosMap, color: "text-orange-500" },
  {
    name: "GoogleNotifications",
    icon: IoIosNotifications,
    color: "text-pink-500",
  },
  { name: "GoogleSheets", icon: IoIosPaper, color: "text-green-600" },
  { name: "GoogleSettings", icon: IoIosSettings, color: "text-gray-700" },
  { name: "GoogleAnalytics", icon: IoIosStats, color: "text-indigo-500" },
  { name: "GoogleTimer", icon: IoIosTimer, color: "text-red-400" },
  { name: "GooglePeople", icon: IoIosPeople, color: "text-violet-500" },
  { name: "GooglePhotos", icon: IoIosCamera, color: "text-blue-600" },
];

const IntegrationIcon = ({ integration }) => {
  return (
    <div className="mx-4 flex items-center justify-center">
      <div className="flex h-16 w-16 items-center justify-center rounded-lg bg-white shadow-sm transition-shadow duration-300 hover:scale-110 hover:shadow-md">
        <integration.icon className={`h-8 w-8 ${integration.color}`} />
      </div>
    </div>
  );
};

const IntegrationRow = ({ integrations, direction }) => {
  const tripleArray = [...integrations, ...integrations, ...integrations];

  return (
    <div className="integration-row relative w-full overflow-hidden py-2">
      <div className={`flex animate-scroll-${direction}`}>
        {tripleArray.map((integration, index) => (
          <IntegrationIcon
            key={`${integration.name}-${index}`}
            integration={integration}
          />
        ))}
      </div>
    </div>
  );
};

const Integrations = () => {
  const halfLength = Math.ceil(integrations.length / 2);
  const firstRow = integrations.slice(0, halfLength);
  const secondRow = integrations.slice(halfLength);

  return (
    <section className="bg-gray-50 px-4 py-20 sm:px-6 lg:px-8">
      <div className="mx-auto max-w-7xl text-center">
        <h2 className="mb-16 text-4xl font-bold tracking-tight text-gray-900">
          Over <span className="text-teal-500">12</span> Integrations
        </h2>
        <div className="space-y-6">
          <IntegrationRow integrations={firstRow} direction="left" />
          <IntegrationRow integrations={secondRow} direction="right" />
        </div>
      </div>
    </section>
  );
};

export default Integrations;
