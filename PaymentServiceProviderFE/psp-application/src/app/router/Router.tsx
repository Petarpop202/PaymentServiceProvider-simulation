import React from 'react';
import { Routes, Route } from 'react-router-dom';
import App from '../layout/App';
import Payments from '../../feature/payment/Payments';
import CreditCardForm from '../../feature/credit-card-form/CreditCardForm';

const Router: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/choose-payment/:id" element={<Payments/>} />
      <Route path="/payment/credit-card/:id" element={<CreditCardForm/>} />
    </Routes>
  );
};

export default Router;