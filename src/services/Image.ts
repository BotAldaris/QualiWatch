import axios from "axios";
import { getUrl } from "./Url";

export async function getTextFromImage(base64: string): Promise<any> {
  const url = await getUrl();
  if (!url) {
    throw new Error("Insira a url");
  }
  try {
    return (
      await axios.post(`${url}api/text-extractor`, { base64_image: base64 })
    ).data;
  } catch (e) {
    throw e;
  }
}
