import { Dispatch, SetStateAction } from "react";
import { View } from "react-native";
import * as ImagePicker from "expo-image-picker";
import { IconButton } from "react-native-paper";

interface IProps {
  setBase64: Dispatch<SetStateAction<string>>;
}

const getCameraPermission = async () => {
  const { status } = await ImagePicker.requestCameraPermissionsAsync();
  if (status !== "granted") {
    alert("Desculpe, precisamos de permissão para acessar a câmera.");
    return;
  }
};

export default function BotaoCamera(props: IProps) {
  const { setBase64 } = props;

  const pickImage = async () => {
    await getCameraPermission();

    const result = await ImagePicker.launchCameraAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      quality: 1,
      base64: true,
    });

    if (result.assets && result.assets[0] && result.assets[0].base64) {
      setBase64(result.assets[0].base64);
    }
  };

  return (
    <View
      style={{
        flex: 1,
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <IconButton icon="camera" size={25} onPress={pickImage} />
    </View>
  );
}
