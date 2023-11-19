import axios from "axios";

export const createOrderBE = (order: { paymentId: string }) => {
  return axios.post("http://localhost:9003/api/payment/pay-pal-create", order);
};

export const executePayment = (token: String) => {
  return axios.post(
    `http://localhost:9003/api/payment/pay-pal-execute?token=${token}`
  );
};
