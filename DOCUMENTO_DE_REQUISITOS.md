# INSTITUTO FEDERAL DE PERNAMBUCO - IFPE
## ANÁLISE E DESENVOLVIMENTO DE SISTEMAS (ADS)
### DOCUMENTO DE REQUISITOS DE SOFTWARE

---

# SafeWalk — Plataforma Colaborativa de Segurança Urbana

**Versão 1.0** — Novembro de 2026

---

## FICHA TÉCNICA

### Equipe Responsável pela Elaboração
* **Felipe Rafael** — Análise e Desenvolvimento de Sistemas (ADS) / IFPE
* **João Vitor Revoredo** — Análise e Desenvolvimento de Sistemas (ADS) / IFPE
* **Pamela Beatriz** — Análise e Desenvolvimento de Sistemas (ADS) / IFPE

### Público Alvo
Este documento destina-se aos professores avaliadores da disciplina, desenvolvedores e equipe técnica responsáveis pelo projeto, implementação, testes e homologação do aplicativo móvel **SafeWalk**.

### Identificação Institucional
* **Instituição:** Instituto Federal de Educação, Ciência e Tecnologia de Pernambuco (IFPE)
* **Curso:** Superior de Tecnologia em Análise e Desenvolvimento de Sistemas (ADS)

*Aviso legal:* Todas as marcas e nomes de produtos (Android, Google Maps, SQLite, Kotlin) pertencem aos seus respectivos proprietários.

---

## SUMÁRIO

1. **INTRODUÇÃO**
   * 1.1 Visão Geral do Documento
   * 1.2 Convenções, Termos e Abreviações
     * 1.2.1 Identificação dos Requisitos
     * 1.2.2 Prioridades dos Requisitos
   * 1.3 Referências

2. **CAPÍTULO 1 - DESCRIÇÃO GERAL DO SISTEMA**
   * 2.1 Propósito e Justificativa do Sistema
   * 2.2 Arquitetura Geral do Sistema
   * 2.3 Abrangência e Sistemas Relacionados (Escopo Positivo e Negativo)
   * 2.4 Descrição dos Usuários
     * 2.4.1 Cidadão / Pedestre (Usuário Geral)
     * 2.4.2 Colaborador Ativo (Sentinela Comunitário)
     * 2.4.3 Administrador / Auditor

3. **CAPÍTULO 2 - REQUISITOS FUNCIONAIS (CASOS DE USO)**
   * 3.1 Módulo 1: Autenticação e Gestão de Usuários
     * [RF001] Cadastro de Novo Usuário
     * [RF002] Autenticação de Usuário (Login)
     * [RF003] Encerramento de Sessão (Logout)
   * 3.2 Módulo 2: Gestão e Alimentação de Alertas de Risco
     * [RF004] Registrar Novo Alerta de Ocorrência / Infraestrutura
     * [RF005] Listar Alertas Registrados (Feed Principal)
     * [RF006] Visualizar Detalhes do Alerta
     * [RF007] Validar / Confirmar Alerta Comunitário
   * 3.3 Módulo 3: Sensoriamento e Geolocalização Interativa
     * [RF008] Visualizar Alertas no Mapa Interativo
     * [RF009] Obter Localização Geográfica Atual via GPS

4. **CAPÍTULO 3 - REQUISITOS NÃO FUNCIONAIS**
   * 4.1 Usabilidade
     * [NF001] Interface Acessível e Intuitiva
     * [NF002] Design Responsivo e Feedbacks Visuais
   * 4.2 Confiabilidade
     * [NF003] Persistência Local e Tolerância a Falhas
     * [NF004] Prevenção de Confirmações Duplicadas
   * 4.3 Desempenho
     * [NF005] Tempo de Resposta e Atualização Reativa
     * [NF006] Otimização de Bateria e Consumo de Localização
   * 4.4 Segurança
     * [NF007] Proteção e Hashing de Credenciais
     * [NF008] Isolamento de Sessão do Usuário
   * 4.5 Distribuição
     * [NF009] Empacotamento Autônomo para Plataforma Android
   * 4.6 Padrões e Boas Práticas
     * [NF010] Diretrizes Google Material Design 3 e Padrão MVVM
   * 4.7 Hardware e Software
     * [NF011] Requisitos de Ambiente de Execução e Sensores

5. **CAPÍTULO 4 - DESCRIÇÃO DA INTERFACE COM O USUÁRIO**
   * 5.1 [IU001] Tela de Login (`LoginFragment`)
   * 5.2 [IU002] Tela de Cadastro (`CadastroFragment`)
   * 5.3 [IU003] Tela Inicial / Feed de Alertas (`FeedAlertaFragment`)
   * 5.4 [IU004] Tela de Registro de Alerta (`CriarAlertaFragment`)
   * 5.5 [IU005] Painel Modal de Detalhes do Alerta (`DetalheAlertaBottomSheet`)
   * 5.6 [IU006] Tela de Mapa Interativo (`MapsFragment`)

---

# INTRODUÇÃO

Este documento especifica o sistema de software móvel **SafeWalk**, fornecendo à equipe de engenharia e aos avaliadores todas as informações necessárias para o projeto, arquitetura, implementação, testes e homologação da solução.

### 1.1 Visão Geral deste Documento
Esta introdução estabelece os objetivos e convenções adotadas na especificação. O restante do documento está estruturado conforme as seções a seguir:
* **Capítulo 1 – Descrição Geral do Sistema:** Contextualiza o propósito do SafeWalk, seus limites de atuação, modelo arquitetural e perfis de usuários.
* **Capítulo 2 – Requisitos Funcionais (Casos de Uso):** Especifica detalhadamente as funções do sistema organizadas por subseções modulares, com atores, pré-condições, pós-condições, fluxo principal e alternativos.
* **Capítulo 3 – Requisitos Não Funcionais:** Apresenta restrições de usabilidade, confiabilidade, desempenho, segurança, distribuição, conformidade com padrões e requisitos de hardware/software.
* **Capítulo 4 – Descrição da Interface com o Usuário:** Documenta o fluxo de navegação, telas, componentes e regras de validação visual.

### 1.2 Convenções, Termos e Abreviações
A interpretação das diretrizes deste documento obedece às convenções formais estabelecidas a seguir:

#### 1.2.1 Identificação dos Requisitos
A identificação segue a notação unificada:
* `[Subseção.RFxxx]` para Requisitos Funcionais (ex.: `[Módulo Alertas.RF004]`).
* `[Subseção.NFxxx]` para Requisitos Não Funcionais (ex.: `[Confiabilidade.NF003]`).
* `[IUxxx]` para Interfaces de Usuário (ex.: `[IU003]`).

#### 1.2.2 Prioridades dos Requisitos
* **Essencial:** Requisito nuclear sem o qual o sistema não entra em operação e não atende aos objetivos fundamentais.
* **Importante:** Requisito necessário para a operação plena e satisfatória, cuja ausência degrada a experiência do usuário, mas não inviabiliza o fluxo principal.
* **Desejável:** Requisito complementar ou de refinamento que pode ser diferido para iterações futuras sem prejudicar o funcionamento básico.

### 1.3 Referências
1. Android Developers Documentation: Architecture Guides & Modern Android Development (MAD). Google, 2026.
2. Material Design 3 Guidelines: Color, Typography, and Elevation Systems. Google, 2026.
3. Sommerville, Ian. *Engenharia de Software*. 10ª Edição, Pearson, 2019.
4. Pressman, Roger S.; Maxim, Bruce R. *Engenharia de Software: Uma Abordagem Profissional*. 8ª Edição, McGraw-Hill, 2016.

---

# CAPÍTULO 1 - DESCRIÇÃO GERAL DO SISTEMA

### 2.1 Propósito e Justificativa do Sistema
O **SafeWalk** é uma plataforma móvel Android de **segurança comunitária colaborativa** que capacita cidadãos a reportar, monitorar e validar incidentes de risco e problemas graves de infraestrutura urbana em tempo real (como vias com iluminação pública inoperante, assaltos recentes, obstáculos intransitáveis, alagamentos e áreas suspeitas). 

Diante da defasagem na comunicação entre os cidadãos e as autoridades sobre os riscos iminentes no espaço público, o SafeWalk transforma informações descentralizadas de cada pedestre em inteligência coletiva, permitindo que qualquer pessoa planeje trajetos mais seguros a pé e tome decisões preventivas.

### 2.2 Arquitetura Geral do Sistema
O aplicativo foi concebido sob o padrão arquitetural **MVVM (Model-View-ViewModel)** em conformidade com as recomendações de *Modern Android Development (MAD)*:
* **UI Layer (View):** Desenvolvida em arquitetura *Single-Activity* com `MainActivity` orquestrando Fragments via **Android Jetpack Navigation Component**.
* **State & Presentation Layer (ViewModel):** Gerencia o ciclo de vida dos dados, mantendo estados imutáveis e expondo fluxos reativos (*Kotlin Coroutines StateFlow*).
* **Data Layer (Repository & Local Database):** Camada de abstração centralizada com **Android Room Persistence Library** sobre banco de dados relacional **SQLite**, garantindo armazenamento local robusto e off-line.
* **Sensoriamento & Integração Externa:** **Google Play Services Location (FusedLocationProviderClient)** para obtenção precisa das coordenadas do usuário e **Google Maps SDK for Android** para renderização gráfica e espacial de marcadores interativos.

```
       +-------------------------------------------------+
       |           UI Layer (Fragments / Views)          |
       |  Login | Feed | Criar Alerta | Detalhes | Mapa  |
       +-------------------------------------------------+
                                | (Events / StateFlow)
                                v
       +-------------------------------------------------+
       |      ViewModel Layer (Alerta, Map, Usuario)     |
       +-------------------------------------------------+
                                |
                                v
       +-------------------------------------------------+
       |       Repository Layer (AlertaRepository)       |
       +-------------------------------------------------+
                 /                               \
                v                                 v
   +--------------------------+     +--------------------------+
   |   Room DB (SQLite Local) |     |  Hardware / GPS Sensors  |
   |  Usuarios / Alertas /    |     |  FusedLocationClient     |
   |  Confirmacoes            |     |  Google Maps SDK         |
   +--------------------------+     +--------------------------+
```

### 2.3 Abrangência e Sistemas Relacionados

#### 2.3.1 Escopo Positivo (O que o sistema faz):
* Gerencia o cadastro e login de usuários com identificação de sessão local.
* Coleta e armazena registros de alertas contendo título, descrição detalhada, coordenadas de latitude/longitude, categoria do incidente e carimbo de data/hora.
* Exibe a listagem cronológica atualizada dos alertas da região através de uma tela de Feed dinâmica.
* Apresenta os alertas espacialmente distribuídos em um mapa georreferenciado com marcadores temáticos.
* Oferece um mecanismo de validação comunitária onde pedestres podem atestar a veracidade de um perigo reportado, incrementando o índice de confiabilidade do alerta.
* Funciona de maneira autônoma com persistência de dados local no dispositivo.

#### 2.3.2 Escopo Negativo (O que o sistema não faz):
* O sistema **não** despacha viaturas policiais, ambulâncias ou equipes de resgate de emergência; para tais situações, o usuário deve acionar os canais telefônicos oficiais (190, 192, 193).
* O sistema **não** requer conexão contínua com provedor em nuvem para seu núcleo de banco de dados, utilizando o armazenamento relacional interno SQLite (Room) para demonstrar alta performance local e resiliência.

### 2.4 Descrição dos Usuários

#### 2.4.1 Cidadão / Pedestre (Usuário Geral)
* **Perfil:** Qualquer pessoa que transita pelas vias urbanas a pé ou por transporte público.
* **Objetivos:** Consultar a lista de alertas e verificar no mapa se seu percurso habitual possui pontos de assalto recente, trechos ermos ou falta de iluminação.
* **Dificuldades:** Falta de informações prévias sobre áreas de perigo iminente.

#### 2.4.2 Colaborador Ativo (Sentinela Comunitário)
* **Perfil:** Moradores de bairros, estudantes universitários e trabalhadores engajados na segurança local.
* **Objetivos:** Registrar novos alertas com sua localização geográfica imediata e validar alertas abertos por outros moradores para advertir a comunidade.
* **Dificuldades:** Burocracia em canais públicos de comunicação de perigos.

#### 2.4.3 Administrador / Auditor
* **Perfil:** Representantes comunitários ou técnicos de manutenção urbana que utilizam o feed e os dados de confirmação para priorizar demandas públicas.

---

# CAPÍTULO 2 - REQUISITOS FUNCIONAIS (CASOS DE USO)

## 3.1 Módulo 1: Autenticação e Gestão de Usuários

### [RF001] Cadastro de Novo Usuário
* **Ator:** Pedestre não cadastrado.
* **Prioridade:** Essencial.
* **Interfaces associadas:** [IU002] Tela de Cadastro (`CadastroFragment`).
* **Entradas e pré-condições:** 
  * Entradas: Nome completo, número de telefone com DDD e senha de acesso.
  * Pré-condição: O aplicativo deve estar instalado e o usuário deve acessar o formulário de cadastro.
* **Saídas e pós-condições:** 
  * Saídas: Mensagem de confirmação de cadastro e redirecionamento para a tela de autenticação.
  * Pós-condição: Registro do novo usuário inserido com sucesso na tabela `usuarios` do banco local Room.

#### Fluxo de eventos principal:
1. O usuário seleciona a opção "Cadastre-se" na tela de login.
2. O sistema apresenta o formulário de cadastro [IU002].
3. O usuário insere nome, telefone e senha, e aciona o botão "Cadastrar".
4. O sistema valida se todos os campos obrigatórios foram preenchidos.
5. O sistema registra o usuário no banco de dados local.
6. O sistema exibe mensagem de sucesso e redireciona o usuário para a tela de Login [IU001].

#### Fluxos secundários:
* **FS01 - Campos em branco:** Caso o usuário submeta o formulário com algum campo vazio, o sistema exibe mensagem de alerta no campo correspondente e impede a inserção no banco de dados.

---

### [RF002] Autenticação de Usuário (Login)
* **Ator:** Usuário previamente cadastrado.
* **Prioridade:** Essencial.
* **Interfaces associadas:** [IU001] Tela de Login (`LoginFragment`).
* **Entradas e pré-condições:**
  * Entradas: Número de telefone e senha.
  * Pré-condição: Existência de cadastro prévio.
* **Saídas e pós-condições:**
  * Saídas: Redirecionamento para a tela principal (Feed de Alertas [IU003]).
  * Pós-condição: `SessionManager` atualizado com o `id` do usuário autenticado.

#### Fluxo de eventos principal:
1. O usuário informa o número de telefone e senha cadastrados.
2. O usuário clica no botão "Entrar".
3. O sistema consulta as credenciais na tabela de usuários.
4. Ao verificar a correspondência exata, o sistema grava o identificador na sessão ativa (`SessionManager.usuarioLogadoId`).
5. O sistema redireciona a navegação para o Feed de Alertas [IU003].

#### Fluxos secundários:
* **FS01 - Credenciais inválidas:** Caso o telefone não exista ou a senha não coincida, o sistema exibe notificação via Toast ("Credenciais inválidas") e mantém o usuário na tela de login.

---

### [RF003] Encerramento de Sessão (Logout)
* **Ator:** Usuário logado.
* **Prioridade:** Importante.
* **Interfaces associadas:** [IU003] Tela de Feed (`FeedAlertaFragment`).
* **Entradas e pré-condições:**
  * Entradas: Clique no botão de logout no cabeçalho.
  * Pré-condição: Usuário com sessão ativa.
* **Saídas e pós-condições:**
  * Saídas: Navegação de retorno para a Tela de Login [IU001].
  * Pós-condição: Identificador de sessão apagado (`SessionManager.usuarioLogadoId = null`).

#### Fluxo de eventos principal:
1. O usuário aciona o ícone de logout no canto superior direito do Feed.
2. O sistema invalida o token de sessão local.
3. O sistema redireciona imediatamente para o fluxo inicial de login [IU001].

---

## 3.2 Módulo 2: Gestão e Alimentação de Alertas de Risco

### [RF004] Registrar Novo Alerta de Ocorrência / Infraestrutura
* **Ator:** Colaborador Ativo / Cidadão autenticado.
* **Prioridade:** Essencial.
* **Interfaces associadas:** [IU004] Tela de Cadastro de Alerta (`CriarAlertaFragment`).
* **Entradas e pré-condições:**
  * Entradas: Título do incidente, descrição circunstanciada, categoria (ex.: Iluminação Ruim, Assalto, Obstáculo, Outro).
  * Pré-condição: Usuário autenticado e permissão de GPS concedida.
* **Saídas e pós-condições:**
  * Saídas: Alerta gravado com sucesso, retorno automático ao Feed.
  * Pós-condição: Novo registro na tabela `alertas` contendo latitude, longitude e carimbo temporal.

#### Fluxo de eventos principal:
1. Na tela principal, o usuário clica no botão flutuante de criação (+).
2. O sistema requisita as coordenadas mais recentes do dispositivo ao sensor de geolocalização.
3. O usuário preenche o título e a descrição do incidente.
4. O usuário clica no botão "Salvar".
5. O sistema vincula as coordenadas obtidas, o ID do usuário da sessão e persiste a entidade `AlertaInfraestrutura`.
6. O sistema exibe notificação de confirmação e retorna à tela anterior.

#### Fluxos secundários:
* **FS01 - Indisponibilidade de coordenadas GPS:** Caso o sensor não forneça latitude/longitude válidas, o sistema emite alerta ("Não foi possível obter a localização") e impede a gravação até que o GPS estabilize.

---

### [RF005] Listar Alertas Registrados (Feed Principal)
* **Ator:** Cidadão autenticado.
* **Prioridade:** Essencial.
* **Interfaces associadas:** [IU003] Feed de Alertas (`FeedAlertaFragment`).
* **Entradas e pré-condições:**
  * Entradas: Nenhuma entrada direta (carregamento automático da base de dados).
  * Pré-condição: Usuário logado.
* **Saídas e pós-condições:**
  * Saídas: Lista ordenada de cards contendo título, descrição prévia e número de confirmações comunitárias.
  * Pós-condição: Exibição reativa atualizada via `StateFlow` e `RecyclerView`.

#### Fluxo de eventos principal:
1. O usuário acessa a aplicação ou retorna de uma ação.
2. O `AlertaViewModel` consulta o `AlertaRepository`.
3. O repositório emite um fluxo contínuo dos alertas cadastrados.
4. O `AlertaAdapter` renderiza os cartões dinamicamente no `RecyclerView`.

---

### [RF006] Visualizar Detalhes do Alerta
* **Ator:** Cidadão autenticado.
* **Prioridade:** Essencial.
* **Interfaces associadas:** [IU005] Modal de Detalhes (`DetalheAlertaBottomSheet`).
* **Entradas e pré-condições:**
  * Entradas: Toque em um cartão do feed de alertas ou no marcador do mapa.
  * Pré-condição: Lista de alertas carregada na interface.
* **Saídas e pós-condições:**
  * Saídas: Apresentação da tela/modal detalhada exibindo: Título completo, Descrição rica, Tipo classificado, Total acumulado de confirmações e Status do incidente (ATIVO / RESOLVIDO).
  * Pós-condição: Nenhuma alteração de estado no banco de dados.

#### Fluxo de eventos principal:
1. O usuário navega pelo Feed de Alertas [IU003] ou pelo Mapa [IU006].
2. O usuário toca no card ou no marcador correspondente.
3. O sistema abre o painel modal [IU005] recebendo o objeto serializado `AlertaInfraestrutura`.
4. Os dados completos são exibidos para leitura do usuário.
5. O usuário fecha o painel arrastando para baixo ou tocando fora da área modal.

---

### [RF007] Validar / Confirmar Alerta Comunitário
* **Ator:** Cidadão / Colaborador autenticado.
* **Prioridade:** Importante.
* **Interfaces associadas:** [IU005] Modal de Detalhes / Mapa (`AlertaDetalheBottomSheet`).
* **Entradas e pré-condições:**
  * Entradas: Clique no botão "Confirmar Alerta".
  * Pré-condição: Alerta ativo existente e usuário não ter confirmado o mesmo alerta anteriormente.
* **Saídas e pós-condições:**
  * Saídas: Atualização visual imediata do contador de confirmações.
  * Pós-condição: Registro inserido na tabela relacional `confirmacoes` e incremento no campo `confirmacoes` da tabela `alertas`.

#### Fluxo de eventos principal:
1. Ao inspecionar os detalhes do alerta, o usuário clica no botão "Confirmar Alerta".
2. O sistema registra a validação com a chave única composta `(alertaId, usuarioId)`.
3. O sistema incrementa a contagem de confirmações.
4. O painel modal fecha e os marcadores/cards refletem a nova confiabilidade.

---

## 3.3 Módulo 3: Sensoriamento e Geolocalização Interativa

### [RF008] Visualizar Alertas no Mapa Interativo
* **Ator:** Cidadão autenticado.
* **Prioridade:** Importante (Funcionalidade de Alto Valor).
* **Interfaces associadas:** [IU006] Mapa de Alertas (`MapsFragment`).
* **Entradas e pré-condições:**
  * Entradas: Ação do usuário ao tocar no botão "Ver Mapa".
  * Pré-condição: Chave da Google Maps API configurada e sinal de internet para download de azulejos cartográficos.
* **Saídas e pós-condições:**
  * Saídas: Mapa do Google renderizado com marcadores vermelhos em cada ponto de incidente.
  * Pós-condição: Câmera centralizada na posição atual ou nos incidentes.

#### Fluxo de eventos principal:
1. O usuário toca no botão "Ver Mapa" presente no Feed.
2. O sistema inicializa o `SupportMapFragment`.
3. O mapa carrega e consome a lista de alertas via `MapViewModel`.
4. Cada alerta gera um `Marker` na coordenada exata de latitude e longitude.
5. Ao tocar em qualquer marcador, o sistema abre o modal de detalhes do alerta.

---

### [RF009] Obter Localização Geográfica Atual via GPS
* **Ator:** Sistema operacional Android / Usuário.
* **Prioridade:** Essencial.
* **Interfaces associadas:** [IU004] e [IU006].
* **Entradas e pré-condições:**
  * Entradas: Sinal dos satélites GPS / Redes celulares do dispositivo móvel.
  * Pré-condição: Permissão `ACCESS_FINE_LOCATION` concedida pelo usuário.
* **Saídas e pós-condições:**
  * Saídas: Objeto `Location` com latitude e longitude de precisão métrica.

---

# CAPÍTULO 3 - REQUISITOS NÃO FUNCIONAIS

### 4.1 Usabilidade
* **[Usabilidade.NF001] Design Acessível e Intuitivo**
  * Prioridade: Essencial.
  * Descrição: A interface deve adotar contraste cromático adequado (fundo neutro claro `#F8FAFC`, cartões brancos com acentos em azul `#2563EB` e escuro `#1E293B`), textos de no mínimo 14sp legíveis à luz do dia, ícones autoexplicativos e áreas de toque com no mínimo 48x48dp para todos os botões.
* **[Usabilidade.NF002] Feedback Visual Imediato**
  * Prioridade: Importante.
  * Descrição: Qualquer operação submetida pelo usuário (como login, gravação de alerta ou confirmação) deve produzir resposta visual imediata na forma de animações de transição, snackbars ou toasts.

### 4.2 Confiabilidade
* **[Confiabilidade.NF003] Persistência Segura e Tolerância a Falhas**
  * Prioridade: Essencial.
  * Descrição: As transações de dados devem ser gerenciadas pelo framework Room (SQLite) com propriedades ACID, assegurando que interrupções de processo ou término repentino da bateria não corrompam os dados armazenados.
* **[Confiabilidade.NF004] Prevenção de Confirmações Duplicadas**
  * Prioridade: Importante.
  * Descrição: O banco de dados deve impor uma restrição de integridade relacional unívoca (`Index(alertaId, usuarioId, unique = true)`), impedindo que um mesmo usuário vote múltiplas vezes no mesmo alerta para fraudar métricas.

### 4.3 Desempenho
* **[Desempenho.NF005] Tempo de Resposta e Atualização Reativa**
  * Prioridade: Essencial.
  * Descrição: A transição de telas e a atualização da lista de alertas no RecyclerView devem ocorrer em menos de 100 milissegundos após a alteração no banco local, através do uso de corrotinas assíncronas do Kotlin.
* **[Desempenho.NF006] Otimização de Consumo de Bateria**
  * Prioridade: Importante.
  * Descrição: O uso de sensores de GPS deve ser pontual, requisitando a última localização conhecida via `FusedLocationProviderClient` somente no instante da criação do alerta ou abertura do mapa, cessando o rastreamento em segundo plano para preservar a carga da bateria.

### 4.4 Segurança
* **[Segurança.NF007] Proteção de Credenciais**
  * Prioridade: Essencial.
  * Descrição: O sistema não deve expor a senha do usuário em texto plano na interface e deve armazenar o campo hash no banco de dados local.
* **[Segurança.NF008] Isolamento de Sessão**
  * Prioridade: Importante.
  * Descrição: Nenhuma tela interna (Feed, Cadastro de Alerta, Detalhes ou Mapa) pode ser acessada sem que haja um usuário ativo no `SessionManager`. Caso o usuário não esteja logado, a navegação é imediatamente redirecionada para a tela de Login.

### 4.5 Distribuição
* **[Distribuição.NF009] Empacotamento Autônomo para Android**
  * Prioridade: Essencial.
  * Descrição: O sistema deve ser gerado no formato padrão `.apk` assinado, permitindo a instalação direta em qualquer aparelho Android ou via emulador do Android Studio sem dependências de servidores proprietários instalados à parte.

### 4.6 Padrões
* **[Padrões.NF010] Aderência ao Google Material Design 3 e Arquitetura Android**
  * Prioridade: Essencial.
  * Descrição: O código fonte deve seguir rigorosamente as convenções de nomenclatura oficiais da linguagem Kotlin, padrões de pacotes estruturados por camadas (`ui`, `viewModel`, `database`) e especificações de design do Material 3.

### 4.7 Hardware e Software
* **[Hardware/Software.NF011] Requisitos de Sistema Operacional e Dispositivo**
  * Prioridade: Essencial.
  * Descrição: O aplicativo é compatível com dispositivos que executem Android 8.0 (API nível 26) ou superior, com capacidade mínima de 2GB de RAM, sensor de GPS integrado e suporte ao Google Play Services.

---

# CAPÍTULO 4 - DESCRIÇÃO DA INTERFACE COM O USUÁRIO

### 5.1 [IU001] Tela de Login (`LoginFragment`)
* **Propósito:** Permitir o acesso de usuários já cadastrados ao ambiente do SafeWalk.
* **Componentes:**
  * Logotipo do aplicativo SafeWalk e subtítulo institucional.
  * Campo de entrada para Telefone com máscara numérica.
  * Campo de entrada para Senha com máscara de caracteres ocultos.
  * Botão de ação primária "Entrar" estilizado.
  * Botão secundário de redirecionamento "Criar Conta".
* **Críticas da Interface:**
  * Validação de campo não vazio para ambos os inputs.
  * Bloqueio de submissão enquanto os campos não estiverem preenchidos.

### 5.2 [IU002] Tela de Cadastro (`CadastroFragment`)
* **Propósito:** Registrar novos pedestres e colaboradores no sistema.
* **Componentes:**
  * Campos de texto com contorno: Nome Completo, Telefone, Senha.
  * Botão "Cadastrar" em destaque.
  * Botão de retorno à tela de login.
* **Críticas da Interface:**
  * O número de telefone deve conter no mínimo 10 dígitos.
  * A senha deve conter pelo menos 4 caracteres.

### 5.3 [IU003] Tela Inicial / Feed de Alertas (`FeedAlertaFragment`)
* **Propósito:** Tela Home do aplicativo que lista as ocorrências cadastradas e centraliza as ações de navegação.
* **Componentes:**
  * Cabeçalho escuro (`#1E293B`) com o título "Alertas Próximos" e botão de Logout.
  * Botão "Ver Mapa" no cabeçalho com ícone indicativo.
  * `RecyclerView` dinâmico renderizando os cartões de alerta [IU005].
  * Cada cartão (`item_alerta.xml`) exibe: Barra lateral colorida de identificação, Título em negrito, Descrição truncada em 2 linhas, Ícone de alerta temático e Contador de confirmações.
  * Botão de Ação Flutuante (FAB) verde/azul no canto inferior direito para cadastrar novo alerta.
* **Comportamento Interativo:**
  * Toque em qualquer cartão abre instantaneamente a Tela de Detalhes (`DetalheAlertaBottomSheet`).
  * Toque no FAB abre a Tela de Cadastro de Alerta.
  * Toque em "Ver Mapa" abre a Tela de Mapa Interativo.

### 5.4 [IU004] Tela de Cadastro de Alerta (`CriarAlertaFragment`)
* **Propósito:** Execução de uma ação útil e interativa: submeter um alerta georreferenciado para a comunidade.
* **Componentes:**
  * Campo de texto: Título do Alerta.
  * Campo de texto multilinha: Descrição do Incidente.
  * Botão "Salvar Alerta".
* **Críticas da Interface:**
  * Validação de obrigatoriedade dos campos de texto.
  * Exigência de permissão de GPS concedida para capturar coordenadas reais.

### 5.5 [IU005] Painel Modal de Detalhes do Alerta (`DetalheAlertaBottomSheet`)
* **Propósito:** Apresentar a visão pormenorizada de um incidente selecionado.
* **Componentes:**
  * Título em tamanho destacado (18sp, negrito).
  * Descrição detalhada do risco.
  * Categoria/Tipo do alerta.
  * Indicador de Confirmações Comunitárias com badge numérico.
  * Status da ocorrência (ex.: "ATIVO").
  * Botão de ação "Confirmar Alerta" para incrementar a confiabilidade comunitária.

### 5.6 [IU006] Tela de Mapa Interativo (`MapsFragment`)
* **Propósito:** Exibir a distribuição espacial dos alertas sobre o mapa urbano da cidade.
* **Componentes:**
  * Camada do Google Maps em tela cheia.
  * Marcadores (pins) posicionados na latitude e longitude de cada registro.
  * Habilitação da camada da posição atual do usuário com ponto azul.
  * Janela de detalhes e BottomSheet ativada ao clicar em qualquer marcador.

