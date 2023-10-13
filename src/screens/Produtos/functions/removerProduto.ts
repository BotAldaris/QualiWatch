import { deleteProduto } from "../../../services/Produto";

export default async function remover(id: number, atualizarDados: Function) {
  try {
    await deleteProduto(id);
    await atualizarDados();
    return true;
  } catch (error) {
    alert(`Erro ao ler produtos: ${error}`);
    return false;
  }
}
