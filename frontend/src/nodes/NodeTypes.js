import NodeWithLogo from "./design/NodeWithLogo";
import {Triggers, Actions } from "../constants/ActionUtils";;

const nodeTypes = {
  [Triggers.RUNONCE]: NodeWithLogo,
  [Triggers.RUNDAILY]: NodeWithLogo,
  [Actions.SENDMAIL]: NodeWithLogo,
};

export default nodeTypes;
