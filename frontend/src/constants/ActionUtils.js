import { FiClock, FiZap } from "react-icons/fi";
import { IoIosMail } from "react-icons/io";

const Triggers = {
    RUNONCE:"RUNONCE",
    RUNDAILY:"RUNDAILY",
}

const Actions = {
    SENDMAIL: "SENDMAIL",
}

const IconMapping = {
  [Triggers.RUNONCE]: FiClock,
  [Triggers.RUNDAILY]: FiClock,
  [Actions.SENDMAIL]: IoIosMail,
};

const isTrigger = (actionType) => Object.values(Triggers).includes(actionType);
const isAction = (actionType) => Object.values(Actions).includes(actionType);
const getIcon = (actionType) => IconMapping[actionType] || FiZap;

export { isTrigger, isAction, Actions, Triggers, getIcon };