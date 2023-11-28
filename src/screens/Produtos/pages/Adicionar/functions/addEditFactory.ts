import ICreateProduto from "../../../../../interfaces/Produtos/CreateProduto";
import adicionarProduto from "./adicionarProduto";
import editarProduto from "./edit";
import verificarTudo from "./verificarTudo";

interface IProps {
  id: string;
  produto: ICreateProduto;
}

export async function addEditFactory({
  id,
  produto,
}: IProps): Promise<boolean> {
  try {
    if (verificarTudo({ produto })) {
      if (id == "") {
        await adicionarProduto({ produto });
      } else {
        await editarProduto({ produto, id });
      }
    } else {
      alert("Preencha todos os valores e verifique se todos estão corretos");
      return false;
    }
  } catch (error) {
    alert(error);
    return false;
  }
  return true;
}
