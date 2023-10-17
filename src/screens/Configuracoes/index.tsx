import { estilos } from "./estilos";
import { useContext } from "react";
import { TemaContext, ITemaContext } from "../../contexts/TemaContext";
import { View } from "react-native";
import { Button, Switch, Text, TextInput } from "react-native-paper";
import { saveUrl } from "../../services/Url";
import { useUrl } from "./hooks/useUrl";
import { useOnOff } from "./hooks/useOnOff";

export default function Configuracao() {
  const { salvarTemaNoDispositivo, theme } = useContext(
    TemaContext
  ) as ITemaContext;

  const { url, handleUrl } = useUrl();
  const { onOff, handleOnOff } = useOnOff();
  const estilo = estilos();

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
      <View style={estilo.inputArea}>
        <Text style={estilo.subtitulo}>
          Salvar: {onOff == "online" ? "Online" : "Offline"}
        </Text>
        <Switch
          onValueChange={() =>
            onOff == "online" ? handleOnOff("offline") : handleOnOff("online")
          }
          value={onOff == "online" ? true : false}
        />
      </View>
      <TextInput
        onChangeText={handleUrl}
        value={url}
        placeholder="Url"
        style={{ width: "90%" }}
      ></TextInput>
      <Button onPress={enviar}>Enviar</Button>
    </View>
  );
}
