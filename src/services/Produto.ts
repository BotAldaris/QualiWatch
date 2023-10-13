import IReadProduto from "../interfaces/Produtos/ReadProduto";
import axios from "axios";
import { getUrl } from "./Url";
import ICreateProduto from "../interfaces/Produtos/CreateProduto";
export async function ReadProduto(): Promise<IReadProduto[]> {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    const response = await axios.get(`${url}Produto`);
    return response.data;
  } catch (e) {
    // try {
    //   const valor = await AsyncStorage.getItem("produtos");
    //   if (valor) {
    //     try {
    //       const valorConvertido = JSON.parse(valor) as IReadProduto[];
    //       return valorConvertido;
    //     } catch (error) {
    //       throw new Error(
    //         `Erro ao Converter os Produtos pegos no Async Storage para uma lista: ${error}`
    //       );
    //     }
    //   }
    // } catch (error) {
    //   throw new Error(`Erro ao Pegar os Produtos no Async Storage: ${error}`);
    // }
    throw e;
  }
}

export async function saveProduto(produto: ICreateProduto) {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    await axios.post(`${url}Produto`, produto);
  } catch (e) {
    throw e;
  }
  // try {
  //   await AsyncStorage.setItem("produtos", produtos);
  // } catch (error) {
  //   throw new Error(`Erro ao salvar os produtos no Async Storage ${error}`);
  // }
}

export async function putProduto(produto: ICreateProduto, id: number) {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    await axios.put(`${url}Produto/${id}`, produto);
  } catch (e) {
    throw e;
  }
}

export async function deleteProduto(id: number) {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    await axios.delete(`${url}Produto/${id}`);
  } catch (e) {
    throw e;
  }
}
