import { useEffect, useState } from "react";
import { estado, getEstado, saveEstado } from "../../../services/Estado";

export const useOnOff = () => {
  const [onOff, setOnOff] = useState("online" as estado);

  const fetchOnOff = async () => {
    const resposta = await getEstado();
    if (resposta) {
      setOnOff(resposta);
    }
  };
  const handleOnOff = async (estado: estado) => {
    setOnOff(estado);
    await saveEstado(estado);
  };
  useEffect(() => {
    fetchOnOff();
  }, []);

  return { onOff, handleOnOff };
};
