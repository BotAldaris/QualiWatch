export default function stringParaData(data: string): Date {
  const tamanho: number = data.length;
  const parts = data.split(/[./\s-]/);
  if (tamanho === 10) {
    // dd/mm/yyyy
    const dia = Number.parseInt(parts[0]);
    const mes = Number.parseInt(parts[1]) - 1;
    const ano = Number.parseInt(parts[2]);
    return new Date(ano, mes, dia);
  } else if (tamanho === 8) {
    // dd/mm/yy
    const dia = Number.parseInt(parts[0]);
    const mes = Number.parseInt(parts[1]) - 1;
    const ano = Number.parseInt("20" + parts[2]);
    return new Date(ano, mes, dia);
  } else if (tamanho === 7) {
    // mm/yyyy
    const mes = Number.parseInt(parts[0]) - 1;
    const ano = Number.parseInt(parts[1]);
    return new Date(ano, mes);
  } else {
    // mm/yy
    const mes = Number.parseInt(parts[0]) - 1;
    const ano = Number.parseInt("20" + parts[1]);
    return new Date(ano, mes);
  }
}
