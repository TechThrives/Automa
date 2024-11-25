import { FaYoutube, FaTwitter, FaFacebook } from "react-icons/fa";
import NodeWithLogo from "./design/NodeWithLogo";

export const nodeTypes = {
  youtubeInfo: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: FaYoutube,
        text: "Youtube Info Node",
        iconColor: "text-red-500",
      }}
    />
  ),
  twitterInfo: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: FaTwitter,
        text: "Twitter Info Node",
        iconColor: "text-blue-400",
      }}
    />
  ),
  facebookInfo: (props) => (
    <NodeWithLogo
      {...props}
      nodeInfo={{
        icon: FaFacebook,
        text: "Facebook Info Node",
        iconColor: "text-blue-600",
      }}
    />
  ),
};

