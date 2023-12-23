import axios from "axios";
import { CardPaymentModel } from "../model/CardPaymentModel";

const ACQUIRER_URL = "http://localhost:9010/api/aquirer";
const PSP_URL = "http://localhost:9003/api/payment";

export const createOrderBE = (order: { paymentId: string }) => {
  return axios.post(`${PSP_URL}/pay-pal-create`, order);
};

export const executePayment = (token: String) => {
  return axios.post(`${PSP_URL}/pay-pal-execute?token=${token}`);
};

export const requestCardPayment = async (order: { paymentId: string }) => {
  return await axios.post(`${PSP_URL}/card-payment-request`, order);
};

export const submitCardPayment = async (data: CardPaymentModel) => {
  return await axios.post(`${ACQUIRER_URL}/card-payment`, data);
};

export const checkPaymentStatus = async (order: { paymentId: string }) => {
  return await axios.post(`${ACQUIRER_URL}/payment-status`, order);
};

export const createCryptoPayment = (order: { paymentId: string }) => {
  return axios.post(`${PSP_URL}/crypto-payment`, order);
};

export const ipsCodeGenerate = async (order: {paymentId: string}) => {
  return await axios.post(`${ACQUIRER_URL}/qr-code-generator`, order)
};

export const ipsCodeValidate = async (order: {paymentId: string}) => {
  return await axios.post(`${ACQUIRER_URL}/qr-code-validator`, order)
}
