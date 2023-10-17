import { useState } from "react";

export const useProdutoFilter = () => {
  const [searchQuery, setSearchQuery] = useState("");

  const handleSearchQuery = (pesquisa: string) => {
    setSearchQuery(pesquisa);
  };

  return { searchQuery, handleSearchQuery };
};
