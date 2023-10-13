import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Adicionar from "../screens/Produtos/pages/Adicionar";
import Principal from "../screens/Produtos";
// import OverviewProduto from "../telas/Produtores/pages/Overview";
const Stack = createNativeStackNavigator();

export default function ProdutosRotas() {
  return (
    <Stack.Navigator screenOptions={() => ({ headerShown: false })}>
      <Stack.Screen name="ProdutosScreen" component={Principal} />
      <Stack.Screen name="AdicionarProduto" component={Adicionar} />
      {/* <Stack.Screen name="OverviewProduto" component={OverviewProduto} /> */}
    </Stack.Navigator>
  );
}
