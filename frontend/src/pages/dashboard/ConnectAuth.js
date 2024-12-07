import { useEffect, useCallback } from "react";

const ConnectAuth = () => {
  const CONFIG = {
    clientId: process.env.REACT_APP_GOOGLE_CLIENT_ID,
    redirectUri: process.env.REACT_APP_GOOGLE_REDIRECT_URI,
    scopes: [
      "https://www.googleapis.com/auth/gmail.readonly",
      "https://www.googleapis.com/auth/youtube.upload",
      "https://mail.google.com",
      "https://www.googleapis.com/auth/drive",
      "https://www.googleapis.com/auth/userinfo.email",
      "https://www.googleapis.com/auth/userinfo.profile",
      // "https://www.googleapis.com/auth/youtube.readonly"
    ],
    accessType: "offline",
    includeGrantedScopes: true,
    response_type: "code",
  };

  const handleLogin = useCallback(() => {
    try {
      const params = new URLSearchParams({
        client_id: CONFIG.clientId,
        redirect_uri: CONFIG.redirectUri,
        response_type: CONFIG.response_type,
        scope: CONFIG.scopes.join(" "),
        include_granted_scopes: CONFIG.includeGrantedScopes,
        access_type: CONFIG.accessType,
      });

      const oauthUrl = `https://accounts.google.com/o/oauth2/auth?${params}`;

      // Calculate centered popup position
      const width = 500;
      const height = 600;
      const left = window.screen.width / 2 - width / 2;
      const top = window.screen.height / 2 - height / 2;

      // Open centered popup
      const popup = window.open(
        oauthUrl,
        "GoogleLogin",
        `width=${width},height=${height},top=${top},left=${left},scrollbars=yes`
      );

      if (!popup) {
        throw new Error(
          "Failed to open popup window. Please check if popups are blocked."
        );
      }
    } catch (error) {
      console.error("Error initiating OAuth flow:", error);
    }
  }, []);

  return (
    <div className="space-y-4">
      <button
        onClick={handleLogin}
        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
      >
        Sign in with Google
      </button>
    </div>
  );
};

export default ConnectAuth;
