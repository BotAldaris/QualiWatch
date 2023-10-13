import { NavigationContainer } from "@react-navigation/native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { useContext } from "react";
import { PaperProvider } from "react-native-paper";
import { ITemaContext, TemaContext } from "../contexts/TemaContext";
import MaterialCommunityIcons from "@expo/vector-icons/MaterialCommunityIcons";
import Configuracao from "../screens/Configuracoes";
import ProdutosRotas from "./ProdutosRotas";
import { StatusBar } from "react-native";
const Tab = createBottomTabNavigator();
interface IIcon {
  nome: string;
  color: string;
}
function Icon({ nome, color }: IIcon) {
  if (nome === "Produtos") {
    return <MaterialCommunityIcons name="monitor" size={25} color={color} />;
  } else {
    return <MaterialCommunityIcons name="cog" size={25} color={color} />;
  }
}
export default function AppRotas() {
  const { theme } = useContext(TemaContext) as ITemaContext;
  StatusBar.setBarStyle("dark-content", theme.dark);
  return (
    <PaperProvider theme={theme}>
      <NavigationContainer theme={theme}>
        <StatusBar
          backgroundColor={theme.colors.background}
          barStyle={theme.dark ? "light-content" : "dark-content"}
        />
        <Tab.Navigator
          screenOptions={({ route }) => ({
            headerShown: false,
            tabBarIcon: ({ color }) => Icon({ nome: route.name, color }),
          })}
        >
          <Tab.Screen name="Produtos" component={ProdutosRotas} />
          <Tab.Screen name="Configuracao" component={Configuracao} />
        </Tab.Navigator>
      </NavigationContainer>
    </PaperProvider>
  );
}
