// SuccessPayment.tsx
import React from "react";
import { FaExclamationCircle } from "react-icons/fa";
import './ErrorPayment.css'; 

const ErrorPayment: React.FC = () => {
  return (
    <div className="error-container">
      <FaExclamationCircle className="error-icon" />
      <h2 className="error-message">Error Payment</h2>
    </div>
  );
};

export default ErrorPayment;
