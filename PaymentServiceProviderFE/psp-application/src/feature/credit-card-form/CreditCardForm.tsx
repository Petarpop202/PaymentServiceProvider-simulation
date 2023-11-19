import React, { useState, ChangeEvent } from 'react';
import './CreditCardForm.css';
import { useParams } from 'react-router-dom';

interface CardFormData {
  paymentId: string;
  pan: string;
  securityCode: string;
  expirationDate: string;
  cardHolderName: string;
}

export default function CreditCardForm() {
  const [formData, setFormData] = useState<CardFormData>({
    paymentId: '',
    pan: '',
    securityCode: '',
    expirationDate: '',
    cardHolderName: '',
  });

  const { id } = useParams();

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

    // Validacija
    if (!formData.pan || !formData.securityCode || !formData.expirationDate || !formData.cardHolderName) {
      console.error('Please fill in all required fields.');
      return;
    }
    const refDate = new Date();
    const expirationDate1 = new Date(formData.expirationDate);
    if(formData.pan.length < 19 || formData.securityCode.length < 3 || expirationDate1.getTime() <= refDate.getTime())
      return;

    try {
      const formattedPan = formData.pan.replace(/\s/g, '');

      const expirationDate = new Date(`${formData.expirationDate}-01T00:00:00`).toISOString();

      const response = await fetch('http://localhost:9000/api/aquirer/card-payment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          paymentId: id,
          pan: formattedPan,
          securityCode: formData.securityCode,
          expirationDate,
          cardHolderName: formData.cardHolderName,
        }),
      });

      if (response.ok) {
        const responseData = await response.json();
        console.log(responseData.statusUrl);
      } else {
        console.error('Error processing payment.');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

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
