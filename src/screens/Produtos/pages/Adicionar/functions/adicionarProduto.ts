import ICreateProduto from "../../../../../interfaces/Produtos/CreateProduto";
import { saveProduto } from "../../../../../services/Produto";

interface Iprops {
  produto: ICreateProduto;
}

export default async function adicionarProduto({
  produto,
}: Iprops): Promise<boolean> {
  try {
    await saveProduto(produto);
    return true;
  } catch (error) {
    alert(`Erro ao ler produtos: ${error}`);
    return false;
  }
}
