// SuccessPayment.tsx
import React from "react";
import { FaTimesCircle } from "react-icons/fa";
import './FailedPayment.css'; 

const FailedPayment: React.FC = () => {
  return (
    <div className="failed-container">
      <FaTimesCircle className="failed-icon" />
      <h2 className="failed-message">Payment Failed</h2>
    </div>
  );
};

export default FailedPayment;
