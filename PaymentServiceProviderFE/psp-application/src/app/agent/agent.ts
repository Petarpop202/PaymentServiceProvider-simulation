import axios from "axios";
import { CardPaymentModel } from "../model/CardPaymentModel";

const ACQUIRER_URL = "http://localhost:9010/api/aquirer";
const PSP_URL = "http://localhost:9003/api/payment";
const PSP_PAYMENT_URL = "http://localhost:9003/api/subscription";

type PaymentMethod = {
  id: number;
  name: string;
};

const axiosInstance = axios.create();

axiosInstance.interceptors.request.use(
  (config) => {
    const jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJQU1AiLCJzdWIiOiIxIiwiYXVkIjoid2ViIiwiaWF0IjoxNzAzNDQ1MDI1fQ.uTDA0_9Z0h9Ark_h_7bZb1SSunvqEZGdexjALwum8TBpAAgk08xr2H50GBg8YLAWxJxUeA8EYj2xZJcAEmmeVQ";

    config.headers.Authorization = `Bearer ${jwtToken}`;

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const createOrderBE = (order: { paymentId: string }) => {
  return axiosInstance.post(`${PSP_URL}/pay-pal-create`, order);
};

export const executePayment = (token: String) => {
  return axiosInstance.post(`${PSP_URL}/pay-pal-execute?token=${token}`);
};

export const requestCardPayment = async (order: { paymentId: string }) => {
  return await axiosInstance.post(`${PSP_URL}/card-payment-request`, order);
};

export const submitCardPayment = async (data: CardPaymentModel) => {
  return await axios.post(`${ACQUIRER_URL}/card-payment`, data);
};

export const checkPaymentStatus = async (order: { paymentId: string }) => {
  return await axios.post(`${ACQUIRER_URL}/payment-status`, order);
};

export const createCryptoPayment = (order: { paymentId: string }) => {
  return axiosInstance.post(`${PSP_URL}/crypto-payment`, order);
};

export const ipsCodeGenerate = async (order: {paymentId: string}) => {
  return await axios.post(`${ACQUIRER_URL}/qr-code-generator`, order)
};

export const ipsCodeValidate = async (order: {paymentId: string}) => {
  return await axios.post(`${ACQUIRER_URL}/qr-code-validator`, order)
}

export const sendOrdersToBackend = async (order: {methodsForSubscription: PaymentMethod[]}) => {
  return await axios.post(`${PSP_PAYMENT_URL}/create-subscription`, order);
}

export const fetchPaymentMethods = async () => {
  return axios.get(`${PSP_PAYMENT_URL}/get-all-methods`);
}

export const getActivePaymentMethods = async () => {
  return axiosInstance.get(`${PSP_PAYMENT_URL}/get-methods-for-subscription`);
}