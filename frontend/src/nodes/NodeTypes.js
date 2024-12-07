import NodeWithLogo from "./design/NodeWithLogo";
import IconMapping from "../constants/IconMapping";
import { isTrigger } from "../constants/ActionUtils";

const nodeTypes = {
  TIME: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: IconMapping[props.type],
        text: "Schedule Time",
        iconColor: "text-gray-500",
        isTrigger: isTrigger(props.type),
      }}
    />
  ),
  SENDMAIL: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: IconMapping[props.type],
        text: "Send Mail Node",
        iconColor: "text-green-500",
        isTrigger: isTrigger(props.type),
      }}
    />
  ),
};

export default nodeTypes;