import { PayPalScriptProvider } from "@paypal/react-paypal-js";
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import Router from "./app/router/Router";
import "./index.css";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

root.render(
  <React.StrictMode>
    <BrowserRouter>
      <PayPalScriptProvider
        options={{
          clientId:
            "ATcRmO0vakND1PSs7S9UHyplUXzY0DJySmkzNhKgIijUx_HGtzYogtDgduU7YgfNDlz_VffqRrCMwDe8",
        }}
      >
        <Router />
      </PayPalScriptProvider>
    </BrowserRouter>
  </React.StrictMode>
);
