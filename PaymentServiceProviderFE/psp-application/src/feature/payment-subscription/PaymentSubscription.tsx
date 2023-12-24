import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import './PaymentSubscription.css';
import { fetchPaymentMethods, sendOrdersToBackend } from '../../app/agent/agent';
import { FaBitcoin, FaCreditCard, FaPaypal } from 'react-icons/fa';
import { MdQrCodeScanner } from 'react-icons/md';

type PaymentMethod = {
  id: number;
  name: string;
};

type SelectedOptions = {
  creditCard: PaymentMethod | null;
  ips: PaymentMethod | null;
  payPal: PaymentMethod | null;
  crypto: PaymentMethod | null;
};

export default function PaymentSubscription() {
  const [selectedOptions, setSelectedOptions] = useState<SelectedOptions>({
    creditCard: null,
    ips: null,
    payPal: null,
    crypto: null,
  });

  const navigate = useNavigate();

  useEffect(() => {
    const getMethods = async () => {
    await fetchPaymentMethods()
    .then((res) => {
      const paymentMethods: PaymentMethod[] = res.data;
        setSelectedOptions((prevSelected) => ({
          ...prevSelected,
          creditCard: paymentMethods[0],
          ips: paymentMethods[2],
          payPal: paymentMethods[1],
          crypto: paymentMethods[3],
        }));
    })
    .catch((err) => {

    })
  }

    getMethods();
  }, []);

  const handleOptionChange = (option: keyof SelectedOptions, paymentMethod: PaymentMethod | null) => {
    setSelectedOptions((prevSelected) => ({
      ...prevSelected,
      [option]: paymentMethod,
    }));
  };

  const handleBuyNowClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    const selectedOptionsArray = Object.entries(selectedOptions)
      .filter(([_, paymentMethod]) => paymentMethod)
      .map(([_, paymentMethod]) => paymentMethod as PaymentMethod);

    // TODO: Izmeniti bekend
      await sendOrdersToBackend({
        options: selectedOptionsArray,
      })
      .then((res) => {

      })
      .catch((err) => {

      });
  };

  return (
    <body className="body">
      <div className="card mt-50 mb-50">
        <div className="card-title mx-auto">Choose payment method</div>
        <form>
          <div
            className={`row row-1 ${selectedOptions.creditCard ? 'selected' : ''}`}
            onClick={() => handleOptionChange('creditCard', selectedOptions.creditCard)}
          >
            <label htmlFor="creditCard" className="col-2">
              <FaCreditCard size={35} />
              <span className="ml-50">Credit card</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="creditCard"
                name="paymentOption"
                value="creditCard"
                onChange={() => {}}
                checked={selectedOptions.creditCard !== null}
                style={{ display: 'none' }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${selectedOptions.ips ? 'selected' : ''}`}
            onClick={() => handleOptionChange('ips', selectedOptions.ips)}
          >
            <label htmlFor="ips" className="col-2">
              <MdQrCodeScanner size={35} />
              <span className="ml-50">IPS</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="ips"
                name="paymentOption"
                value="ips"
                onChange={() => {}}
                checked={selectedOptions.ips !== null}
                style={{ display: 'none' }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${selectedOptions.payPal ? 'selected' : ''}`}
            onClick={() => handleOptionChange('payPal', selectedOptions.payPal)}
          >
            <label htmlFor="payPal" className="col-2">
              <FaPaypal size={35} />
              <span className="ml-50">Pay-Pal</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="payPal"
                name="paymentOption"
                value="payPal"
                onChange={() => {}}
                checked={selectedOptions.payPal !== null}
                style={{ display: 'none' }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${selectedOptions.crypto ? 'selected' : ''}`}
            onClick={() => handleOptionChange('crypto', selectedOptions.crypto)}
          >
            <label htmlFor="crypto" className="col-2">
              <FaBitcoin size={35} />
              <span className="ml-50">Crypto</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="crypto"
                name="paymentOption"
                value="crypto"
                onChange={() => {}}
                checked={selectedOptions.crypto !== null}
                style={{ display: 'none' }}
              />
            </div>
          </div>

          <button
            className="btn d-flex mx-auto"
            onClick={handleBuyNowClick}
            style={{
              backgroundColor: '#ffffff',
              border: '2px solid #a1aaa5',
              color: '#333333',
              fontSize: '1.1rem',
            }}
          >
            <b>Subscribe now</b>
          </button>
        </form>
      </div>
    </body>
  );
}
