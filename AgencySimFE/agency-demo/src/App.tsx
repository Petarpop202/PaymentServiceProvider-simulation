import React, { useState, useEffect } from 'react';
import './App.css';


function App() {
  const [amount, setAmount] = useState(12000);
  const [paid, setPaid] = useState(false);
  const [redirectUrl, setRedirectUrl] = useState('');

  useEffect(() => {
    const handlePayment = async () => {
      const timeStamp = new Date().toISOString();

      const paymentData = {
        amount,
        merchantOrderId: 1,
        timeStamp,
      };

      try {
        const response = await fetch('http://localhost:9003/api/payment/card-payment', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(paymentData),
        });

        if (response.ok) {
          console.log('Uspešno plaćeno!');
          const responseData = await response.json();

          setRedirectUrl(responseData.paymentUrl);
          setPaid(true);
        } else {
          console.error('Greška prilikom plaćanja.');
        }
      } catch (error) {
        console.error('Greška prilikom slanja podataka na backend.', error);
      }
    };

    handlePayment();
  }, [amount]);

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
          <button type="button" onClick={() => setAmount(12000)}>
            Plati
          </button>
        </form>
    </div>
  );
}

export default App;