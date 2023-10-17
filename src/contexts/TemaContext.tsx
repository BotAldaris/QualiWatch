import { ReactNode, createContext, useEffect, useState } from "react";
import { getTheme, saveTheme } from "../services/Tema";
import { CombinedDarkTheme, CombinedDefaultTheme } from "../estilosGlobais";
interface IProps {
  children: ReactNode;
}
export interface ITemaContext {
  salvarTemaNoDispositivo(tema: string): Promise<void>;
  theme: typeof CombinedDarkTheme;
}
export const TemaContext = createContext({});
export function TemaProvider({ children }: IProps) {
  const [temaAtualbool, setTemaAtualbool] = useState(true);

  async function salvarTemaNoDispositivo(tema: string) {
    await saveTheme(tema);
    setTemaAtualbool(isTemaEscuro(tema));
  }

  const isTemaEscuro = (tema: string) => (tema === "escuro" ? true : false);

  const fetchTema = async () => {
    try {
      const resposta = await getTheme();
      if (resposta) {
        setTemaAtualbool(isTemaEscuro(resposta));
      } else {
        await salvarTemaNoDispositivo(resposta);
      }
    } catch (error) {
      console.log(`Erro ao pegar o tema no dispostivo ${error}`);
    }
  };
  useEffect(() => {
    fetchTema();
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
