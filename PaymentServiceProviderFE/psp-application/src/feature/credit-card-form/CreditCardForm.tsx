import React, { useState, ChangeEvent } from 'react';
import './CreditCardForm.css';
import { useParams } from 'react-router-dom';

interface CardFormData {
  pan: string;
  cvc: string;
  expirationDate: string;
  cardHolderName: string;
}

export default function CreditCardForm() {
  const [formData, setFormData] = useState<CardFormData>({
    pan: '',
    cvc: '',
    expirationDate: '',
    cardHolderName: '',
  });

  const { id } = useParams();

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;

    if (name === 'pan' && value.length > 16) return;
    if (name === 'cvc' && value.length > 3) return;

    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();


    console.log('Submitted data:', formData);
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
            <label htmlFor="cvc">CVC:</label>
            <input
              type="text"
              id="cvc"
              name="cvc"
              value={formData.cvc}
              onChange={handleInputChange}
              placeholder="CVC"
            />
          </div>

          <div className="form-group">
            <label htmlFor="expirationDate">Expiration Date:</label>
            <input
              type="text"
              id="expirationDate"
              name="expirationDate"
              value={formData.expirationDate}
              onChange={handleInputChange}
              placeholder="MM/YY"
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

        <button type="submit" className="btn d-flex mx-auto"><b>Submit</b></button>
      </form>
    </div>
  );
}
