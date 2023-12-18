import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ipsCodeGenerate } from "../../app/agent/agent";
import { Buffer } from 'buffer';

export default function IpsCodePayment() {
    const [qrCodeImage, setQrCodeImage] = useState<string | null>(null);
    const { id } = useParams();
  
    useEffect(() => {
        const generate = async () => {
            const order = {
                paymentId: id as string,
              };
            await ipsCodeGenerate(order)
            .then((response) => {
                setQrCodeImage("data:image/png;base64,"+response.data);
            })
        }
        generate();
    }, []);
  
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        {qrCodeImage && <img src={qrCodeImage} alt="QR Code" />}
      </div>
    );
  }