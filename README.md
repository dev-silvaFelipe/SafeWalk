#SafeWalk
O SafeWalk nasceu de uma ideia simples: e se a própria comunidade pudesse ajudar a tornar as cidades mais seguras?

O aplicativo é uma plataforma Android focada em segurança colaborativa, onde usuários podem registrar, visualizar e confirmar alertas de incidentes em tempo real através de um mapa interativo.
A proposta é transformar informações individuais em consciência coletiva, ajudando pessoas a identificarem áreas de risco e tomarem decisões mais seguras no dia a dia.

Mais do que um app, o SafeWalk é um experimento de tecnologia aplicada ao cuidado comunitário.

Como o projeto foi pensado:
O SafeWalk foi desenvolvido utilizando práticas modernas do ecossistema Android, com foco em:
código organizado e escalável
facilidade de manutenção
separação clara de responsabilidades
experiência fluida para o usuário

A arquitetura busca simular um projeto real de mercado, seguindo padrões amplamente adotados em aplicações profissionais.

Tecnologias e Arquitetura
Linguagem:
Kotlin 

Arquitetura:
MVVM (Model-View-ViewModel) — separação entre interface, regras de negócio e dados

Single-Activity:
Architecture com navegação baseada em Fragments

Android Jetpack
Navigation Component — controle centralizado de navegação
Room — persistência local com SQLite
ViewModel & Lifecycle — gerenciamento de estado seguro
View Binding — acesso tipado aos layouts

Concorrência
Kotlin Coroutines para operações assíncronas e acesso ao banco de dados
Geolocalização
Google Maps SDK
FusedLocationProviderClient para localização precisa do usuário

Funcionalidades

✅ Mapa Interativo
Visualize alertas próximos à sua localização em tempo real.

✅ Criação de Alertas
Usuários podem registrar ocorrências com título, tipo e descrição.

✅ Validação Comunitária
Outros usuários podem confirmar alertas, aumentando a confiabilidade das informações.

✅ Autenticação de Usuário
Sistema de cadastro e login para manter a integridade das interações.

✅ Feed de Incidentes
Lista cronológica com os alertas mais recentes da comunidade.

 Como rodar o projeto
1️⃣ Clonar o repositório
git clone https://github.com/seu-usuario/SafeWalk.git

2️⃣ Configurar a API Key do Google Maps
Para que o mapa funcione corretamente, é necessário adicionar uma chave da Google Cloud.
Edite o arquivo local.properties na raiz do projeto.
Adicione sua chave:
MAPS_API_KEY="SUA_CHAVE_AQUI"

Objetivo do Projeto
O SafeWalk também funciona como um projeto de aprendizado e portfólio, explorando:
desenvolvimento Android moderno
arquitetura MVVM na prática
integração com mapas e geolocalização
boas práticas de organização de código

🤝 Contribuições

Sugestões, melhorias e feedbacks são sempre bem-vindos!

Se quiser contribuir:

Faça um fork do projeto

Crie uma branch para sua feature

Abra um Pull Request 

Ou
entre em contato comigo Pelo Linkedin:
Linkedin: https://www.linkedin.com/in/dev-rafael/

📌 Status do Projeto

🧪 Em desenvolvimento ativo — novas funcionalidades e melhorias estão sendo adicionadas continuamente.
