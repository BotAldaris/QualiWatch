import DateTimePicker, {
  DateTimePickerEvent,
} from "@react-native-community/datetimepicker";
import { useState } from "react";
import { Platform, Pressable, View } from "react-native";
import { Button, TextInput } from "react-native-paper";
import EscolherData from "./EscolherData";
import { useDatePicker } from "./hooks/useDatePicker";
import BotaoCamera from "./BotaoCamera";

interface IProps {
  validade: Date;
  setValidade: React.Dispatch<React.SetStateAction<Date>>;
}
export default function DatePicker({ validade, setValidade }: IProps) {
  const [show, setShow] = useState(false);
  const { setBase64, setVisivel, visivel, esperar, items } = useDatePicker(true);
  const onChange = (
    event: DateTimePickerEvent,
    selectedDate: Date | undefined
  ) => {
    if (selectedDate) {
      const currentDate = selectedDate;
      setValidade(currentDate);
    }
    setShow(false);
  };
  const toggleDatePicker = () => {
    setShow(!show);
  };
  return (
    <View>
      {show && (
        <DateTimePicker
          testID="dateTimePicker"
          value={validade}
          mode="date"
          display="spinner"
          is24Hour={true}
          onChange={onChange}
        />
      )}
      {show && Platform.OS == "ios" && (
        <View>
          <Button mode="outlined" onPress={toggleDatePicker}>
            Cancelar
          </Button>
          <Button mode="outlined" onPress={toggleDatePicker}>
            Confirmar
          </Button>
        </View>
      )}
      {!show && (
        <View style={{ flexDirection: "row", alignItems: "flex-end" }}>
          <Pressable onPress={toggleDatePicker} style={{ width: "90%" }}>
            <TextInput
              placeholder="Escolha a data"
              value={validade.toLocaleString()}
              label="Validade"
              mode="outlined"
              editable={false}
            />
          </Pressable>
          <BotaoCamera setBase64={setBase64} />
          <EscolherData
            setValor={setValidade}
            items={items}
            visivel={visivel}
            setVisivel={setVisivel}
            esperar={esperar}
            nome="a Data"
            pegarData={true}
          />
        </View>
      )}
    </View>
  );
}
