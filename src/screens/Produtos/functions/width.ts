import { Dimensions } from "react-native";

const windowWidth = Dimensions.get("window").width;

export default function numeroDeColunas() {
  if (768 > windowWidth) {
    return 1;
  } else if (1024 > windowWidth) {
    return 2;
  } else if (1061 > windowWidth) {
    return 3;
  } else {
    return 4;
  }
}
