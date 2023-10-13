import React, { useState, useEffect } from "react";
import { Image, View, Platform } from "react-native";
import * as ImagePicker from "expo-image-picker";
import { Button, Text } from "react-native-paper";
import { getTextFromImage } from "../../../../../services/Image";
import EscolherData from "./EscolherData";
interface IProps {
  setValidade: React.Dispatch<React.SetStateAction<Date>>;
}
const getCameraPermission = async () => {
  const { status } = await ImagePicker.requestCameraPermissionsAsync();
  if (status !== "granted") {
    alert("Desculpe, precisamos de permissão para acessar a câmera.");
    return;
  }
};

export default function DatePickerCamera({ setValidade }: IProps) {
  const [image, setImage] = useState<string | null>(null);
  const [base64, setBase64] = useState("");
  const [esperar, setEsperar] = useState(false);
  const [visivel, setVisivel] = useState(false);
  const [items, setItems] = useState<any>();

  const pickImage = async () => {
    await getCameraPermission();
    let result = await ImagePicker.launchCameraAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      quality: 1,
      base64: true,
    });
    if (result.assets) {
      if (result.assets[0]) {
        if (result.assets[0].base64) {
          setBase64(result.assets[0].base64);
        }
        setImage(result.assets[0].uri);
      }
    }
  };
  const RetirarTexto = async () => {
    setEsperar(true);
    setVisivel(true);
    console.log(items);
    const dados = await getTextFromImage(base64);
    setItems(dados);
    setEsperar(false);
  };
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
      <Button onPress={pickImage}>Pick an image from camera roll</Button>
      {image && (
        <Image source={{ uri: image }} style={{ width: 200, height: 200 }} />
      )}
      <Button onPress={RetirarTexto}>Processar</Button>
      <EscolherData
        visivel={visivel}
        setVisivel={setVisivel}
        items={items}
        esperar={esperar}
        setValidade={setValidade}
      />
    </View>
  );
}
