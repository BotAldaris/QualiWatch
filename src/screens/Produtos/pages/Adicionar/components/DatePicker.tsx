import DateTimePicker, {
  DateTimePickerEvent,
} from "@react-native-community/datetimepicker";
import { useState } from "react";
import { Platform, Pressable, View } from "react-native";
import { Button, IconButton, TextInput } from "react-native-paper";

interface IProps {
  validade: Date;
  setValidade: React.Dispatch<React.SetStateAction<Date>>;
}
export default function DatePicker({ validade, setValidade }: IProps) {
  const [show, setShow] = useState(false);
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
        <Pressable onPress={toggleDatePicker}>
          <TextInput
            placeholder="Escolha a data"
            value={validade.toLocaleString()}
            label="Validade"
            mode="outlined"
            editable={false}
          />
        </Pressable>
      )}
    </View>
  );
}
