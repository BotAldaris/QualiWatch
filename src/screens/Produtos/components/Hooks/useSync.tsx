import { useState } from "react";
import { SincronizarProdutoAyncStorageParaApi } from "../../../../services/Produto";
import { getEstado } from "../../../../services/Estado";

export const useSync = (fetchDados: () => Promise<void>) => {
  const [girando, setGirando] = useState(false);

  const sincronizar = async () => {
    try {
      setGirando(true);
      await SincronizarProdutoAyncStorageParaApi();
      const resposta = await getEstado();
      if (resposta) {
        await fetchDados();
      }
      setGirando(false);
    } catch (e) {
      setGirando(false);
      alert(e);
    }
  };
  return { girando, sincronizar };
};
