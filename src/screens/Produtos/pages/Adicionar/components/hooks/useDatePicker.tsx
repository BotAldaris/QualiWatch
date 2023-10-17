import { useEffect, useState } from "react";
import { getTextFromImage } from "../../../../../../services/Image";

export const useDatePicker = () => {
  const [base64, setBase64] = useState("");
  const [items, setItems] = useState<any>();
  const [visivel, setVisivel] = useState(false);
  const [esperar, setEsperar] = useState(false);

  const fetchItems = async () => {
    setVisivel(true);
    setEsperar(true);
    const resultado = await getTextFromImage(base64);
    setItems(resultado);
    setEsperar(false);
  };

  useEffect(() => {
    if (base64) {
      fetchItems();
    }
  }, [base64]);

  return { setBase64, items, visivel, setVisivel, esperar };
};
