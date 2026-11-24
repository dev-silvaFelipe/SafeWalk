# UFU-FACOM

# Documento de Requisitos

# SafeWalk

**Versão 1.1 - novembro de 2026**

---

## Ficha Técnica

### Equipe Responsável pela Elaboração

- Felipe Rafael - Desenvolvimento mobile e integração
- João Vitor Revoredo - Desenvolvimento mobile e dados
- Pamela Beatriz - Interface e documentação

### Público-Alvo

Este documento destina-se à equipe de desenvolvimento, à equipe de avaliação acadêmica, aos usuários testadores e aos responsáveis pela homologação do aplicativo SafeWalk.

### Contato Institucional

- **Instituição:** Instituto Federal de Educação, Ciência e Tecnologia de Pernambuco (IFPE)
- **Curso:** Superior de Tecnologia em Análise e Desenvolvimento de Sistemas (ADS)

---

# Sumário

1. **Introdução**
   - 1.1 Visão geral deste documento
   - 1.2 Convenções, termos e abreviações
   - 1.3 Identificação dos requisitos
   - 1.4 Prioridades dos requisitos
   - 1.5 Referências
2. **Capítulo 1 - Descrição Geral do Sistema**
   - 2.1 Propósito do sistema
   - 2.2 Arquitetura geral
   - 2.3 Abrangência e sistemas relacionados
   - 2.4 Descrição dos usuários
3. **Capítulo 2 - Requisitos Funcionais**
   - 3.1 Autenticação e sessão
   - 3.2 Gestão de alertas
   - 3.3 Visualização geográfica
4. **Capítulo 3 - Requisitos Não Funcionais**
   - 4.1 Usabilidade
   - 4.2 Confiabilidade
   - 4.3 Desempenho
   - 4.4 Segurança
   - 4.5 Distribuição
   - 4.6 Padrões
   - 4.7 Hardware e software
5. **Capítulo 4 - Descrição da Interface com o Usuário**
   - 5.1 Interface de login
   - 5.2 Interface de cadastro
   - 5.3 Interface do feed
   - 5.4 Interface de criação de alerta
   - 5.5 Interface de detalhes
   - 5.6 Interface do mapa
6. **Rastreabilidade e Lacunas Identificadas**

---

# Introdução

Este documento especifica o aplicativo SafeWalk, fornecendo à equipe de desenvolvimento e à equipe de testes os requisitos necessários para implementação, avaliação e homologação da solução.

## 1.1 Visão geral deste documento

O documento está organizado da seguinte forma:

- **Seção 2 - Descrição geral do sistema:** apresenta propósito, arquitetura, escopo, sistemas relacionados e perfis de usuários.
- **Seção 3 - Requisitos funcionais:** apresenta casos de uso com atores, prioridades, entradas, pré-condições, saídas, pós-condições e fluxos.
- **Seção 4 - Requisitos não funcionais:** apresenta restrições de usabilidade, confiabilidade, desempenho, segurança, distribuição, padrões e ambiente técnico.
- **Seção 5 - Descrição da interface:** relaciona os requisitos às telas do aplicativo.

## 1.2 Convenções, termos e abreviações

- **APK:** pacote de instalação do Android.
- **GPS:** sistema de posicionamento global.
- **MVVM:** Model-View-ViewModel.
- **Room:** biblioteca de persistência local do Android baseada em SQLite.
- **OSM:** OpenStreetMap.
- **Prefeitura:** perfil institucional identificado pela entidade `isPrefeitura = true`.
- **Alerta:** registro de problema urbano com título, descrição, categoria, coordenadas, status e confirmação.
- **Toast:** mensagem breve exibida na interface.
- **Bottom sheet:** painel modal inferior usado para apresentar detalhes.

## 1.3 Identificação dos requisitos

- Requisitos funcionais: `[RFxxx]`.
- Requisitos não funcionais: `[NFxxx]`.
- Interfaces: `[IUxxx]`.
- Fluxos secundários: `FSxx`.

Os identificadores não devem ser reutilizados após a publicação de uma versão.

## 1.4 Prioridades dos requisitos

- **Essencial:** sem o requisito o sistema não cumpre seu objetivo principal.
- **Importante:** o sistema pode operar, mas com operação não satisfatória ou incompleta.
- **Desejável:** melhoria que pode ser incluída em versões posteriores.

## 1.5 Referências

1. Android Developers. *Application Fundamentals, Architecture and Jetpack Navigation*. Google, 2026.
2. Android Developers. *Room Persistence Library*. Google, 2026.
3. OpenStreetMap Foundation. *Contribuições cartográficas*. 2026.
4. CARTO. *Mapas de blocos basemaps utilizados pelo osmdroid*. 2026.
5. Material Design 3. *Componentes e diretrizes de interface*. Google, 2026.
6. Repositório oficial do projeto SafeWalk: `https://github.com/dev-silvaFelipe/SafeWalk`.

---

# CAPÍTULO 1 - Descrição Geral do Sistema

## 2.1 Propósito do sistema

O SafeWalk é um aplicativo Android para apoio à segurança comunitária e à comunicação de problemas de infraestrutura urbana. O usuário pode cadastrar uma conta, autenticar-se, registrar ocorrências com coordenadas geográficas, consultar alertas ativos, confirmar a persistência de um problema e visualizar ocorrências em uma lista ou em um mapa.

O sistema busca transformar registros individuais em informações coletivas que auxiliem moradores e pedestres na avaliação de riscos e apoiem representantes da prefeitura na identificação e resolução de chamados urbanos.

## 2.2 Arquitetura geral

O aplicativo adota uma arquitetura **MVVM** com uma única `MainActivity` e navegação baseada em Fragments.

- **Camada de interface:** Activities e Fragments.
- **Camada de apresentação:** `ViewModel` e `StateFlow`.
- **Camada de dados:** Repositories e DAOs.
- **Persistência:** Room sobre SQLite.
- **Localização:** `FusedLocationProviderClient`.
- **Mapas:** osmdroid com mapa base CARTO Voyager e atribuição ao OpenStreetMap.
- **Concorrência:** Kotlin Coroutines.

```text
Fragments / Views
       |
ViewModels
       |
Repositories
       |
Room/SQLite  +  Serviços locais de localização e mapas
```

## 2.3 Abrangência e sistemas relacionados

### Escopo positivo

- Cadastro e login de usuários.
- Identificação de perfil cidadão ou prefeitura.
- Persistência local de usuários, alertas e confirmações.
- Registro de alertas com título, descrição, categoria, coordenadas, data e status.
- Consulta de alertas ativos.
- Visualização dos dados em feed e mapa.
- Confirmação comunitária de alertas.
- Encerramento de alertas por perfil de prefeitura.
- Logout e restrição de navegação a usuários autenticados.

### Escopo negativo

- Não realiza cadastro em serviço de nuvem.
- Não possui backend compartilhado entre diferentes dispositivos.
- Não sincroniza dados com órgãos públicos ou sistemas externos.
- Não realiza chamadas de emergência, despacho de viaturas ou envio de notificações.
- Não mantém rastreamento contínuo da localização em segundo plano.
- Não garante login seguro contra ataques externos no estado atual, pois as credenciais são armazenadas localmente.
- Não realiza autenticação biométrica ou recuperação de senha.

O sistema se relaciona com serviços de tiles cartográficos do CARTO/OpenStreetMap e com os provedores de localização do Android. A persistência dos dados ocorre exclusivamente no dispositivo.

## 2.4 Descrição dos usuários

### 2.4.1 Cidadão

Usuário que cria uma conta, faz login e utiliza o aplicativo para registrar, consultar e confirmar problemas urbanos. O cidadão possui acesso aos alertas ativos criados por ele no feed.

**Principais dificuldades:** não conhecer riscos próximos, localizar problemas que precisam de manutenção e verificar se uma ocorrência ainda persiste.

### 2.4.2 Prefeitura

Usuário institucional identificado como perfil oficial. Consulta todos os alertas ativos, confirma chamadas e pode alterar o status de um alerta para `RESOLVIDO`.

**Principais dificuldades:** priorizar chamados urbanos e acompanhar registros da cidade.

### 2.4.3 Usuário não autenticado

Pessoa que ainda não fez login ou cujo cadastro não existe. O usuário pode acessar apenas as telas de login e cadastro e não pode operar o feed, o mapa ou a criação de alertas.

---

# CAPÍTULO 2 - Requisitos Funcionais

## 3.1 Autenticação e sessão

### [RF001] Cadastro de usuário

- **Ator:** Cidadão não autenticado.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU002]`.
- **Entradas:** nome, telefone e senha.
- **Pré-condições:** o usuário está na tela de cadastro.
- **Saídas:** confirmação de cadastro e retorno à tela de login.
- **Pós-condição:** um registro é inserido na tabela `usuarios` com perfil cidadão.

#### Fluxo principal

1. O usuário seleciona “Cadastre-se”.
2. Informa nome, telefone e senha.
3. Seleciona “Cadastrar”.
4. O sistema cria o usuário no banco Room.
5. O sistema informa o sucesso e retorna à tela de login.

#### Fluxos secundários

- **FS01 - Campos vazios:** o cadastro não é concluído e o usuário recebe a mensagem correspondente.
- **FS02 - Retorno à aplicação:** o sistema pode encerrar o cadastro sem persistir dados.

### [RF002] Login de usuário

- **Ator:** Cidadão cadastrado.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU001]`.
- **Entradas:** telefone e senha.
- **Pré-condições:** cadastro existente no banco local.
- **Saídas:** navegação para o feed e atualização da sessão.
- **Pós-condição:** `SessionManager.usuarioLogadoId`, `usuarioLogadoNome` e `isPrefeitura` são atualizados.

#### Fluxo principal

1. O usuário informa telefone e senha.
2. Seleciona “Entrar”.
3. O sistema compara os dados com o registro local.
4. Em caso válido, salva o usuário na sessão.
5. O sistema navega para o feed.

#### Fluxo secundário

- **FS01 - Credenciais inválidas:** o sistema exibe “Telefone ou senha inválidos” e permanece no login.
- **FS02 - Campos vazios:** o sistema solicita o preenchimento de telefone e senha.

### [RF003] Login de prefeitura

- **Ator:** Representante de prefeitura.
- **Prioridade:** Importante.
- **Interface associada:** `[IU001]`.
- **Entradas:** seleção do botão de acesso à prefeitura.
- **Pré-condições:** banco local disponível.
- **Saídas:** sessão configurada como prefeitura.
- **Pós-condição:** o usuário oficial existente é localizado ou criado localmente.

#### Fluxo principal

1. O usuário seleciona “Prefeitura”.
2. O sistema obtém ou cria o usuário oficial com telefone `0800156`.
3. A sessão é marcada como prefeitura.
4. O sistema abre o feed com todos os alertas ativos.

### [RF004] Logout

- **Ator:** Usuário autenticado.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU003]`.
- **Entradas:** acionamento do botão Sair.
- **Pré-condições:** sessão ativa.
- **Saídas:** retorno à tela de login.
- **Pós-condição:** todos os dados da sessão em memória são limpos.

## 3.2 Gestão de alertas

### [RF005] Registrar alerta

- **Ator:** Cidadão ou prefeitura autenticados.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU004]`.
- **Entradas:** título/problema selecionado, descrição opcional e coordenadas obtidas pelo dispositivo.
- **Pré-condições:** usuário autenticado e localização disponível ou obtida pelo sistema.
- **Saídas:** alerta persistido e retorno ao feed.
- **Pós-condição:** novo registro criado em `alertas` com status `ATIVO`, uma confirmação inicial e data/hora atual.

#### Fluxo principal

1. O usuário seleciona o botão de novo alerta no feed.
2. O sistema solicita as permissões de localização, quando necessárias.
3. O usuário seleciona ou informa o problema e pode adicionar detalhes.
4. O usuário seleciona “Publicar Chamado”.
5. O sistema obtém latitude e longitude e salva o alerta.
6. O sistema informa o sucesso e retorna ao feed.

#### Fluxos secundários

- **FS01 - Permissão negada:** o sistema não obtém coordenadas e impede o registro.
- **FS02 - Localização ainda indisponível:** o sistema informa que está aguardando coordenadas.
- **FS03 - Título vazio:** o sistema impede o registro.
- **FS04 - Descrição vazia:** o sistema grava “Sem observações adicionais.”
- **FS05 - Localização nula em emulador:** o sistema utiliza a posição padrão de Recife definida pelo aplicativo.

### [RF006] Consultar feed de alertas

- **Ator:** Usuário autenticado.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU003]`.
- **Entradas:** nenhuma entrada direta.
- **Pré-condições:** sessão ativa.
- **Saídas:** lista de alertas ativos ordenada da ocorrência mais recente para a mais antiga.
- **Pós-condição:** os cartões são atualizados automaticamente em caso de alteração no banco.

#### Regra por perfil

- Cidadão: consulta somente os alertas ativos criados pelo próprio usuário.
- Prefeitura: consulta todos os alertas ativos da cidade armazenados no dispositivo.

### [RF007] Visualizar detalhes do alerta

- **Ator:** Usuário autenticado.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU005]`.
- **Entradas:** toque em cartão do feed ou marcador do mapa.
- **Pré-condições:** alerta ativo disponível.
- **Saídas:** título, descrição, categoria, quantidade de confirmações e status.
- **Pós-condição:** o estado do alerta não é alterado ao apenas visualizar.

### [RF008] Confirmar alerta

- **Ator:** Cidadão ou prefeitura autenticados.
- **Prioridade:** Importante.
- **Interface associada:** `[IU005]`.
- **Entradas:** seleção do botão de confirmação.
- **Pré-condições:** alerta existente e usuário autenticado.
- **Saídas:** incremento visual da quantidade de confirmações.
- **Pós-condição:** o contador `confirmacoes` do alerta é incrementado.

**Observação atual de implementação:** a entidade `ConfirmacaoAlerta` possui chave única composta, mas o fluxo ativo não utiliza essa entidade nem uma verificação por usuário antes do incremento; portanto, confirmações sucessivas pelo mesmo usuário ainda podem incrementar o contador. A prevenção efetiva de duplicidade deve ser implementada antes de considerar este requisito plenamente atendido.

### [RF009] Encerrar alerta

- **Ator:** Prefeitura autenticada.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU005]`.
- **Entradas:** seleção de “Oficializar como Resolvido”.
- **Pré-condições:** perfil prefeitura e alerta ativo.
- **Saídas:** mensagem de resolução e remoção do alerta da consulta ativa.
- **Pós-condição:** status do alerta alterado para `RESOLVIDO`.

## 3.3 Visualização geográfica

### [RF010] Visualizar alertas no mapa

- **Ator:** Usuário autenticado.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU006]`.
- **Entradas:** botão “Mapa”.
- **Pré-condições:** conexão disponível para carregar os tiles cartográficos e banco com alertas.
- **Saídas:** mapa interativo com marcadores.
- **Pós-condição:** o mapa mostra os alertas ativos observados pelo `MapViewModel`.

#### Fluxo principal

1. O usuário seleciona “Mapa”.
2. O sistema inicializa o osmdroid com o mapa base CARTO Voyager.
3. O sistema observa os alertas ativos.
4. Cada alerta é representado por um marcador na latitude e longitude cadastradas.
5. Ao tocar no marcador, o sistema abre o painel de detalhes.

### [RF011] Obter localização atual

- **Ator:** Sistema e usuário.
- **Prioridade:** Essencial.
- **Interface associada:** `[IU004]` e `[IU006]`.
- **Entradas:** permissão do usuário e sensores de localização.
- **Pré-condições:** `ACCESS_FINE_LOCATION` ou `ACCESS_COARSE_LOCATION` disponível.
- **Saídas:** latitude e longitude.
- **Pós-condição:** a criação usa as coordenadas atuais; o mapa centraliza e exibe a posição do usuário.

#### Fluxos secundários

- **FS01 - Permissão negada:** localização não é ativada.
- **FS02 - Localização indisponível:** o sistema informa o estado ao usuário ou usa o fallback previsto no fluxo de criação.

---

# CAPÍTULO 3 - Requisitos Não Funcionais

## 4.1 Usabilidade

### [NF001] Interface intuitiva

- **Prioridade:** Essencial.
- **Descrição:** os elementos devem possuir rótulos claros, contraste adequado, componentes Material 3, área de toque apropriada e navegação reconhecível.
- **Caso(s) de uso associado(s):** todos.

### [NF002] Feedback do usuário

- **Prioridade:** Essencial.
- **Descrição:** ações concluídas ou inválidas devem apresentar feedback por Toast ou alteração imediata da interface.
- **Caso(s) de uso associado(s):** `[RF001]` a `[RF011]`.

## 4.2 Confiabilidade

### [NF003] Persistência local

- **Prioridade:** Essencial.
- **Descrição:** usuários e alertas devem permanecer persistidos no SQLite por meio do Room até serem removidos ou o banco ser desinstalado/limpo.

### [NF004] Integridade das confirmações

- **Prioridade:** Essencial.
- **Descrição:** a regra de negócio deve impedir que o mesmo usuário confirme o mesmo alerta mais de uma vez.
- **Estado:** requisito parcialmente implementado. Existe chave única composta na entidade `ConfirmacaoAlerta`, mas não há persistência dessa confirmação no fluxo atual.

## 4.3 Desempenho

### [NF005] Atualização reativa

- **Prioridade:** Importante.
- **Descrição:** alterações em usuários e alertas devem ser refletidas por Fluxos do Room e `StateFlow` sem atualizações manualmente recorrentes da tela.

### [NF006] Consumo de localização

- **Prioridade:** Importante.
- **Descrição:** a localização deve ser solicitada sob demanda na criação do alerta ou na abertura do mapa. O aplicativo não deve manter rastreamento em segundo plano.

## 4.4 Segurança

### [NF007] Proteção de credenciais

- **Prioridade:** Essencial.
- **Descrição:** o sistema deve evitar que a senha seja exibida em texto claro na interface. Deve armazenar credenciais com derivação segura e não de forma reversível.
- **Estado:** não atendido. Atualmente o campo `senhaHash` recebe e compara diretamente a senha digitada.

### [NF008] Controle de acesso por perfil

- **Prioridade:** Essencial.
- **Descrição:** apenas a prefeitura deve visualizar e ativar o controle de resolução; telas internas não podem ser abertas sem sessão ativa.
- **Estado:** restrição atual implementada em memória pelo `SessionManager` e pelo flag `isPrefeitura`.

### [NF009] Permissões Android

- **Prioridade:** Essencial.
- **Descrição:** o aplicativo deve solicitar localização apenas quando necessário e continuar operacional quando a permissão não for concedida.

## 4.5 Distribuição

### [NF010] Pacote Android

- **Prioridade:** Essencial.
- **Descrição:** o sistema deve poder ser instalado como aplicativo Android em dispositivo ou emulador compatível.

## 4.6 Padrões

### [NF011] Arquitetura MVVM

- **Prioridade:** Essencial.
- **Descrição:** o código deve separar interface, apresentação e acesso a dados conforme os padrões da arquitetura MVVM.

### [NF012] Navegação e componentes

- **Prioridade:** Importante.
- **Descrição:** a navegação deve utilizar Android Jetpack Navigation e os componentes visuais devem seguir Material Design.

## 4.7 Hardware e software

### [NF013] Plataforma mínima

- **Prioridade:** Essencial.
- **Descrição:** executar em Android com `minSdk` 24 e `targetSdk` 36.

### [NF014] Recursos do dispositivo

- **Prioridade:** Essencial.
- **Descrição:** exige conexão de dados para baixar o mapa, armazenamento disponível para Room e cache dos tiles e localização por GPS/rede quando disponível.

---

# CAPÍTULO 4 - Descrição da Interface com o Usuário

## 5.1 [IU001] Tela de Login

Apresenta campos de telefone e senha, botão “Entrar”, botão de cadastro e acesso rápido “Prefeitura”.

### Críticas da interface

- Os campos devem ser associados a teclado adequado.
- Senhas devem ser ocultadas.
- Erros de autenticação não devem revelar se o telefone ou a senha está incorreto separadamente.

## 5.2 [IU002] Tela de Cadastro

Apresenta campos de nome, telefone e senha e botão “Cadastrar”.

## 5.3 [IU003] Tela do Feed

Apresenta cabeçalho com perfil, botão de logout, lista de cards de alertas ativos, botão flutuante de criação e navegação para o mapa.

### Regras visuais

- Cidadão exibe alertas criados pelo usuário.
- Prefeitura exibe todos os alertas ativos e identifica o perfil no cabeçalho.
- Alertas `RESOLVIDO` não aparecem no feed ativo.

## 5.4 [IU004] Tela de Criação de Alerta

Apresenta seleção padronizada de problemas, campo de detalhes e botão “Publicar Chamado”.

### Categorias visíveis

Iluminação, asfalto, esgoto, vazamento e outros problemas de infraestrutura. Internamente, o aplicativo converte essas opções em `TipoAlerta`.

## 5.5 [IU005] Painel de Detalhes

Apresenta título, descrição, tipo, confirmações e status. Para a prefeitura, também apresenta o botão de resolução.

## 5.6 [IU006] Mapa

Exibe mapa baseado em tiles CARTO Voyager, controle multitouch, posição do usuário e marcadores coloridos por tipo de alerta.

### Cores de marcadores

- Vermelho: asfalto.
- Amarelo: iluminação.
- Laranja: esgoto.
- Roxo: vazamento.
- Ciano: outro problema.

---

# Rastreabilidade e Lacunas Identificadas

| Requisito | Escopo do sistema | Estado atual |
|---|---|---|
| RF001-RF004 | Cadastro, login, prefeitura e logout | Implementado localmente |
| RF005-RF007 | Criação, consulta e detalhes de alertas | Implementado |
| RF008 | Confirmação comunitária | Implementado apenas como incremento, sem persistência por usuário |
| RF009 | Encerramento pela prefeitura | Implementado |
| RF010-RF011 | Mapa e localização | Implementado com osmdroid e Play Services |
| NF004 | Prevenção de confirmações duplicadas | Parcial |
| NF007 | Hash seguro de senha | Não implementado |
| NF008 | Controle de sessão/perfil | Implementado em memória; não equivale a segurança de backend |
| NF010-NF014 | Distribuição, padrões e ambiente | Implementado ou especificado |

O documento cobre o escopo implementado e explicita os pontos que ainda não possuem conformidade completa.
