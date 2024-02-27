import { deleteProduto } from "../../../services/Produto";

export default async function remover(
  id: string,
  atualizarDados: () => Promise<void>
) {
  try {
    await deleteProduto(id);
    await atualizarDados();
    return true;
  } catch (error) {
    alert(`Erro ao ler produtos: ${error}`);
    return false;
  }
}
