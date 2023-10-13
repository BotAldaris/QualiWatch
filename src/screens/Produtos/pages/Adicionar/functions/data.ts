export default function stringParaData(data: string): Date {
  const tamanho: number = data.length;
  //dd / mm / yyyy
  //01 2 34 5 6789
  if (tamanho == 10) {
    return new Date(
      Number.parseInt(data.substring(6)),
      Number.parseInt(data.substring(3, 5)),
      Number.parseInt(data.substring(0, 2))
    );
  }
  //dd / mm / yy
  else if (tamanho == 8) {
    return new Date(
      Number.parseInt("20" + data.substring(6)),
      Number.parseInt(data.substring(3, 5)),
      Number.parseInt(data.substring(0, 2))
    );
  }
  //mm / yyyy
  else if (tamanho == 7) {
    return new Date(
      Number.parseInt(data.substring(3)),
      Number.parseInt(data.substring(0, 2))
    );
  }
  //mm / yy
  else {
    return new Date(
      Number.parseInt("20" + data.substring(3)),
      Number.parseInt(data.substring(0, 2))
    );
  }
}
