# QualiWatch, um app de Controle de Validade

## Descrição

Bem-vindo ao QualiWatch! Esta aplicação foi desenvolvida usando o framework Expo para facilitar o controle de validade de produtos. A principal funcionalidade é a capacidade de extrair texto de imagens, permitindo a rápida adição de produtos ao sistema.

Além disso, o aplicativo oferece uma página de alertas para fornecer informações sobre produtos que estão prestes a vencer. Isso ajuda os usuários a gerenciar eficientemente seus estoques e garantir a qualidade dos produtos.

## Tecnologias Utilizadas

- **Expo:** O aplicativo foi desenvolvido utilizando o Expo, que simplifica o processo de construção e implantação de aplicativos React Native.
- **React Native:** A estrutura de desenvolvimento de aplicativos móveis que permite a criação de aplicativos nativos usando React.
- **OCR (Optical Character Recognition):**  funcionalidade de extração de texto de imagens é realizada por meio de OCR para automatizar a entrada de dados.

## Funcionalidades

### 1. Adição Rápida de Produtos

- **Captura de Imagem:** Use a câmera do dispositivo para capturar imagens de rótulos de produtos.

- **Extração de Texto:** O aplicativo utiliza OCR para extrair automaticamente informações relevantes dos rótulos das imagens.

- **Formulário de Adição:** As informações extraídas são preenchidas automaticamente em um formulário, facilitando a adição rápida de novos produtos ao sistema.

### 2. Página de Alertas

- **Alertas de Validade:** Visualize uma lista de produtos que estão prestes a vencer.

- **Notificações:** Receba notificações para produtos que exigem atenção imediata em termos de validade.

- **Detalhes do Produto:** Acesse informações detalhadas sobre cada produto na lista de alertas.

## Como Iniciar

Certifique-se de ter o Expo instalado. Se não tiver, você pode instalá-lo globalmente usando:

```bash
npm install -g expo-cli
```

Clone o repositório do GitHub:

```bash
git clone https://github.com/BotAldaris/QualiWatch.git
```
Em seguida, navegue até o diretório do projeto:

```bash
cd QualiWatch
```

Instale as dependências do projeto:

```bash
npm install
```

Para iniciar o aplicativo, use o comando:

```bash
expo start
```

Isso abrirá a interface do Expo no navegador. Você pode optar por executar o aplicativo em um emulador ou digitalizar o código QR com o aplicativo Expo Go em seu dispositivo físico, Certifique-se também de que o QualiWatchApi esteja em execução para adicionar à URL. Se estiver hospedando localmente, utilize o ngrok, por exemplo, para gerar um link utilizável, pois o Expo geralmente não funciona com o localhost.
