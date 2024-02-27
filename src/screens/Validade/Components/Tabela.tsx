import { useState } from "react";
import { DataTable, IconButton, MD3Colors } from "react-native-paper";
import IReadProduto from "../../../interfaces/Produtos/ReadProduto";
import temValidade from "../Functions/width";
import { removerDosAlertas } from "../../../services/Validade";

interface IProps {
  produtos: IReadProduto[];
  recarregar: () => Promise<void>;
}

const Tabela = (props: IProps) => {
  const [page, setPage] = useState<number>(0);
  const [numberOfItemsPerPageList] = useState([7, 9, 15]);
  const [itemsPerPage, onItemsPerPageChange] = useState(
    numberOfItemsPerPageList[0]
  );
  const from = page * itemsPerPage;
  const to = Math.min((page + 1) * itemsPerPage, props.produtos.length);
  const validade = temValidade();
  const deleta = async (id: string) => {
    await removerDosAlertas(id);
    await props.recarregar();
  };
  return (
    <DataTable>
      <DataTable.Header>
        <DataTable.Title>Nome</DataTable.Title>
        <DataTable.Title numeric>Lote</DataTable.Title>
        {validade && <DataTable.Title numeric>Validade</DataTable.Title>}
        <DataTable.Title numeric>Apagar</DataTable.Title>
      </DataTable.Header>

      {props.produtos.slice(from, to).map((item) => (
        <DataTable.Row key={item.id}>
          <DataTable.Cell>{item.nome}</DataTable.Cell>
          <DataTable.Cell numeric>{item.lote}</DataTable.Cell>
          {validade && (
            <DataTable.Cell numeric>
              {item.validade.toLocaleDateString()}
            </DataTable.Cell>
          )}
          <DataTable.Cell numeric>
            <IconButton
              icon="delete"
              iconColor={MD3Colors.error50}
              size={20}
              onPress={() => deleta(item.id)}
            />
          </DataTable.Cell>
        </DataTable.Row>
      ))}

      <DataTable.Pagination
        page={page}
        numberOfPages={Math.ceil(props.produtos.length / itemsPerPage)}
        onPageChange={(page) => setPage(page)}
        label={`${from + 1}-${to} de ${props.produtos.length}`}
        numberOfItemsPerPageList={numberOfItemsPerPageList}
        numberOfItemsPerPage={itemsPerPage}
        onItemsPerPageChange={onItemsPerPageChange}
        showFastPaginationControls
        selectPageDropdownLabel={"Linhas por pagina"}
      />
    </DataTable>
  );
};

export default Tabela;
