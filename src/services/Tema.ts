import AsyncStorage from "@react-native-async-storage/async-storage";

export async function saveTheme(theme: string) {
  await AsyncStorage.setItem("theme", theme);
}
export async function getTheme(): Promise<string> {
  const theme = await AsyncStorage.getItem("theme");
  if (theme) {
    return theme;
  }
  return "escuro";
}
