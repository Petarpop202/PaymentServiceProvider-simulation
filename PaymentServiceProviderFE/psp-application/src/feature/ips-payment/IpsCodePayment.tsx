import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { checkPaymentStatus, ipsCodeGenerate, ipsCodeValidate } from "../../app/agent/agent";
import { Buffer } from 'buffer';
import { toast } from "react-toastify";

export default function IpsCodePayment() {
    const [qrCodeImage, setQrCodeImage] = useState<string | null>(null);
    const { id } = useParams();
  
    useEffect(() => {
        const checkStatus = async () =>{
          const order = {
            paymentId: id as string,
          };
          await checkPaymentStatus(order)
            .then((res) => {
              if (res.data.status === "SUCCESS") {
                window.location.href = res.data.successUrl;
              } else if (res.data.status === "ERROR") {
                window.location.href = res.data.errorUrl;
              } else if (res.data.status === "FAILED") {
                window.location.href = res.data.failedUrl;
              }else generate(); 
            })
            .catch((err) => {
              console.log(err);
            });
        }
        const generate = async () => {
            const order = {
                paymentId: id as string,
              };
            await ipsCodeGenerate(order)
            .then((response) => {
                setQrCodeImage("data:image/png;base64,"+response.data);
            })
        }
        checkStatus()
        
    }, []);

    const validate = async () => {
      const order = {
        paymentId : id as string
      };
      await ipsCodeValidate(order)
      .then((response) => {
        toast.success("Uspeh");
        window.location.href = response.data.statusUrl;
      })
    }
  
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        {qrCodeImage && <img src={qrCodeImage} alt="QR Code" />}
        <button style={{marginTop: 10}} onClick={validate}>Validate ips and pay</button>
      </div>
    );
  }