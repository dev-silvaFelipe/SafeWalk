# INSTITUTO FEDERAL DE PERNAMBUCO - IFPE
## ANÁLISE E DESENVOLVIMENTO DE SISTEMAS (ADS)
### RELATÓRIO TÉCNICO DE PROJETO FINAL

---

# SafeWalk — Plataforma Colaborativa de Segurança Urbana

---

## 1. Nome do Aplicativo e dos Integrantes

* **Nome do Aplicativo:** SafeWalk
* **Slogan:** *Transformando a informação individual em segurança e consciência coletiva.*
* **Integrantes da Equipe:**
  * **Felipe Rafael**
  * **João Vitor Revoredo**
  * **Pamela Beatriz**
* **Curso:** Análise e Desenvolvimento de Sistemas (ADS)
* **Instituição:** Instituto Federal de Educação, Ciência e Tecnologia de Pernambuco (IFPE)
* **Ano:** 2026

---

## 2. Problema que o App Resolve

Nas cidades brasileiras contemporâneas, pedestres enfrentam diariamente diversas adversidades ao transitar a pé:
* Ruas e calçadas com iluminação pública inexistente ou queimada;
* Ocorrências recorrentes de assaltos, furtos e abordagens suspeitas em pontos cegos;
* Falhas graves de infraestrutura urbana (buracos perigosos, bueiros abertos, alagamentos, obras sem sinalização).

As vias tradicionais de comunicação do poder público e da polícia são lentas e burocráticas, fazendo com que pedestres só descubram áreas de perigo após se tornarem vítimas.

**A Solução SafeWalk:**
O SafeWalk oferece uma plataforma móvel ágil e colaborativa onde a própria comunidade compartilha, visualiza e valida alertas georreferenciados em tempo real. O aplicativo transforma a experiência isolada de cada morador em consciência coletiva, permitindo que qualquer pessoa consulte rotas seguras e antecipe medidas preventivas.

---

## 3. Público-Alvo

1. **Pedestres Urbanos:** Cidadãos comuns, trabalhadores e pedestres que realizam trajetos diários a pé até paradas de ônibus, terminais e estações.
2. **Estudantes Universitários:** Alunos que transitam entre campi, bibliotecas e alojamentos em horários noturnos.
3. **Moradores e Associações de Bairro:** Comunidades locais interessadas em monitorar e catalogar pontos críticos de seu bairro.
4. **Agentes Públicos e Lideranças Comunitárias:** Pessoas que necessitam de um panorama mapeado das principais queixas de infraestrutura e segurança da região.

---

## 4. Descrição das Principais Funcionalidades

O SafeWalk cumpre com folga 100% dos requisitos mínimos obrigatórios e integra funcionalidades avançadas opcionais:

### 4.1 Funcionalidades Mínimas Obrigatórias Atendidas

1. **Tela Inicial (Home):**
   * Tela `FeedAlertaFragment`, contendo cabeçalho institucional escuro, título em destaque ("Alertas Próximos"), menu de ações rápidas, botão de logout, acesso ao mapa e botão flutuante de criação.
2. **Pelo menos 4 Telas Diferentes com Propósitos Distintos:**
   * **Tela 1 — Cadastro/Login (`LoginFragment` e `CadastroFragment`):** Autenticação segura com validação de telefone e senha.
   * **Tela 2 — Lista/Menu de Funcionalidades (`FeedAlertaFragment`):** Feed com rolagem suave contendo cartões dinâmicos dos incidentes registrados.
   * **Tela 3 — Tela de Detalhes (`DetalheAlertaBottomSheet`):** Apresentação completa dos dados do incidente (título, descrição detalhada, categoria, data/hora, quantidade de validações comunitárias e status).
   * **Tela 4 — Execução de uma Ação Interativa (`CriarAlertaFragment`):** Formulário interativo que captura as coordenadas via GPS e armazena um novo incidente com dados fornecidos pelo usuário.
   * **Tela Bônus Opcional — Mapa Georreferenciado (`MapsFragment`):** Visualização espacial com Google Maps de todos os focos de risco.
3. **Navegação Funcional e Fluida:**
   * Gerenciada pelo **Jetpack Navigation Component** com transições seguras entre telas, passagem de argumentos serializados e suporte ao botão físico/virtual de voltar (`popBackStack`).
4. **Interface Organizada e Acessível:**
   * Desenvolvida com componentes do Google Material Design 3 (`MaterialCardView`, `MaterialButton`, `FloatingActionButton`, `TextInputLayout`);
   * Cores contrastantes e semânticas: Primária `#1E293B` (ardósia escuro), Acento `#2563EB` (azul segurança), Fundo `#F8FAFC` (cinza suave) e Branco `#FFFFFF`;
   * Tipografia legível e ícones nativos coerentes.
5. **Dados Armazenados Localmente:**
   * Utilização da biblioteca de persistência oficial **Android Room (SQLite)** com três entidades estruturadas:
     * `Usuario`: Registro dos usuários, telefones e credenciais;
     * `AlertaInfraestrutura`: Armazenamento de título, descrição, latitude, longitude, tipo, confirmações e timestamp;
     * `ConfirmacaoAlerta`: Chave composta para validação comunitária com integridade referencial e deleção em cascata (`onDelete = CASCADE`);
   * Armazenamento e gerenciamento de estado de autenticação em memória via `SessionManager`.
6. **Funcionalidades Úteis e Interativas:**
   * **Enviar e armazenar informações:** Registro de novos alertas no banco de dados local com coordenadas geográficas imediatas;
   * **Calcular e agregar dados:** Cálculo incremental de confirmações de perigo da comunidade, bloqueando votos duplicados do mesmo usuário;
   * **Mostrar lista de dados:** Listagem em tempo real através de `RecyclerView` sincronizado de forma reativa com o banco via `Kotlin Coroutines Flow`;
   * **Exibir informações úteis:** Exibição da latitude/longitude e status do incidente para tomada de decisão consciente pelo pedestre.

### 4.2 Funcionalidades Opcionais e Diferenciais Implementados

* **Geolocalização via GPS:** Integração com o `FusedLocationProviderClient` da Google para extração precisa das coordenadas do pedestre em tempo real.
* **Integração com Google Maps SDK:** Exibição cartográfica com posicionamento de marcadores nos locais dos alertas e clique interativo.
* **Design Responsivo:** Layout estruturado com `ConstraintLayout` e dimensões relativas, garantindo adaptação perfeita para diversas resoluções de smartphones Android.

---

## 5. Tecnologias Utilizadas

| Camada / Função | Tecnologia | Descrição / Justificativa |
|---|---|---|
| **Linguagem Principal** | **Kotlin 2.0+** | Linguagem oficial e moderna para Android com tipagem segura e concisão |
| **Arquitetura** | **MVVM (Model-View-ViewModel)** | Separação estrita de responsabilidades entre interface, regras e dados |
| **Padrão de Interface** | **Single-Activity Architecture** | Uma única `MainActivity` orquestrando Fragments leves |
| **Navegação** | **Jetpack Navigation Component** | Roteamento seguro de telas com grafo de navegação em XML |
| **Persistência Local** | **Android Room (SQLite)** | ORM com verificação de consultas em tempo de compilação |
| **Concorrência Assíncrona**| **Kotlin Coroutines & Flow** | Execução de tarefas de I/O em background sem congelar a interface |
| **Geolocalização** | **Google Play Services Location** | `FusedLocationProviderClient` para obtenção eficiente de GPS |
| **Mapeamento e Cartografia**| **Google Maps SDK for Android** | Renderização vetorial fluida de mapas e marcadores dinâmicos |
| **Design System** | **Material Design 3 (M3)** | Componentes acessíveis, responsivos e padronizados pelo Google |
| **Compilação e Build** | **Gradle Kotlin DSL (kts)** | Gerenciamento automatizado de dependências e build moderno |

---

## 6. Estrutura e Imagens das Telas (Protótipo e Fluxo)

### 6.1 Diagrama de Navegação entre Telas

```
     +-----------------------------------------------------------+
     |                       SplashScreen                        |
     +-----------------------------------------------------------+
                                   |
                                   v
     +-----------------------------------------------------------+
     |                       LoginFragment                       |
     |           (Inputs: Telefone, Senha / Botões)              |
     +-----------------------------------------------------------+
                     |                           ^
          (Não possui conta?)             (Cadastrado com sucesso)
                     v                           |
     +-----------------------------------------------------------+
     |                     CadastroFragment                      |
     |         (Inputs: Nome, Telefone, Senha / Cadastrar)       |
     +-----------------------------------------------------------+
                     |
            (Login realizado com sucesso)
                     v
     +-----------------------------------------------------------+
     |                   FeedAlertaFragment (Home)               |
     |  - Header: Título "Alertas Próximos", Logout e "Ver Mapa" |
     |  - Lista RecyclerView com Cards de Alertas                |
     |  - Botão Flutuante (+) para Criar Alerta                  |
     +-----------------------------------------------------------+
          /                      |                         \
         / (Clique no item)      | (Clique no Mapa)         \ (Clique no FAB +)
        v                        v                           v
+-----------------------+ +-----------------------+ +----------------------+
| DetalheAlertaModal    | | MapsFragment          | | CriarAlertaFragment  |
| - Título completo     | | - Mapa Google Maps    | | - Título do perigo   |
| - Descrição detalhada | | - Marcadores de risco | | - Descrição          |
| - Total confirmações  | | - Clique no marcador  | | - Coordenadas GPS    |
| - Botão: Confirmar    | |   abre detalhes       | | - Botão Salvar       |
+-----------------------+ +-----------------------+ +----------------------+
```

### 6.2 Especificação Visual das Telas

1. **Tela de Login (`fragment_login.xml`):**
   * Fundo limpo, cabeçalho estilizado com nome SafeWalk, campos arredondados com foco em azul, botão primário contrastante e link estilizado para registro.
2. **Tela de Cadastro (`fragment_cadastro.xml`):**
   * Formulário intuitivo de três passos com validação em tempo real e retorno automático após sucesso.
3. **Tela Feed Principal (`fragment_feed_alertas.xml`):**
   * Top bar escura elegante com título branco e ações em azul acentuado. Lista com elevação de 2dp, cantos arredondados (16dp) e marcadores laterais coloridos indicativos.
4. **Tela de Criação de Alerta (`fragment_alerta.xml`):**
   * Formulário limpo com campos amplos para digitação rápida no celular durante o trânsito a pé e botão de salvar em destaque.
5. **Modal de Detalhes (`bottom_sheet_detalhe_alerta.xml`):**
   * Bottom sheet deslizável com canto arredondado que sobe suavemente sem desorientar o usuário, contendo todas as estatísticas e informações do local.
6. **Tela do Mapa (`fragment_map.xml`):**
   * Visão imersiva com mapa vetorial e ícones de perigo dispostos exatamente nas latitudes e longitudes cadastradas.

---

## 7. Desafios Enfrentados e Soluções Adotadas

### 7.1 Obtenção Precisa e Assíncrona de Coordenadas de Localização
* **Desafio:** A leitura de GPS no Android pode sofrer atrasos, falhas por falta de sinal ou consumo excessivo de energia caso o provedor de localização permaneça ativo indevidamente.
* **Solução:** Utilizou-se o `FusedLocationProviderClient` para requisitar a última posição conhecida com checagem rigorosa de permissões em tempo de execução (`ActivityCompat.checkSelfPermission`). Caso a coordenada ainda não esteja disponível, o aplicativo adverte o usuário de forma amigável antes de efetuar a gravação.

### 7.2 Persistência e Integridade Relacional Local
* **Desafio:** Garantir que confirmações comunitárias não fossem fraudadas por cliques repetidos do mesmo usuário e que alertas apagados limpassem automaticamente suas dependências.
* **Solução:** Configuração do Android Room com Foreign Keys e chave composta unívoca `Index(value = ["alertaId", "usuarioId"], unique = true)` e regra `onDelete = ForeignKey.CASCADE`. Isso garante consistência matemática sem necessidade de backend complexo.

### 7.3 Atualização em Tempo Real da Lista sem Travamentos na Interface (Jank)
* **Desafio:** Manter o Feed e o Mapa perfeitamente sincronizados com o banco de dados sem realizar consultas manuais contínuas (polling) que degradariam a bateria.
* **Solução:** Adoção de **Kotlin Coroutines StateFlow** no `AlertaViewModel`. O Room emite automaticamente novos dados sempre que uma linha da tabela de alertas é adicionada ou atualizada, e o `AlertaAdapter` no fragmento consome essa emissão via `submitList()` de forma não bloqueante.

### 7.4 Compatibilidade com o Ciclo de Vida do Android (Lifecycle Safety)
* **Desafio:** Evitar vazamento de memória (*memory leaks*) ao lidar com mapas do Google (`SupportMapFragment`) e fechamento indevido de telas durante rotação de display.
* **Solução:** Uso do padrão MVVM com `viewLifecycleOwner.lifecycleScope` e `StateIn(WhileSubscribed(5_000))`, assegurando que fluxos assíncronos sejam pausados quando a tela estiver em segundo plano.

---

## 8. Conclusão

O projeto **SafeWalk** atende integralmente a todos os critérios pedagógicos e técnicos exigidos na especificação da disciplina, demonstrando maturidade arquitetural (MVVM, Clean Architecture, Room, Coroutines e Jetpack Navigation), usabilidade focada em segurança pública e pleno cumprimento das diretrizes acadêmicas do curso de ADS - IFPE.

