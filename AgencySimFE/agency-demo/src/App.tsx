import { useEffect, useState } from "react";
import "./App.css";

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
      const response = await fetch(
        "http://localhost:9003/api/payment/card-payment",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(paymentData),
        }
      );

      if (response.ok) {
        console.log("Uspešno plaćeno!");
        const responseData = await response.json();

        setRedirectUrl(responseData.paymentUrl);
        setPaid(true);
      } else {
        console.error("Greška prilikom plaćanja.");
      }
    } catch (error) {
      console.error("Greška prilikom slanja podataka na backend.", error);
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
