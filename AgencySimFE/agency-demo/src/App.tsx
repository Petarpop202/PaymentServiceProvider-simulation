import { useEffect, useState } from "react";
import "./App.css";
import { useNavigate, useParams } from "react-router-dom";

function App() {
  const [amount, setAmount] = useState(120);
  const [paid, setPaid] = useState(false);
  const [redirectUrl, setRedirectUrl] = useState("");

  async function pay() {
    try {
      const timeStamp = new Date();
      timeStamp.setHours(timeStamp.getHours() + 1);
      timeStamp.toISOString();

      const paymentData = {
        amount,
        merchantOrderId: 1,
        timeStamp,
      };

      // Retrieve the JWT token from your authentication system
      const jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJQU1AiLCJzdWIiOiIxIiwiYXVkIjoid2ViIiwiaWF0IjoxNzAzNDQ1MDI1fQ.uTDA0_9Z0h9Ark_h_7bZb1SSunvqEZGdexjALwum8TBpAAgk08xr2H50GBg8YLAWxJxUeA8EYj2xZJcAEmmeVQ";

      const response = await fetch(
        "http://localhost:9003/api/payment/payment-request",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwtToken}`,
          },
          body: JSON.stringify(paymentData),
        }
      );

      if (response.ok) {
        const responseData = await response.json();

        setRedirectUrl(responseData.paymentUrl);
        setPaid(true);
      } else {
        setRedirectUrl("http://localhost:4000/payment-subscription");
        setPaid(true);
      }
    } catch (error) {
      setRedirectUrl("http://localhost:4000/payment-subscription");
      setPaid(true);
    }
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
