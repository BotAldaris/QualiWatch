import { useState } from "react";
import { View } from "react-native";
import { DataTable, RadioButton, Text } from "react-native-paper";
import ImageResponse from "../../../../../interfaces/Images/ImageResponse";
interface IProps {
  items: ImageResponse[];
  setData: React.Dispatch<React.SetStateAction<string>>;
}

export default function DatasTable({ items, setData }: IProps) {
  const [escolhido, setEscolhido] = useState(0);
  return (
    <DataTable>
      <DataTable.Header>
        <DataTable.Title>Data</DataTable.Title>
      </DataTable.Header>
      {items.map((item) => (
        <DataTable.Row key={item.id}>
          <DataTable.Cell>
            <View style={{ alignItems: "center", flexDirection: "row" }}>
              <RadioButton
                value={item.data}
                status={escolhido === item.id ? "checked" : "unchecked"}
                onPress={() => {
                  setEscolhido(item.id);
                  console.log(`a data ''e ${item.data}`);
                  setData(item.data);
                }}
              />
              <Text>{item.data}</Text>
            </View>
          </DataTable.Cell>
        </DataTable.Row>
      ))}
    </DataTable>
  );
}
