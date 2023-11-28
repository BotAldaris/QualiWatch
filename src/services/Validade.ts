import AsyncStorage from "@react-native-async-storage/async-storage";
import { getListaProdutosPertoDeVencerApi } from "./Produto";

const VALIDADE = "validades";

export async function removerDosAlertas(id: string) {
  let produtos = await getListaProdutosPertoDeVencerApi();
  produtos = produtos.filter((p) => p.id != id);
  await AsyncStorage.setItem(VALIDADE, JSON.stringify(produtos));
}

export async function removerTodosOsAlertas() {
  await AsyncStorage.removeItem(VALIDADE);
}
