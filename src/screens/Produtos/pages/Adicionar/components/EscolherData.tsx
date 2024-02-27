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
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  setValor: React.Dispatch<React.SetStateAction<any>>;
  nome: string;
  pegarData: boolean;
}
export default function EscolherData({
  items,
  visivel,
  setVisivel,
  esperar,
  setValor,
  nome,
  pegarData,
}: IProps) {
  const [data, setData] = useState("");
  const esconderDialogo = () => setVisivel(false);
  const confirmar = () => {
    if (pegarData) {
      const dataFormatada = stringParaData(data);
      if (dataFormatada) {
        setValor(dataFormatada);
      }
    } else {
      setValor(data);
    }
    esconderDialogo();
  };
  return (
    <Portal>
      <Dialog visible={visivel} onDismiss={esconderDialogo}>
        <Dialog.Title>Escolha {nome}</Dialog.Title>
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
