import IReadProduto from "../interfaces/Produtos/ReadProduto";
import axios from "axios";
import { getUrl } from "./Url";
import ICreateProduto from "../interfaces/Produtos/CreateProduto";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { getEstado } from "./Estado";
import NetInfo from "@react-native-community/netinfo";
const dateTimeReviver = function (key: any, value: any) {
  var a;
  if (typeof value === "string") {
    a = /\/Date\((\d*)\)\//.exec(value);
    if (a) {
      return new Date(+a[1]);
    }
  }
  return value;
};

export async function ReadProduto(): Promise<IReadProduto[]> {
  try {
    const estado = await getEstado();
    if (estado == "online") {
      return await ReadProdutoApi();
    } else {
      const resultado = await ReadProdutoAsyncStorage();
      if (resultado) {
        return resultado;
      }
      return [] as IReadProduto[];
    }
  } catch (e) {
    throw e;
  }
}
const ReadProdutoApi = async () => {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    const response = await axios.get(`${url}Produto`);
    return response.data as IReadProduto[];
  } catch (e) {
    throw new Error(`Erro ao pegar os produtos no banco de dados ${e}`);
  }
};

const ReadProdutoAsyncStorage = async () => {
  try {
    const valor = await AsyncStorage.getItem("produtos");
    if (valor) {
      try {
        const valorConvertido = JSON.parse(
          valor,
          dateTimeReviver
        ) as IReadProduto[];
        return valorConvertido;
      } catch (error) {
        throw new Error(
          `Erro ao Converter os Produtos pegos no Async Storage para uma lista: ${error}`
        );
      }
    } else {
      return [] as IReadProduto[];
    }
  } catch (error) {
    throw new Error(`Erro ao Pegar os Produtos no Async Storage: ${error}`);
  }
};

export async function saveProduto(produto: ICreateProduto) {
  try {
    const estado = await getEstado();
    if (estado == "online") {
      return await saveProdutoApi(produto);
    } else {
      return await saveProdutoAsyncStorage(produto);
    }
  } catch (e) {
    throw e;
  }
}

const saveProdutoApi = async (produto: ICreateProduto) => {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    await axios.post(`${url}Produto`, produto);
  } catch (e) {
    throw e;
  }
};

const saveProdutoAsyncStorage = async (produto: ICreateProduto) => {
  try {
    let dados = await ReadProdutoAsyncStorage();
    if (dados && dados.length >= 1) {
      const novoProduto = {
        ...produto,
        id: dados[dados.length - 1].id + 1,
      };
      dados.push(novoProduto);
    } else {
      dados = [] as IReadProduto[];
      const novoProduto = {
        ...produto,
        id: 1,
      };
      dados.push(novoProduto);
    }
    AsyncStorage.setItem("produtos", JSON.stringify(dados));
  } catch (e) {
    throw new Error(`Erro ao adicionar o produto offline, erro: ${e}}`);
  }
};
export async function putProduto(produto: ICreateProduto, id: number) {
  try {
    const estado = await getEstado();
    if (estado == "online") {
      return await putProdutoApi(produto, id);
    } else {
      return await putProdutoAyncStorage(produto, id);
    }
  } catch (e) {
    throw e;
  }
}

const putProdutoApi = async (produto: ICreateProduto, id: number) => {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    await axios.put(`${url}Produto/${id}`, produto);
  } catch (e) {
    throw new Error(`Erro ao editar o produto online, erro ${e}}`);
  }
};

const putProdutoAyncStorage = async (produto: ICreateProduto, id: number) => {
  try {
    let dados = await ReadProdutoAsyncStorage();
    if (dados) {
      const novoProduto = {
        ...produto,
        id,
      };
      const posicaoDoProdutoVelho = dados.findIndex((x) => x.id == id);
      console.log(dados[posicaoDoProdutoVelho]);
      console.log(novoProduto);
      dados[posicaoDoProdutoVelho] = novoProduto;
      console.log(dados[posicaoDoProdutoVelho]);

      AsyncStorage.setItem("produtos", JSON.stringify(dados));
    }
  } catch (e) {
    throw new Error(`Erro ao editar o produto offline, erro ${e}}`);
  }
};

export async function deleteProduto(id: number) {
  try {
    const estado = await getEstado();
    if (estado == "online") {
      return await deleteProdutoApi(id);
    } else {
      return await deleteProdutoAyncStorage(id);
    }
  } catch (e) {
    throw e;
  }
}

const deleteProdutoApi = async (id: number) => {
  try {
    const url = await getUrl();
    if (!url) {
      throw new Error("Insira a url");
    }
    await axios.delete(`${url}Produto/${id}`);
  } catch (e) {
    throw e;
  }
};

const deleteProdutoAyncStorage = async (id: number) => {
  try {
    let dados = await ReadProdutoAsyncStorage();
    if (dados) {
      dados = dados.filter((x) => x.id != id);
      AsyncStorage.setItem("produtos", JSON.stringify(dados));
    }
  } catch (e) {
    throw new Error(`Erro ao editar o produto offline, erro ${e}}`);
  }
};

export async function SincronizarProdutoAyncStorageParaApi() {
  let conectado: boolean | null = false;
  conectado = (await NetInfo.fetch()).isConnected;

  if (conectado) {
    const dados = await ReadProdutoAsyncStorage();
    const savePromises = dados.map((produto) => saveProdutoApi(produto));
    try {
      await Promise.all(savePromises);
      await AsyncStorage.setItem("produtos", "");
      return;
    } catch (errors: any) {
      const produtosNaoSalvos = [] as IReadProduto[];
      errors.forEach((error: Error | undefined, index: number) => {
        if (error) {
          produtosNaoSalvos.push(dados[index]);
        } else {
          console.error("Erro desconhecido durante o salvamento do produto");
        }
      });
      await AsyncStorage.setItem("produtos", JSON.stringify(dados));
      throw new Error(
        `Erro ao Sincronizar os produtos, os produtos nao salvos se mantiveram no offline`
      );
    }
  }
}
