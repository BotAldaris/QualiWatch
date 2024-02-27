import { useEffect } from "react";
import { View, Animated, Easing, Pressable } from "react-native";
import { Icon } from "react-native-paper";
import { useSync } from "./Hooks/useSync";

interface IProps {
  fetchDados: () => Promise<void>;
}

function SyncIcon({ fetchDados }: IProps) {
  const spinValue = new Animated.Value(0);
  const { girando, sincronizar } = useSync(fetchDados);
  useEffect(() => {
    if (girando) {
      startSpin();
    } else {
      stopSpin();
    }
  }, [girando]);

  const startSpin = () => {
    spinValue.setValue(0);
    Animated.timing(spinValue, {
      toValue: 1,
      duration: 3000,
      easing: Easing.linear,
      useNativeDriver: true,
    }).start(() => startSpin());
  };

  const stopSpin = () => {
    spinValue.stopAnimation();
  };

  const spin = spinValue.interpolate({
    inputRange: [0, 1],
    outputRange: ["0deg", "360deg"],
  });

  return (
    <Pressable onPress={sincronizar}>
      <View>
        <Animated.View
          style={{
            transform: [{ rotate: girando ? spin : "0deg" }],
          }}
        >
          <Icon source="sync" size={30} />
        </Animated.View>
      </View>
    </Pressable>
  );
}

export default SyncIcon;
