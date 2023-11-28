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
import DatePickerCamera from "./components/BotaoCamera";
import { useBase64 } from "./hooks/useBase64";
import NomeInput from "./components/ValueInputWithOcr";

interface IProps {
  route: RouteProp<any>;
  navigation: NativeStackNavigationProp<any>;
}

export default function Adicionar({ route, navigation }: IProps) {
  const { nome, setNome, setBase64 } = useBase64();
  const [lote, setLote] = useState("");
  const [validade, setValidade] = useState<Date>(new Date());
  const [id, setId] = useState<string>("");
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
  }, []);

  async function enviarAtualizar() {
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
          <NomeInput valor={nome} setValor={setNome} nome="Nome" />
          <NomeInput valor={lote} setValor={setLote} nome="Lote" />
          <DatePicker validade={validade} setValidade={setValidade} />
          <View></View>
          {id ? (
            <Button
              mode="outlined"
              onPress={async () => await enviarAtualizar()}
            >
              Editar
            </Button>
          ) : (
            <Button
              mode="outlined"
              onPress={async () => await enviarAtualizar()}
            >
              Enviar
            </Button>
          )}
        </View>
      </View>
    </ScrollView>
  );
}
