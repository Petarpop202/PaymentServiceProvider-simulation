export interface PaymentStatusResponse {
    paymentId: string;
    successUrl: string
    errorUrl: string
    failedUrl: string
    status: string
}
