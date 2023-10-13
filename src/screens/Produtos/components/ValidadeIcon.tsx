import MaterialCommunityIcons from "@expo/vector-icons/MaterialCommunityIcons";

export default function ValidadeIcon(validade: string) {
  const data = new Date(validade);
  const diasFaltando = (data.getTime() - Date.now()) / (1000 * 60 * 60 * 24);

  if (diasFaltando < 0) {
    // Produto vencido
    return <MaterialCommunityIcons name="alert" size={25} color="red" />;
  } else if (diasFaltando < 30) {
    // Menos de 30 dias restantes
    return (
      <MaterialCommunityIcons name="clock-alert" size={25} color="yellow" />
    );
  } else if (diasFaltando < 60) {
    // Menos de 60 dias restantes
    return (
      <MaterialCommunityIcons
        name="clock-alert-outline"
        size={25}
        color="orange"
      />
    );
  } else {
    // Mais de 60 dias restantes
    return <MaterialCommunityIcons name="clock" size={25} color="green" />;
  }
}
