import ICreateProduto from "../../../../../interfaces/Produtos/CreateProduto";
interface IProps {
  produto: ICreateProduto;
}
export default function verificarTudo({ produto }: IProps): boolean {
  if (!produto.lote) {
    throw new Error("Por favor, preencha o lote do Produto");
  }
  if (!produto.nome) {
    throw new Error("Por favor, preencha o nome do Produto");
  }
  if (!produto.validade) {
    throw new Error("Por favor, preencha a validade do Produto");
  }
  return true;
}
