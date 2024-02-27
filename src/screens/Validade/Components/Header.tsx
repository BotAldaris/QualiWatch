import { Appbar } from "react-native-paper";
import { removerTodosOsAlertas } from "../../../services/Validade";
import { atualizarListaProdutosPertodeVencer } from "../../../services/Produto";
import SyncIcon from "../../Produtos/components/SyncIcon";

interface IProps {
  recarregar: () => Promise<void>;
}

const Header = (props: IProps) => {
  async function apagarTudo() {
    await removerTodosOsAlertas();
    await props.recarregar();
  }
  async function sincronizar() {
    await atualizarListaProdutosPertodeVencer();
    await props.recarregar();
  }
  return (
    <Appbar.Header>
      <Appbar.Content title="Alertas" />
      <SyncIcon fetchDados={sincronizar} />
      <Appbar.Action
        icon="delete-empty"
        size={30}
        onPress={() => apagarTudo()}
      />
    </Appbar.Header>
  );
};

export default Header;
