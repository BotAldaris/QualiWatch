import axios from "axios";
import { getUrl } from "./Url";
import ImageResponse from "../interfaces/Images/ImageResponse";

export async function getTextFromImage(
  base64: string,
  validade: boolean = true
): Promise<ImageResponse[]> {
  const url = await getUrl();
  const indicador = "Image";
  if (!url) {
    throw new Error("Insira a url");
  }
  try {
    if (validade) {
      return (
        await axios.post(`${url}/${indicador}/validade`, {
          base64: base64,
        })
      ).data;
    }
    return (
      await axios.post(`${url}/${indicador}/texto`, {
        base64: base64,
      })
    ).data;
  } catch (e) {
    throw "Erro ao converter a imagem para texto";
  }
}
