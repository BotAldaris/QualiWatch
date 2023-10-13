import ICreateProduto from "../../../../../interfaces/Produtos/CreateProduto";
import { putProduto } from "../../../../../services/Produto";

interface IProps {
  produto: ICreateProduto;
  id: number;
}

export default async function editarProduto({ produto, id }: IProps) {
  try {
    putProduto(produto, id);
    return true;
  } catch (error) {
    alert(`Erro ao ler produtos: ${error}`);
    return false;
  }
}
