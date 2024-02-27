/* eslint-disable no-undef */
import stringParaData from "../../../../../../src/screens/Produtos/pages/Adicionar/functions/data";

test("data com o separador /", () => {
  const data = "26/02/2024";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 26));
});

test("data com o separador -", () => {
  const data = "26-02-2024";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 26));
});

test("data com o separador ' '", () => {
  const data = "26 02 2024";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 26));
});

test("data sem nenhum separador", () => {
  const data = "26022024";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 26));
});

test("data no formato dd/mm/yyyy", () => {
  const data = "26/02/2024";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 26));
});

test("data no formato dd/mm/yy", () => {
  const data = "26/02/24";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 26));
});

test("data no formato mm/yyyy", () => {
  const data = "02/2024";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 1));
});

test("data no formato mm/yy", () => {
  const data = "02/24";
  const dataFormatada = stringParaData(data);
  expect(dataFormatada).toEqual(new Date(2024, 1, 1));
});
