import { useEffect, useCallback } from "react";

const ConnectAuth = () => {
  const CONFIG = {
    clientId:
      "775540466291-7otscll1b0eukqnm9vusms0d2dhqr4a4.apps.googleusercontent.com",
    redirectUri: "http://localhost:8080/api/google/callback",
    scopes: [
      "https://www.googleapis.com/auth/gmail.readonly",
      "https://www.googleapis.com/auth/youtube.upload",
      "https://mail.google.com",
      "https://www.googleapis.com/auth/drive",
      "https://www.googleapis.com/auth/userinfo.email",
      "https://www.googleapis.com/auth/userinfo.profile"
      // "https://www.googleapis.com/auth/youtube.readonly"
    ],
    accessType: "offline",
    includeGrantedScopes: true,
    response_type: "code",
  };

  const handleMessage = useCallback((event) => {
    if (
      event.origin != "http://localhost:8080" &&
      event.origin != "http://localhost:3000" &&
      event.origin != "https://accounts.google.com"
    ) {
      console.warn("Received message from unauthorized origin:", event.origin);
      return;
    }

    try {
      if (event.data.message) {
        console.log("Received message:", event.data.message);
      }
    } catch (error) {
      console.error("Error processing window message:", error);
    } finally {
      if (window.loginWindow) {
        window.loginWindow.close(); // close using frontend
      }
    }
  }, []);

  useEffect(() => {
    window.addEventListener("message", handleMessage);
    return () => window.removeEventListener("message", handleMessage);
  }, [handleMessage]);

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

      window.loginWindow = popup;
    } catch (error) {
      console.error("Error initiating OAuth flow:", error);
    }
  }, []);

  const setCookies = () => {
    fetch("http://localhost:8080/api/auth/coo", {
      method: "GET",
      credentials: "include", // Important for including cookies in the request
  })
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error(error));
  };

  const getCookies = () => {
    fetch("http://localhost:8080/api/google/callback?code=your-code", {
      method: "GET",
      credentials: "include", // Send credentials (if required)
      headers: {
        "Authorization": "Bearer your-token",
      },
    })
      .then(response => response.json())
      .then(data => console.log(data))
      .catch(error => console.error("CORS Error:", error));
    
  };

  return (
    <div className="space-y-4">
      <button
        onClick={handleLogin}
        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
      >
        Sign in with Google
      </button>
    <button
      onClick={setCookies}
     >
      setCookies
    </button>

    <button
      onClick={getCookies}
     >
      getCookies
    </button>
    </div>

  );
};

export default ConnectAuth;