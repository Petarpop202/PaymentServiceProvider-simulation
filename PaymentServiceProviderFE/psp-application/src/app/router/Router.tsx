import React from 'react';
import { Routes, Route } from 'react-router-dom';
import App from '../layout/App';
import Payments from '../../feature/payment/Payments';
import CreditCardForm from '../../feature/credit-card-form/CreditCardForm';
import SuccessPayment from '../../feature/bank-response-urls/SuccessPayment';
import ErrorPayment from '../../feature/bank-response-urls/ErrorPayment';
import FailedPayment from '../../feature/bank-response-urls/FailedPayment';

const Router: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/choose-payment/:id" element={<Payments/>} />
      <Route path="/payment/credit-card/:id" element={<CreditCardForm/>} />
      <Route path="/success-payment" element={<SuccessPayment/>} />
      <Route path="/error-payment" element={<ErrorPayment/>} />
      <Route path="/failed-payment" element={<FailedPayment/>} />
    </Routes>
  );
};

export default Router;