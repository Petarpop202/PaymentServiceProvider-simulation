import React, { useState, ChangeEvent } from 'react';
import './Payments.css';
import { FaCreditCard } from "react-icons/fa";
import { FaPaypal } from "react-icons/fa";

export default function Payments() {
  const [selectedOption, setSelectedOption] = useState<string | null>(null);

  const handleOptionChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSelectedOption(event.target.value);
  };

  return (
    <body className='body'>
      <div className="card mt-50 mb-50">
        <div className="card-title mx-auto">Choose payment method</div>
        <div className="nav">
          <ul className="ul">
            <li className="active">
              <a href="#">Payment</a>
            </li>
          </ul>
        </div>
        <form>
          <span id="card-header">Saved cards:</span>
          <div className="row row-1">
            <div className="col-2">
              <FaCreditCard size={35}/>
              <a className="ml-50">Credit card</a>
            </div>
            <div className="col-7">
              <input type="radio" onChange={handleOptionChange} />
            </div>
          </div>
          <div className="row row-1">
            <div className="col-2">
              <FaPaypal size={35}/>
              <a className="ml-50">IPS</a>
            </div>
            <div className="col-7">
              <input type="radio" value={"payPal"} onChange={handleOptionChange} />
            </div>
          </div>
          <div className="row row-1">
            <div className="col-2">
              <FaPaypal size={35}/>
              <a className="ml-50">Pay-pal</a>
            </div>
            <div className="col-7">
              <input type="radio" value={"payPal"} onChange={handleOptionChange} />
            </div>
          </div>
          <div className="row row-1">
            <div className="col-2">
              <FaPaypal size={35}/>
              <a className="ml-50">Crypto</a>
            </div>
            <div className="col-7">
              <input type="radio" value={"payPal"} onChange={handleOptionChange} />
            </div>
          </div>
          <button className="btn d-flex mx-auto"><b>Buy now</b></button>
          
        </form>
      </div>
    </body>
  );
}
