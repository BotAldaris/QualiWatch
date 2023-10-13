import { FlatList, View } from "react-native";
import HeaderPrincipal from "../../components/Header";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import React, { useEffect, useState } from "react";
import { useFocusEffect } from "@react-navigation/native";
import IReadProduto from "../../interfaces/Produtos/ReadProduto";
import { ReadProduto } from "../../services/Produto";
import ItemProduto from "./components/ItemProduto";
import { Searchbar } from "react-native-paper";

interface IProps {
  navigation: NativeStackNavigationProp<any>;
}
export default function Produtos({ navigation }: IProps) {
  const [dados, setDados] = useState([] as IReadProduto[]);
  const [searchQuery, setSearchQuery] = useState("");
  const fetchDados = async () => {
    try {
      setDados(await ReadProduto());
    } catch (error) {
      alert(error);
    }
  };
  useFocusEffect(
    React.useCallback(() => {
      fetchDados();
    }, [])
  );
  const filtrarPorNome = async () => {
    const novosDados = await ReadProduto();
    if (searchQuery) {
      setDados(
        novosDados.filter((produto) =>
          produto.nome.toLowerCase().includes(searchQuery.toLocaleLowerCase())
        )
      );
    } else {
      setDados(novosDados);
    }
  };
  useEffect(() => {
    filtrarPorNome();
  }, [searchQuery]);
  return (
    <View style={{ flex: 1 }}>
      <HeaderPrincipal
        navigation={navigation}
        titulo="Produtos"
        destino="AdicionarProduto"
      />

      <FlatList
        data={dados}
        renderItem={(produto) => (
          <View>
            <ItemProduto
              produto={produto.item}
              navigation={navigation}
              atualizarDados={filtrarPorNome}
            />
          </View>
        )}
        keyExtractor={(produto) => produto.id.toString()}
        ListHeaderComponent={
          <Searchbar
            style={{ marginTop: 16 }}
            placeholder="Procurar"
            onChangeText={(value) => {
              setSearchQuery(value);
            }}
            value={searchQuery}
          />
        }
      />
    </View>
  );
}
