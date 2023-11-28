import { Dimensions } from "react-native";

const windowWidth = Dimensions.get("window").width;

export default function temValidade(): boolean {
  if (768 > windowWidth) {
    return false;
  } else {
    return true;
  }
}
