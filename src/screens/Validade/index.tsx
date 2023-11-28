import Header from "./Components/Header";
import Tabela from "./Components/Tabela";
import { useAlerta } from "./hooks/useAlerta";

const Alerta = () => {
  const { fetchProdutos, produtos } = useAlerta();
  return (
    <>
      <Header recarregar={fetchProdutos} />
      <Tabela produtos={produtos} recarregar={fetchProdutos} />
    </>
  );
};

export default Alerta;
