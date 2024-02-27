import type { NativeStackNavigationProp } from "@react-navigation/native-stack";
import IReadProduto from "../../interfaces/Produtos/ReadProduto";
import type { RouteProp } from "@react-navigation/native";

export type ProdutosStackNavigatorParamList = {
  ProdutosScreen: undefined;
  AdicionarProduto: {
    produto: IReadProduto | null;
  };
};

export type ProdutosStackNavigatorProp =
  NativeStackNavigationProp<ProdutosStackNavigatorParamList>;

export type AdicionarProdutoRouteProp = RouteProp<
  ProdutosStackNavigatorParamList,
  "AdicionarProduto"
>;
