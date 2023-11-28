import { Dimensions, FlatList, View } from "react-native";
import HeaderPrincipal from "../../components/Header";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import ItemProduto from "./components/ItemProduto";
import { Searchbar } from "react-native-paper";
import { useProdutos } from "./hooks/useProdutos";
import { useProdutoFilter } from "./hooks/useProdutoFillter";
import SyncIcon from "./components/SyncIcon";
import numeroDeColunas from "./functions/width";

interface IProps {
  navigation: NativeStackNavigationProp<any>;
}

export default function Produtos({ navigation }: IProps) {
  const { searchQuery, handleSearchQuery } = useProdutoFilter();
  const { produtos, fetchDados } = useProdutos(searchQuery);
  return (
    <View style={{ flex: 1 }}>
      <HeaderPrincipal
        navigation={navigation}
        titulo="Produtos"
        destino="AdicionarProduto"
        children={<SyncIcon fetchDados={fetchDados} />}
      />

      <FlatList
        numColumns={numeroDeColunas()}
        data={produtos}
        renderItem={(produto) => (
          <ItemProduto
            produto={produto.item}
            navigation={navigation}
            atualizarDados={fetchDados}
          />
        )}
        keyExtractor={(produto) => produto.id.toString()}
        ListHeaderComponent={
          <Searchbar
            style={{ marginTop: 16 }}
            placeholder="Procurar"
            onChangeText={handleSearchQuery}
            value={searchQuery}
          />
        }
      />
    </View>
  );
}
