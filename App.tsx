import AppRotas from "./src/routes/AppRotas";
import { TemaProvider } from "./src/contexts/TemaContext";
export default function App() {
  return (
    <TemaProvider>
      <AppRotas />
    </TemaProvider>
  );
}
