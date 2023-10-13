import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { Appbar } from "react-native-paper";
interface IProps {
  navigation: NativeStackNavigationProp<any>;
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