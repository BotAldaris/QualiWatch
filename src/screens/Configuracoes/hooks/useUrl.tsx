import { useEffect, useState } from "react";
import { getUrl } from "../../../services/Url";

export const useUrl = () => {
  const [url, setUrl] = useState("");

  const fetchUrl = async () => {
    const resposta = await getUrl();
    if (resposta) {
      setUrl(resposta);
    }
  };
  const handleUrl = (url: string) => {
    setUrl(url);
  };
  useEffect(() => {
    fetchUrl();
  }, []);

  return { url, handleUrl };
};
