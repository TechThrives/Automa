import NodeWithLogo from "./design/NodeWithLogo";
import {Triggers, Actions } from "../constants/ActionUtils";;

const nodeTypes = {
  [Triggers.RUNONCE]: NodeWithLogo,
  [Triggers.RUNDAILY]: NodeWithLogo,
  [Actions.SENDMAIL]: NodeWithLogo,
  [Actions.GETTODAYNEWS]: NodeWithLogo,
  [Actions.DOWNLOADVIDEO]: NodeWithLogo,
  [Actions.UPLOADVIDEO]: NodeWithLogo,
  [Actions.READSHEET]: NodeWithLogo,
};

export default nodeTypes;
