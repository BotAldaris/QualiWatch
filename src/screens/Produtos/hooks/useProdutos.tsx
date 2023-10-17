import { useCallback, useEffect, useState } from "react";
import IReadProduto from "../../../interfaces/Produtos/ReadProduto";
import { ReadProduto } from "../../../services/Produto";
import { useFocusEffect } from "@react-navigation/native";

export const useProdutos = (searchQuery: string) => {
  const [produtosOriginal, setProdutosOrignal] = useState([] as IReadProduto[]);
  const [produtos, setProdutos] = useState([] as IReadProduto[]);

  const fetchDados = async () => {
    try {
      const resposta = await ReadProduto();
      setProdutosOrignal(resposta);
    } catch (error) {
      alert(error);
    }
  };

  const handleFilter = () => {
    if (searchQuery) {
      const produtosFiltrados = produtosOriginal.filter((produto) =>
        produto.nome.includes(searchQuery)
      );
      setProdutos(produtosFiltrados);
    } else {
      setProdutos(produtosOriginal);
    }
  };

  useEffect(() => {
    handleFilter();
  }, [produtosOriginal, searchQuery]);

  useFocusEffect(
    useCallback(() => {
      fetchDados();
    }, [])
  );

  return { produtos, fetchDados };
};
