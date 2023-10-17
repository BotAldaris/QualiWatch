import AsyncStorage from "@react-native-async-storage/async-storage";
export type estado = "online" | "offline";
export async function saveEstado(estado: estado) {
  await AsyncStorage.setItem("estado", estado);
}
export async function getEstado(): Promise<estado> {
  const estado = await AsyncStorage.getItem("estado");
  if (estado) {
    return estado as estado;
  } else {
    return "online";
  }
}
