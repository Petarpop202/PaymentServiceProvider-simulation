import React from 'react';
import { FaCheckCircle } from 'react-icons/fa';
import './SuccessPayment.css'; 

const SuccessPayment: React.FC = () => {
  return (
    <div className="success-container">
      <FaCheckCircle className="success-icon" />
      <h2 className="success-message">Payment Successful</h2>
    </div>
  );
}

export default SuccessPayment;