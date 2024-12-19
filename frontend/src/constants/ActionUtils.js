import React from "react";
import { FiClock, FiZap } from "react-icons/fi";
import { IoIosMail } from "react-icons/io";

const Triggers = {
  RUNONCE: "RUNONCE",
  RUNDAILY: "RUNDAILY",
};

const Actions = {
  SENDMAIL: "SENDMAIL",
};

const IconMapping = {
  [Triggers.RUNONCE]: <FiClock className={`h-6 w-6 text-gray-500`} />,
  [Triggers.RUNDAILY]: <FiClock className={`h-6 w-6 text-gray-500`} />,
  [Actions.SENDMAIL]: <IoIosMail className={`h-6 w-6 text-green-500`} />,
};

const isTrigger = (actionType) => Object.values(Triggers).includes(actionType);
const isAction = (actionType) => Object.values(Actions).includes(actionType);

const ActionIcon = ({ actionType, className }) => {
  const icon = IconMapping[actionType] || <FiZap className="h-6 w-6 text-gray-800" />;
  return React.cloneElement(icon, { className: `${icon.props.className} ${className}` });
};

export { isTrigger, isAction, Actions, Triggers, ActionIcon };
