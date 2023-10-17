import axios from "axios";
import { getUrl } from "./Url";

export async function getTextFromImage(
  base64: string,
  validade: boolean = true
): Promise<any> {
  const url = await getUrl();
  if (!url) {
    throw new Error("Insira a url");
  }
  try {
    if (validade) {
      return (
        await axios.post(`${url}TextExtractor/validade`, {
          base64_image: base64,
        })
      ).data;
    } else {
      return (
        await axios.post(`${url}TextExtractor/texto`, { base64_image: base64 })
      ).data;
    }
  } catch (e) {
    throw e;
  }
}
