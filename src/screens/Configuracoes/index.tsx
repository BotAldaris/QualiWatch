import { estilos } from "./estilos";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { useContext, useEffect, useState } from "react";
import { TemaContext, ITemaContext } from "../../contexts/TemaContext";
import { View } from "react-native";
import { Button, Switch, Text, TextInput } from "react-native-paper";
import { getUrl, saveUrl } from "../../services/Url";
interface IProps {
  navigation: NativeStackNavigationProp<any>;
}
export default function Configuracao({ navigation }: IProps) {
  const { salvarTemaNoDispositivo, theme } = useContext(
    TemaContext
  ) as ITemaContext;
  const [url, setUrl] = useState("");
  const estilo = estilos();
  useEffect(() => {
    const carregarUrl = async () => {
      setUrl(await getUrl());
    };
    carregarUrl();
  }, []);
  async function enviar() {
    await saveUrl(url);
  }
  return (
    <View style={estilo.container}>
      <Text style={estilo.titulo}>Configuração</Text>

      <View style={estilo.inputArea}>
        <Text style={estilo.subtitulo}>
          Tema: {theme.dark ? "Escuro" : "Claro"}
        </Text>
        <Switch
          onValueChange={() =>
            theme.dark
              ? salvarTemaNoDispositivo("claro")
              : salvarTemaNoDispositivo("escuro")
          }
          value={theme.dark ? true : false}
        />
      </View>
      <TextInput
        onChangeText={setUrl}
        value={url}
        placeholder="Url"
        style={{width:"90%"}}
      ></TextInput>
      <Button onPress={enviar}>Enviar</Button>
    </View>
  );
}
