import React, { useState, ChangeEvent, useEffect } from 'react';
import './CreditCardForm.css';
import { useParams } from 'react-router-dom';
import { checkPaymentStatus, submitCardPayment } from '../../app/agent/agent';
import { CardPaymentModel } from '../../app/model/CardPaymentModel';
import { toast } from 'react-toastify';



export default function CreditCardForm() {
  const { id } = useParams<{ id: string }>();
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState<CardPaymentModel>({
    paymentId: id as string,
    pan: '',
    securityCode: '',
    expirationDate: '',
    cardHolderName: '',
  });

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;

    if (name === 'pan' && value.length > 19) return;
    if (name === 'securityCode' && value.length > 3) return;

    const formattedPan = value.replace(/\s/g, '').replace(/(\d{4})/g, '$1 ').trim();

    setFormData({
      ...formData,
      [name]: name === 'pan' ? formattedPan : value,
    });
  };


  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    if (!formData.pan || !formData.securityCode || !formData.expirationDate || !formData.cardHolderName) {
      toast.error('Please fill in all required fields!');
      return;
    }
    const refDate = new Date();
    const expirationDate1 = new Date(formData.expirationDate);
    if(formData.pan.length < 16 || formData.securityCode.length < 3 || expirationDate1.getTime() <= refDate.getTime())
    {
      toast.error('Invalid input. Please check your card details!');
      return;
    }

      formData.pan = formData.pan.replace(/\s/g, '');

      formData.expirationDate = new Date(`${formData.expirationDate}-01T00:00:00`).toISOString();

      await submitCardPayment(formData)
        .then((res) => {
          window.location.href = res.data.statusUrl;
        })
        .catch((err) =>{
          toast.error(err.response.data);
        })
  }


  useEffect(() => {
    const checkStatus = async () => {
      if (loading) {
        const order = {
          paymentId: id as string,
        };
        await checkPaymentStatus(order)
          .then((res) => {
            if (res.data.status === 'SUCCESS') {
              window.location.href = res.data.successUrl;
            } else if(res.data.status === 'ERROR'){
              window.location.href = res.data.errorUrl;
            }else if(res.data.status === 'FAILED'){
              window.location.href = res.data.failedUrl;
            }else {
              setLoading(false);
            }
          })
          .catch((err) => {
            console.log(err);
            setLoading(false); 
          });
      }
    };

    checkStatus();
  }, [loading, formData.paymentId]);

  return (
    <div className="card-form mt-50 mb-50">
      <div className="card-title mx-auto">Enter Card Details</div>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="pan">Card Number:</label>
          <input
            type="text"
            id="pan"
            name="pan"
            value={formData.pan}
            onChange={handleInputChange}
            placeholder="Enter card number"
          />
        </div>

        <div className="form-group-row">
          <div className="form-group">
            <label htmlFor="securityCode">CVC:</label>
            <input
              type="text"
              id="securityCode"
              name="securityCode"
              value={formData.securityCode}
              onChange={handleInputChange}
              placeholder="CVC"
            />
          </div>

          <div className="form-group">
            <label htmlFor="expirationDate">Expiration Date:</label>
            <input
              type="month"
              id="expirationDate"
              name="expirationDate"
              value={formData.expirationDate}
              onChange={handleInputChange}
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="cardHolderName">Cardholder Name:</label>
          <input
            type="text"
            id="cardHolderName"
            name="cardHolderName"
            value={formData.cardHolderName}
            onChange={handleInputChange}
            placeholder="Enter cardholder name"
          />
        </div>

        <button type="submit" className="btn d-flex mx-auto" disabled={!formData.pan || !formData.securityCode || !formData.expirationDate || !formData.cardHolderName}><b>Submit</b></button>
      </form>
    </div>
  );
}

