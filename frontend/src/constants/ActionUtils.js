import React from "react";
import { FiClock, FiZap } from "react-icons/fi";
import { IoIosMail } from "react-icons/io";
import { FaNewspaper, FaGoogleDrive, FaYoutube } from "react-icons/fa";
import { SiGooglesheets } from "react-icons/si";

const Triggers = {
  RUNONCE: "RUNONCE",
  RUNDAILY: "RUNDAILY",
};

const Actions = {
  SENDMAIL: "SENDMAIL",
  GETTODAYNEWS: "GETTODAYNEWS",
  DOWNLOADVIDEO: "DOWNLOADVIDEO",
  UPLOADVIDEO: "UPLOADVIDEO",
  READSHEET: "READSHEET",
};

const IconMapping = {
  [Triggers.RUNONCE]: <FiClock className={`h-6 w-6 text-gray-500`} />,
  [Triggers.RUNDAILY]: <FiClock className={`h-6 w-6 text-gray-500`} />,
  [Actions.SENDMAIL]: <IoIosMail className={`h-6 w-6 text-green-500`} />,
  [Actions.GETTODAYNEWS]: <FaNewspaper className={`h-6 w-6 text-blue-500`} />,
  [Actions.DOWNLOADVIDEO]: <FaGoogleDrive className={`h-6 w-6 text-yellow-500`} />,
  [Actions.UPLOADVIDEO]: <FaYoutube className={`h-6 w-6 text-red-500`} />,
  [Actions.READSHEET]: <SiGooglesheets className={`h-6 w-6 text-green-500`} />,
};

const isTrigger = (actionType) => Object.values(Triggers).includes(actionType);
const isAction = (actionType) => Object.values(Actions).includes(actionType);

const ActionIcon = ({ actionType, className }) => {
  const icon = IconMapping[actionType] || <FiZap className="h-6 w-6 text-gray-800" />;
  return React.cloneElement(icon, { className: `${icon.props.className} ${className}` });
};

export { isTrigger, isAction, Actions, Triggers, ActionIcon };
