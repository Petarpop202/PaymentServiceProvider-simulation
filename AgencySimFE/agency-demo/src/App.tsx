import axios from "axios";
import { useEffect, useState } from "react";
import "./App.css";
import { configureAxiosRequestInterceptors } from "./AxiosConfing";

function App() {
  const [amount, setAmount] = useState(120);
  const [paid, setPaid] = useState(false);
  const [redirectUrl, setRedirectUrl] = useState("");
  configureAxiosRequestInterceptors();

  async function pay() {
    const timeStamp = new Date();
    timeStamp.setHours(timeStamp.getHours() + 1);
    timeStamp.toISOString();

    const paymentData = {
      amount,
      merchantOrderId: 1,
      timeStamp,
    };

    // Retrieve the JWT token from your authentication system
    const jwtToken =
      "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJQU1AiLCJzdWIiOiIxIiwiYXVkIjoid2ViIiwiaWF0IjoxNzA2NzE2ODQ3fQ.sijP48YoyVpcZ5kHmMCPmoPeS56g7jc5kuP_6EPKyQpg1Fv43wpL6wZbo3KSreNVLDxrCdQFs744sg3ZhCCozw";

    axios
      .post("https://localhost:9003/api/payment/payment-request", paymentData, {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      })
      .then((response) => {
        setRedirectUrl(response.data.paymentUrl);
        setPaid(true);
      })
      .catch((error) => {
        setRedirectUrl("https://localhost:4000/payment-subscription");
        setPaid(true);
      });
  }

  useEffect(() => {
    if (paid && redirectUrl) {
      window.location.href = redirectUrl;
    }
  }, [paid, redirectUrl]);

  return (
    <div className="App">
      <h1>Agency app</h1>
      <form>
        <p>Markirana roba</p>
        <p>Cena: {amount} dinara</p>
        <button type="button" onClick={() => pay()}>
          Plati
        </button>
      </form>
    </div>
  );
}

export default App;
