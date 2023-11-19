import { PayPalButtons, PayPalScriptProvider } from "@paypal/react-paypal-js";
import { ChangeEvent, useState } from "react";
import { FaBitcoin, FaCreditCard } from "react-icons/fa";
import { MdQrCodeScanner } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";
import { createOrderBE, executePayment } from "../../app/agent/agent";
import "./Payments.css";

export default function Payments() {
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const { id } = useParams();
  const navigate = useNavigate();

  const handleOptionChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSelectedOption(event.target.value);
  };

  const handleBuyNowClick = () => {
    // Dodajte redirekciju na osnovu odabrane opcije
    switch (selectedOption) {
      case "creditCard":
        navigate("/payment/credit-card/" + id); // Prilagodite ruta
        break;
      case "ips":
        navigate("/choose-payment/" + id); // Prilagodite ruta
        break;
      case "crypto":
        navigate("/choose-payment/" + id); // Prilagodite ruta
        break;
      default:
        navigate("/choose-payment/" + id); // Prilagodite ruta
        break;
    }
  };

  const onCreate = (data: any, actions: any): Promise<string> => {
    const order = {
      paymentId: id as string,
    };
    return createOrderBE(order)
      .then((res) => {
        return res.data;
      })
      .catch((err) => {
        console.log(err);
        return "";
      });
  };

  const onApprove = async (data: any, actions: any) => {
    console.log(data);
    await executePayment(data["orderID"])
      .then((res) => {
        console.log(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <body className="body">
      <div className="card mt-50 mb-50">
        <div className="card-title mx-auto">Choose payment method</div>
        <div className="nav">
          <ul className="ul">
            <li className="active">
              <a href="#">Payment</a>
            </li>
          </ul>
        </div>
        <form>
          <div
            className={`row row-1 ${
              selectedOption === "creditCard" ? "selected" : ""
            }`}
            onClick={() => setSelectedOption("creditCard")}
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
                onChange={handleOptionChange}
                checked={selectedOption === "creditCard"}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${
              selectedOption === "ips" ? "selected" : ""
            }`}
            onClick={() => setSelectedOption("ips")}
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
                onChange={handleOptionChange}
                checked={selectedOption === "ips"}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <div
            className={`row row-1 ${
              selectedOption === "payPal" ? "selected" : ""
            }`}
            onClick={() => setSelectedOption("payPal")}
          >
            <PayPalScriptProvider
              options={{
                clientId:
                  "ATcRmO0vakND1PSs7S9UHyplUXzY0DJySmkzNhKgIijUx_HGtzYogtDgduU7YgfNDlz_VffqRrCMwDe8",
              }}
            >
              <PayPalButtons
                fundingSource="paypal"
                createOrder={onCreate}
                onApprove={onApprove}
              />
            </PayPalScriptProvider>
          </div>

          <div
            className={`row row-1 ${
              selectedOption === "crypto" ? "selected" : ""
            }`}
            onClick={() => setSelectedOption("crypto")}
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
                onChange={handleOptionChange}
                checked={selectedOption === "crypto"}
                style={{ display: "none" }}
              />
            </div>
          </div>

          <button className="btn d-flex mx-auto" onClick={handleBuyNowClick}>
            <b>Buy now</b>
          </button>
        </form>
      </div>
    </body>
  );
}
