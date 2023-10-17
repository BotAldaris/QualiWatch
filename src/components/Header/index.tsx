import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { ReactNode } from "react";
import { Appbar } from "react-native-paper";
interface IProps {
  navigation: NativeStackNavigationProp<any>;
  children?: ReactNode;
  titulo: string;
  destino: string;
}
export default function HeaderPrincipal(props: IProps) {
  const { children, titulo, navigation, destino } = props;

  return (
    <Appbar.Header>
      <Appbar.Content title={titulo} />
      <Appbar.Action
        icon="plus-thick"
        size={30}
        onPress={() => navigation.navigate(destino)}
      />
      {children}
    </Appbar.Header>
  );
}
