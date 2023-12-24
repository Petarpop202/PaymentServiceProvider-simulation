import { PayPalButtons, PayPalScriptProvider } from "@paypal/react-paypal-js";
import React, { ChangeEvent, useEffect, useState } from "react";
import { FaBitcoin, FaCreditCard, FaPaypal } from "react-icons/fa";
import { MdQrCodeScanner } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import {
  checkPaymentStatus,
  createCryptoPayment,
  createOrderBE,
  executePayment,
  getActivePaymentMethods,
  requestCardPayment,
} from "../../app/agent/agent";
import "./Payments.css";

type PaymentMethod = {
  id: number;
  name: string;
};


export default function Payments() {
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const { id } = useParams();
  const [activePaymentMethods, setActivePaymentMethods] = useState<PaymentMethod[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const handleOptionChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSelectedOption(event.target.value);
  };

  const handleBuyNowClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    switch (selectedOption) {
      case "CREDIT CARD":
        const order = {
          paymentId: id as string,
        };
        await requestCardPayment(order)
          .then((res) => {
            window.location.href = res.data.paymentUrl;
          })
          .catch((err) => {
            console.log(err);
          });
        return;
      case "QR CODE":
        const order1 = {
          paymentId: id as string,
        };
        await requestCardPayment(order1)
        .then((res) => {
          navigate("/payment/ips-payment/"+id);
        })
        .catch((err) =>{
          console.log(err);
        })
        break;
      case "CRYPTO":
        executeCryptoPayment();
        return;
      case "PAYPAL":
        break;
      default:
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
        navigate("/success-payment");
      })
      .catch((err) => {
        navigate("/error-payment");
      });
  };

  const executeCryptoPayment = () => {
    const paymentData = {
      paymentId: id as string,
    };
    createCryptoPayment(paymentData)
      .then((res) => {
        window.location.href = res.data.payment_url;
      })
      .catch((err) => {
        toast.error(err.message);
      });
  };

  useEffect(() => {
    const getActivePaymentMethodsFun = async () => {
        await getActivePaymentMethods()
        .then((res) => {
          setActivePaymentMethods(res.data)
        })
        .catch((err) => {

        }); 
    };

    const checkStatus = async () => {
      console.log("Check status");
      if (loading) {
        const order = {
          paymentId: id as string,
        };
        await checkPaymentStatus(order)
          .then((res) => {
            if (res.data.status === "SUCCESS") {
              window.location.href = res.data.successUrl;
            } else if (res.data.status === "ERROR") {
              window.location.href = res.data.errorUrl;
            } else if (res.data.status === "FAILED") {
              window.location.href = res.data.failedUrl;
            } else {
              setLoading(false);
            }
          })
          .catch((err) => {
            console.log(err);
            setLoading(false);
          });
      }
    };

    checkStatus();
    getActivePaymentMethodsFun();
  }, [loading, id]);

  return (
    <body className="body">
      <div className="card mt-50 mb-50">
        <div className="card-title mx-auto">Choose payment method</div>
        <div className="nav"></div>
        <form>
          {activePaymentMethods.map((paymentMethod) => (
            <div
              key={paymentMethod.name}
              className={`row row-1 ${
                selectedOption === paymentMethod.name ? "selected" : ""
              }`}
              onClick={() => setSelectedOption(paymentMethod.name)}
            >
              <label htmlFor={paymentMethod.name} className="col-2">
                {paymentMethod.name === "CREDIT CARD" && <FaCreditCard size={35} />}
                {paymentMethod.name === "QR CODE" && <MdQrCodeScanner size={35} />}
                {paymentMethod.name === "PAY PAL" && <FaPaypal size={35} />}
                {paymentMethod.name === "CRYPTO" && <FaBitcoin size={35} />}
                <span className="ml-50">{paymentMethod.name}</span>
              </label>
              <div className="col-7">
                <input
                  type="radio"
                  id={paymentMethod.id.toString()}
                  name="paymentOption"
                  value={paymentMethod.name}
                  onChange={handleOptionChange}
                  checked={selectedOption === paymentMethod.name}
                  style={{ display: "none" }}
                />
              </div>
            </div>
          ))}
          {selectedOption === "PAY PAL" && (
            <PayPalScriptProvider
              options={{
                clientId:
                  "ATcRmO0vakND1PSs7S9UHyplUXzY0DJySmkzNhKgIijUx_HGtzYogtDgduU7YgfNDlz_VffqRrCMwDe8",
              }}
            >
              <div className="col-7">
                <PayPalButtons
                  fundingSource="paypal"
                  createOrder={onCreate}
                  onApprove={onApprove}
                  style={{ color: "white", shape: "rect", tagline: false }}
                />
              </div>
            </PayPalScriptProvider>
          )}

          <button
            className="btn d-flex mx-auto"
            onClick={handleBuyNowClick}
            style={{
              display: selectedOption === "payPal" ? "none" : "block",
              backgroundColor: "#ffffff",
              border: "2px solid #a1aaa5",
              color: "#333333",
              fontSize: "1.1rem",
            }}
          >
            <b>Buy now</b>
          </button>
        </form>
      </div>
    </body>
  );
}
