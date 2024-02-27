import { Appbar } from "react-native-paper";
import { ProdutosStackNavigatorProp } from "../../../../../routes/Types/ProdutosStackNavigator";
interface IProps {
  navigation: ProdutosStackNavigatorProp;
  editar: boolean;
}
export default function Header({ navigation, editar }: IProps) {
  return (
    <Appbar.Header>
      <Appbar.BackAction onPress={() => navigation.popToTop()} />
      {editar ? (
        <Appbar.Content title="Editar Produto" />
      ) : (
        <Appbar.Content title="Adicionar Produto" />
      )}
    </Appbar.Header>
  );
}
