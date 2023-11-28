export default function stringParaData(dataString: string) {
  let ano = 2000;
  let mes = 1;
  let dia = 1;
  const apenasNumeros = dataString.replace(/\D/g, "");

  // Analisar a string com base no comprimento
  if (apenasNumeros.length === 8) {
    ano = parseInt(apenasNumeros.substring(4, 8), 10);
    mes = parseInt(apenasNumeros.substring(2, 4), 10);
    dia = parseInt(apenasNumeros.substring(0, 2), 10);
  } else if (
    apenasNumeros.length === 6 &&
    apenasNumeros.substring(2, 6) < "2000"
  ) {
    ano = 2000 + parseInt(apenasNumeros.substring(4, 6), 10);
    mes = parseInt(apenasNumeros.substring(2, 4), 10);
    dia = parseInt(apenasNumeros.substring(0, 2), 10);
  } else if (apenasNumeros.length === 4) {
    ano = 2000 + parseInt(apenasNumeros.substring(2, 4), 10);
    mes = parseInt(apenasNumeros.substring(0, 2), 10);
  } else if (apenasNumeros.length === 6) {
    ano = parseInt(apenasNumeros.substring(2, 6), 10);
    mes = parseInt(apenasNumeros.substring(0, 2), 10);
  } else {
    // Retornar null para formatos inválidos
    return null;
  }

  // Criar objeto de data
  const data = new Date(ano, mes - 1, dia);

  // Verificar se a data é válida
  if (
    isNaN(data.getFullYear()) ||
    isNaN(data.getMonth()) ||
    isNaN(data.getDate())
  ) {
    return null;
  }

  return data;
}
