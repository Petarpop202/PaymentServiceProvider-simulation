import React from 'react';
import { Routes, Route } from 'react-router-dom';
import App from '../layout/App';
import Payments from '../../feature/payment/Payments';

const Router: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/choose-payment" element={<Payments/>} />
    </Routes>
  );
};

export default Router;