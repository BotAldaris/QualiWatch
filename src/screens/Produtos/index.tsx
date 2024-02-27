import { FlatList, View } from "react-native";
import ItemProduto from "./components/ItemProduto";
import { Searchbar } from "react-native-paper";
import { useProdutos } from "./hooks/useProdutos";
import { useProdutoFilter } from "./hooks/useProdutoFillter";
import SyncIcon from "./components/SyncIcon";
import numeroDeColunas from "./functions/width";
import { ProdutosStackNavigatorProp } from "../../routes/Types/ProdutosStackNavigator";
import Header from "./components/Header";

interface IProps {
  navigation: ProdutosStackNavigatorProp;
}

export default function Produtos({ navigation }: IProps) {
  const { searchQuery, handleSearchQuery } = useProdutoFilter();
  const { produtos, fetchDados } = useProdutos(searchQuery);
  return (
    <View style={{ flex: 1 }}>
      <Header navigation={navigation} titulo="Produtos">
        <SyncIcon fetchDados={fetchDados} />
      </Header>

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
