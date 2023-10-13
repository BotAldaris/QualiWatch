import { useContext, useEffect, useState } from "react";
import { ScrollView, View } from "react-native";
import { TextInput, Button } from "react-native-paper";
import { TemaContext, ITemaContext } from "../../../../contexts/TemaContext";
import { RouteProp } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import Header from "./components/Header";
import DatePicker from "./components/DatePicker";
import { addEditFactory } from "./functions/addEditFactory";
import ICreateProduto from "../../../../interfaces/Produtos/CreateProduto";
import IReadProduto from "../../../../interfaces/Produtos/ReadProduto";
import DatePickerCamera from "./components/DatePickerCamera";

interface IProps {
  route: RouteProp<any>;
  navigation: NativeStackNavigationProp<any>;
}

export default function Adicionar({ route, navigation }: IProps) {
  const [nome, setNome] = useState("");
  const [lote, setLote] = useState("");
  const [validade, setValidade] = useState<Date>(new Date());
  const [id, setId] = useState<number>(0);
  const { theme } = useContext(TemaContext) as ITemaContext;
  const data = route.params?.produto as IReadProduto;
  const editar = data ? true : false;

  useEffect(() => {
    if (data) {
      setNome(data.nome);
      setId(data.id);
      setValidade(new Date(data.validade));
      setLote(data.lote);
    }
    console.log(route.params);
  }, []);

  async function enviarAtualizar(funcao: string) {
    const produto = {
      nome,
      validade,
      lote,
    } as ICreateProduto;
    if (await addEditFactory({ produto, id })) {
      navigation.popToTop();
    }
  }

  return (
    <ScrollView>
      <Header navigation={navigation} editar={editar} />
      <View
        style={{
          width: "100%",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <View style={{ width: "95%", gap: 16 }}>
          <TextInput
            style={{ marginTop: 16 }}
            placeholder="Nome do produto"
            label="Nome do produto"
            placeholderTextColor={theme.colors.primary}
            autoCapitalize="none"
            value={nome}
            onChangeText={setNome}
            mode="outlined"
          />
          <View>
            <TextInput
              placeholder="Lote"
              label="Lote"
              value={lote}
              onChangeText={setLote}
              mode="outlined"
            />
          </View>
          <DatePicker validade={validade} setValidade={setValidade} />
          <View></View>
          {id != 0 ? (
            <Button
              mode="outlined"
              onPress={async () => await enviarAtualizar("editar")}
            >
              Editar
            </Button>
          ) : (
            <Button
              mode="outlined"
              onPress={async () => await enviarAtualizar("adicionar")}
            >
              Enviar
            </Button>
          )}
          <DatePickerCamera setValidade={setValidade}></DatePickerCamera>
        </View>
      </View>
    </ScrollView>
  );
}
