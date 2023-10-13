import { StyleSheet } from "react-native";

export const estilos = () => {
  return StyleSheet.create({
    container: {
      flex: 1,
      alignItems: "center",
      justifyContent: "center",
      textAlign:"center"
    },
    titulo: {
      fontSize: 25,
      fontWeight: "bold",
      marginBottom: 20,
    },
    subtitulo: {
      fontSize: 18,
      fontWeight: "400",
      marginBottom: 20,
    },
    inputArea: {
      height: 200,
      width: "100%",
      alignItems: "center",
      justifyContent: "center",
    },
  });
};