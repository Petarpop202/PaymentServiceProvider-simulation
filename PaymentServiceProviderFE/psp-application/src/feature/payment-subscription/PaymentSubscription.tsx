import React, { useEffect, useState } from "react";
import { FaBitcoin, FaCreditCard, FaPaypal } from "react-icons/fa";
import { MdQrCodeScanner } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import {
  fetchPaymentMethods,
  sendOrdersToBackend,
} from "../../app/agent/agent";
import "./PaymentSubscription.css";

type PaymentMethod = {
  id: number;
  name: string;
};

type SelectedOptions = {
  CREDIT_CARD: PaymentMethod | null;
  QR_CODE: PaymentMethod | null;
  PAY_PAL: PaymentMethod | null;
  CRYPTO: PaymentMethod | null;
};

export default function PaymentSubscription() {
  const [selectedOptions, setSelectedOptions] = useState<SelectedOptions>({
    CREDIT_CARD: null,
    QR_CODE: null,
    PAY_PAL: null,
    CRYPTO: null,
  });

  const navigate = useNavigate();

  useEffect(() => {
    const getMethods = async () => {
      await fetchPaymentMethods()
        .then((res) => {
          const paymentMethods: PaymentMethod[] = res.data;

          setSelectedOptions({
            CREDIT_CARD: null,
            QR_CODE: null,
            PAY_PAL: null,
            CRYPTO: null,
          });

          setSelectedOptions((prevSelected) => ({
            ...prevSelected,
            CREDIT_CARD: paymentMethods[0],
            QR_CODE: paymentMethods[1],
            PAY_PAL: paymentMethods[2],
            CRYPTO: paymentMethods[3],
          }));
        })
        .catch((err) => {});
    };

    getMethods();
  }, []);

  const handleOptionChange = (
    option: keyof SelectedOptions,
    paymentMethod: PaymentMethod | null
  ) => {
    setSelectedOptions((prevSelected) => ({
      ...prevSelected,
      [option]: prevSelected[option] === paymentMethod ? null : paymentMethod,
    }));
  };

  const handleBuyNowClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    const selectedOptionsArray = Object.entries(selectedOptions)
      .filter(([_, paymentMethod]) => paymentMethod)
      .map(([_, paymentMethod]) => paymentMethod as PaymentMethod);

    // TODO: Izmeniti bekend
    await sendOrdersToBackend({
      methodsForSubscription: selectedOptionsArray,
    })
      .then((res) => {
        window.location.href = "https://localhost:3000";
      })
      .catch((err) => {});
  };

  return (
    <body className="body">
      <div className="card mt-50 mb-50">
        <div className="card-title mx-auto">Choose payment method</div>
        <form>
          <div
            className={`row row-1 ${
              selectedOptions.CREDIT_CARD !== null ? "selected" : ""
            }`}
            onClick={() =>
              handleOptionChange("CREDIT_CARD", selectedOptions.CREDIT_CARD)
            }
          >
            <label htmlFor="creditCard" className="col-2">
              <FaCreditCard size={35} />
              <span className="ml-50">Credit card</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="CREDIT_CARD"
                name="paymentOption"
                value="CREDIT_CARD"
                onChange={() => {}}
                checked={selectedOptions.CREDIT_CARD !== null}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${
              selectedOptions.QR_CODE !== null ? "selected" : ""
            }`}
            onClick={() =>
              handleOptionChange("QR_CODE", selectedOptions.QR_CODE)
            }
          >
            <label htmlFor="QR_CODE" className="col-2">
              <MdQrCodeScanner size={35} />
              <span className="ml-50">IPS</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="QR_CODE"
                name="paymentOption"
                value="QR_CODE"
                onChange={() => {}}
                checked={selectedOptions.QR_CODE !== null}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${
              selectedOptions.PAY_PAL !== null ? "selected" : ""
            }`}
            onClick={() =>
              handleOptionChange("PAY_PAL", selectedOptions.PAY_PAL)
            }
          >
            <label htmlFor="PAY_PAL" className="col-2">
              <FaPaypal size={35} />
              <span className="ml-50">Pay-Pal</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="PAY_PAL"
                name="paymentOption"
                value="PAY_PAL"
                onChange={() => {}}
                checked={selectedOptions.PAY_PAL !== null}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${
              selectedOptions.CRYPTO !== null ? "selected" : ""
            }`}
            onClick={() => handleOptionChange("CRYPTO", selectedOptions.CRYPTO)}
          >
            <label htmlFor="CRYPTO" className="col-2">
              <FaBitcoin size={35} />
              <span className="ml-50">Crypto</span>
            </label>
            <div className="col-7">
              <input
                type="radio"
                id="CRYPTO"
                name="paymentOption"
                value="CRYPTO"
                onChange={() => {}}
                checked={selectedOptions.CRYPTO !== null}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <button
            className="btn d-flex mx-auto"
            onClick={handleBuyNowClick}
            style={{
              backgroundColor: "#ffffff",
              border: "2px solid #a1aaa5",
              color: "#333333",
              fontSize: "1.1rem",
            }}
          >
            <b>Subscribe now</b>
          </button>
        </form>
      </div>
    </body>
  );
}
