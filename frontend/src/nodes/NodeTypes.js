import NodeWithLogo from "./design/NodeWithLogo";
import { isTrigger, Triggers, Actions, getIcon } from "../constants/ActionUtils";;

const nodeTypes = {
  [Triggers.RUNONCE]: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: getIcon(props.type),
        text: "Run Once",
        iconColor: "text-gray-500",
        isTrigger: isTrigger(props.type),
      }}
    />
  ),
  [Triggers.RUNDAILY]: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: getIcon(props.type),
        text: "Run Daily",
        iconColor: "text-gray-500",
        isTrigger: isTrigger(props.type),
      }}
    />
  ),
  [Actions.SENDMAIL]: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: getIcon(props.type),
        text: "Send Mail Node",
        iconColor: "text-green-500",
        isTrigger: false, // Directly mark this as not a trigger
      }}
    />
  ),
};

export default nodeTypes;
