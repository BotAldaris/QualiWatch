import { ReactNode } from "react";
import { Appbar } from "react-native-paper";
import { ProdutosStackNavigatorProp } from "../../../routes/Types/ProdutosStackNavigator";
interface IProps {
  navigation: ProdutosStackNavigatorProp;
  children?: ReactNode;
  titulo: string;
}
export default function Header(props: IProps) {
  const { children, titulo, navigation } = props;

  return (
    <Appbar.Header>
      <Appbar.Content title={titulo} />
      <Appbar.Action
        icon="plus-thick"
        size={30}
        onPress={() =>
          navigation.navigate("AdicionarProduto", { produto: null })
        }
      />
      {children}
    </Appbar.Header>
  );
}
