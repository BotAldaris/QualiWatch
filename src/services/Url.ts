import AsyncStorage from "@react-native-async-storage/async-storage";

export async function saveUrl(url: string) {
  await AsyncStorage.setItem("url", url);
}
export async function getUrl(): Promise<string> {
  const url = await AsyncStorage.getItem("url");
  if (url) {
    return url;
  }
  
  throw Error("Configure a url.");
}
