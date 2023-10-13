import { ReactNode, createContext, useEffect, useState } from "react";
import { getTheme, saveTheme } from "../services/Tema";
import {
  CombinedDarkTheme,
  CombinedDefaultTheme,
} from "../estilosGlobais";
interface IProps {
  children: ReactNode;
}
export interface ITemaContext {
  salvarTemaNoDispositivo(tema: string): Promise<void>;
  theme: typeof CombinedDarkTheme;
}
export const TemaContext = createContext({});export function TemaProvider({ children }: IProps) {
  const [temaAtualbool, setTemaAtualbool] = useState(true);
  async function salvarTemaNoDispositivo(tema: string) {
    await saveTheme(tema);
    setTemaAtualbool(temabool(tema));
  }
  function temabool(tema: string): boolean {
    if (tema === "escuro") {
      return true;
    }
    return false;
  }
  useEffect(() => {
    getTheme()
      .then((resposta) => {
        if (resposta) {
          setTemaAtualbool(temabool(resposta));
        } else {
          salvarTemaNoDispositivo("escuro");
        }
      })
      .catch((error) => console.log(error));
  }, []);
  return (
    <TemaContext.Provider
      value={{
        theme: temaAtualbool ? CombinedDarkTheme : CombinedDefaultTheme,
        salvarTemaNoDispositivo,
      }}
    >
      {children}
    </TemaContext.Provider>
  );
}