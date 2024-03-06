import { getUrl } from "./Url";
import ImageResponse from "../interfaces/Images/ImageResponse";

export async function getTextFromImage(
  base64: string,
  validade: boolean = true
): Promise<ImageResponse[]> {
  const url = await getUrl();
  const indicador = "Image";
  const headers = {"Content-type": "application/json; charset=UTF-8"}

  if (!url) {
    throw new Error("Insira a url");
  }
  try {
    let tipo = "texto"
    if (validade) {
      tipo = "validade"
    }
    const res = await fetch(`${url}/${indicador}/${tipo}`,{method:"POST",body:JSON.stringify({
      base64: base64,
    }),headers})
    return await res.json() as ImageResponse[]
  } catch (e) {
    throw "Erro ao converter a imagem para texto";
  }
}
