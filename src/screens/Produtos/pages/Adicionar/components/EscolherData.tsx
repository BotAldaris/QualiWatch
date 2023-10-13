import { ActivityIndicator, Button, Dialog, Portal } from "react-native-paper";
import DatasTable from "./DatasTable";
import { useState } from "react";
import stringParaData from "../functions/data";

interface IProps {
  items: {
    data: string;
    id: number;
  }[];
  visivel: boolean;
  setVisivel: React.Dispatch<React.SetStateAction<boolean>>;
  esperar: boolean;
  setValidade: React.Dispatch<React.SetStateAction<Date>>;
}
export default function EscolherData({
  items,
  visivel,
  setVisivel,
  esperar,
  setValidade,
}: IProps) {
  const [data, setData] = useState("");
  const esconderDialogo = () => setVisivel(false);
  const confirmar = () => {
    setValidade(stringParaData(data));
    esconderDialogo();
  };
  return (
    <Portal>
      <Dialog visible={visivel} onDismiss={esconderDialogo}>
        <Dialog.Title>Escolha a data</Dialog.Title>
        <Dialog.Content>
          {esperar ? (
            <ActivityIndicator animating={true} />
          ) : (
            <DatasTable setData={setData} items={items} />
          )}
        </Dialog.Content>
        <Dialog.Actions>
          <Button onPress={esconderDialogo}>Cancelar</Button>
          <Button onPress={confirmar}>Confirmar</Button>
        </Dialog.Actions>
      </Dialog>
    </Portal>
  );
}
