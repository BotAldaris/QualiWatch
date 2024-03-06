import IReadProduto from "../interfaces/Produtos/ReadProduto";
import { getUrl } from "./Url";
import ICreateProduto from "../interfaces/Produtos/CreateProduto";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { getEstado } from "./Estado";
import NetInfo from "@react-native-community/netinfo";
import IReadProdutoApi from "../interfaces/Produtos/ReadProdutoApi";
const headers = {"Content-type": "application/json; charset=UTF-8"}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const dateTimeReviver = function (key: any, value: any) {
  let a;
  if (typeof value === "string") {
    a = /\/Date\((\d*)\)\//.exec(value);
    if (a) {
      return new Date(+a[1]);
    }
  }
  return value;
};

const baseUrl = async () => {
  const base = await getUrl();
  if (!base) {
    throw new Error("Insira a url");
  }
  const result = `${base}/Produtos`;
  return result;
};

export async function ReadProduto(): Promise<IReadProduto[]> {
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
}
const ReadProdutoApi = async () => {
  try {
    const url = await baseUrl();
    const resp= await fetch(url)
    const response = await resp.json()
    return response as IReadProduto[];
  } catch (e) {
    const errors = e as Error
    console.log(errors.stack  )
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
  const estado = await getEstado();
  if (estado == "online") {
    return await saveProdutoApi(produto);
  } else {
    return await saveProdutoAsyncStorage(produto);
  }
}

const saveProdutoApi = async (produto: ICreateProduto) => {
  try {
    const url = await baseUrl();
    await fetch(url,{method:"POST",body: JSON.stringify(produto),headers})
  } catch (e) {
    const errors = e as Error
    throw new Error("Erro ao salvar o produto no servidor, erro " + errors.message);
  }
};

const saveProdutoAsyncStorage = async (produto: ICreateProduto) => {
  try {
    let dados = await ReadProdutoAsyncStorage();
    if (dados && dados.length >= 1) {
      const novoProduto = {
        ...produto,
        id: `${Number.parseInt(dados[dados.length - 1].id) + 1}`,
      };
      dados.push(novoProduto);
    } else {
      dados = [] as IReadProduto[];
      const novoProduto = {
        ...produto,
        id: "1",
      };
      dados.push(novoProduto);
    }
    AsyncStorage.setItem("produtos", JSON.stringify(dados));
  } catch (e) {
    throw new Error(`Erro ao adicionar o produto offline, erro: ${e}}`);
  }
};
export async function putProduto(produto: ICreateProduto, id: string) {
  const estado = await getEstado();
  if (estado == "online") {
    return await putProdutoApi(produto, id);
  } else {
    return await putProdutoAyncStorage(produto, id);
  }
}

const putProdutoApi = async (produto: ICreateProduto, id: string) => {
  try {
    const url = await baseUrl();
    await fetch(`${url}/${id}`,{method:"PUT",body: JSON.stringify(produto), headers})
  } catch (e) {
    const err = e as Error
    throw new Error(`Erro ao editar o produto online, erro ${err.message}}`);
  }
};

const putProdutoAyncStorage = async (produto: ICreateProduto, id: string) => {
  try {
    const dados = await ReadProdutoAsyncStorage();
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

export async function deleteProduto(id: string) {
  const estado = await getEstado();
  if (estado == "online") {
    return await deleteProdutoApi(id);
  } else {
    return await deleteProdutoAyncStorage(id);
  }
}

const deleteProdutoApi = async (id: string) => {
  try {
    const url = await baseUrl();
    await fetch(`${url}/${id}`,{method:"DELETE"})
  } catch (e) {
    const errors = e as Error
    throw new Error("Erro ao deletar o produto no servidor, erro" + errors.message);
  }
};

const deleteProdutoAyncStorage = async (id: string) => {
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
    try {
      const savePromises = dados.map((produto) => saveProdutoApi(produto));

      await Promise.all(savePromises);
      await AsyncStorage.setItem("produtos", "");
    } catch (errors) {
      const produtosNaoSalvos: IReadProduto[] = [];

      if (Array.isArray(errors)) {
        errors.forEach((error, index) => {
          if (error) {
            produtosNaoSalvos.push(dados[index]);
          } else {
            console.error(
              "Erro desconhecido durante o salvamento do produto no índice:",
              index
            );
          }
        });
        if (produtosNaoSalvos.length > 0) {
          await AsyncStorage.setItem(
            "produtos",
            JSON.stringify(produtosNaoSalvos)
          );
          throw new Error(
            "Erro ao Sincronizar os produtos. Alguns produtos não foram salvos no servidor e foram mantidos offline."
          );
        }
      } else {
        console.error(
          "Erro desconhecido durante a leitura do AsyncStorage:",
          errors
        );
        throw new Error("Erro ao ler dados do AsyncStorage");
      }
    }
  }
}

export async function atualizarListaProdutosPertodeVencer() {
  try {
    const body = await criarBodyComValidade();
    const url = await baseUrl();
    const data = new Date().toISOString();
    const resposta = await  fetch(`${url}/validade`,{method:"POST",body:JSON.stringify(body),headers})
    const produtosSemDataFormatada = await resposta.json() as IReadProdutoApi[];
    const produtos = await getListaProdutosPertoDeVencerApi();
    const result = [...produtosSemDataFormatada, ...produtos];
    await AsyncStorage.setItem("validades", JSON.stringify(result));
    await AsyncStorage.setItem("ultimaAtulizacao", data);
  } catch (e) {
    alert("Erro ao atualizar a pagina alerta, erro" + e);
  }
}

export async function getListaProdutosPertoDeVencerApi(): Promise<
  IReadProdutoApi[]
> {
  try {
    const produtosString = await AsyncStorage.getItem("validades");
    if (produtosString) {
      const produtos = JSON.parse(produtosString) as IReadProdutoApi[];
      return produtos;
    }
    return [] as IReadProdutoApi[];
  } catch (error) {
    const errors = error as Error
    throw new Error(`Erro ao Pegar os Alertas no Async Storage: ${errors.message}`);
  }
}

function converterApiparaProduto(
  produtosApi: IReadProdutoApi[]
): IReadProduto[] {
  const result = [] as IReadProduto[];
  produtosApi.forEach((produto) => {
    const novoProduto = {
      ...produto,
      validade: new Date(produto.validade),
    };
    result.push(novoProduto);
  });
  return result;
}

export async function getListaProdutosPertoDeVencer(): Promise<IReadProduto[]> {
  const produtosSemDataFormatada = await getListaProdutosPertoDeVencerApi();
  const result = converterApiparaProduto(produtosSemDataFormatada);
  return result;
}

async function criarBodyComValidade(): Promise<Body> {
  const validade = await AsyncStorage.getItem("ultimaAtulizacao");
  if (validade) {
    return { ultimaAtulizacao: validade };
  }
  return {} as Body;
}
interface Body {
  ultimaAtulizacao: string;
}
