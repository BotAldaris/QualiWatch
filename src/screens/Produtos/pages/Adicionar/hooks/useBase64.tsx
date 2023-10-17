import { useEffect, useState } from "react";
import { getTextFromImage } from "../../../../../services/Image";

export const useBase64 = () => {
  const [base64, setBase64] = useState("");
  const [nome, setNome] = useState("");

  const fetchNome = async () => {
    if (base64) {
      const resultado = await getTextFromImage(base64, false);
      setNome(resultado);
      setBase64("");
    }
  };
  useEffect(() => {
    fetchNome();
  }, [base64]);

  return { setBase64, nome, setNome };
};
