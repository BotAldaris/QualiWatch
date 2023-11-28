import { useEffect, useState } from "react";
import IReadProduto from "../../../interfaces/Produtos/ReadProduto";
import {
  atualizarListaProdutosPertodeVencer,
  getListaProdutosPertoDeVencer,
} from "../../../services/Produto";

export const useAlerta = () => {
  const [produtos, setProdutos] = useState([] as IReadProduto[]);
  async function fetchProdutos() {
    await atualizarListaProdutosPertodeVencer();
    const produtosApi = await getListaProdutosPertoDeVencer();
    console.log("oi");
    setProdutos(produtosApi);
  }
  useEffect(() => {
    fetchProdutos();
  }, []);

  return { produtos, fetchProdutos };
};
