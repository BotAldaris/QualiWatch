import { View } from "react-native";
import { Card, IconButton, Text, Title } from "react-native-paper";
import IReadProduto from "../../../interfaces/Produtos/ReadProduto";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import remover from "../functions/removerProduto";
import ValidadeIcon from "./ValidadeIcon";
interface IProps {
  produto: IReadProduto;
  navigation: NativeStackNavigationProp<any>;
  atualizarDados: Function;
}

export default function ItemProduto({
  produto,
  navigation,
  atualizarDados,
}: IProps) {
  return (
    <Card style={{ margin: 10 }}>
      <Card.Content>
        <View>
          <Title style={{ fontSize: 18 }}>{produto.nome}</Title>
        </View>
        <View>
          <Text>Lote: {produto.lote}</Text>
        </View>
        <View
          style={{
            flexDirection: "row",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <Text>
            Validade: {produto.validade.toLocaleString().slice(0, 10)}
          </Text>
          {ValidadeIcon(produto.validade.toString())}
        </View>
      </Card.Content>
      <Card.Actions style={{ justifyContent: "flex-end" }}>
        <IconButton
          icon="pencil"
          size={20}
          onPress={() => navigation.navigate("AdicionarProduto", { produto })}
          style={{ marginRight: 10 }}
        />
        <IconButton
          icon="delete"
          size={20}
          onPress={() => {
            remover(produto.id, atualizarDados);
          }}
        />
      </Card.Actions>
    </Card>
  );
}
