import { View } from "react-native";
import { TextInput } from "react-native-paper";
import EscolherData from "./EscolherData";
import { useDatePicker } from "./hooks/useDatePicker";
import BotaoCamera from "./BotaoCamera";

interface IProps {
  nome: string;
  valor: string;
  setValor: React.Dispatch<React.SetStateAction<string>>;
}
export default function NomeInput({ nome,valor, setValor }: IProps) {
  const { setBase64, setVisivel, visivel, esperar, items } = useDatePicker(false);
  return (
    <View>
        <View style={{ flexDirection: "row", alignItems: "flex-end" }}>
            <TextInput
            style={{ width: "90%" }}
              placeholder={`Escolha o ${nome}`}
              value={valor}
              label={`${nome}`}
              mode="outlined"
            />
            <BotaoCamera setBase64={setBase64} />
            <EscolherData
              setValor={setValor}
              items={items}
              visivel={visivel}
              setVisivel={setVisivel}
              esperar={esperar}
              nome={`o ${nome}`}
              pegarData={false}
            />
        </View>
    </View>
  );
}
