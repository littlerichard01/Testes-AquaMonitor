# AquaMonitor

ERP para lojas de aquarismo com monitoramento de temperatura via IoT (ESP32 + MQTT).

Repositório: https://github.com/littlerichard01/AquaMonitor

***

## Integrantes

- Felipe da Silva Moreno
- Lucas Aguena Gatto
- Milene Teixeira dos Santos
- Ricardo Palma Garcia de Campos
- Viviane Rezende Silva

***

## Entidades por responsável

| Entidade                                    | Responsável                    |
| ------------------------------------------- | ------------------------------ |
| Especie, DispositivoIoT                     | Felipe da Silva Moreno         |
| ConfiguracaoTemperatura, LeituraTemperatura | Lucas Aguena Gatto             |
| Comprador, ProgramacaoAlimentacao, Venda    | Milene Teixeira dos Santos     |
| Loja, Aquario                               | Ricardo Palma Garcia de Campos |
| Especime, Alerta                            | Viviane Rezende Silva          |

***

## O que a aplicação faz

Auxilia lojas de aquarismo a organizar seus aquários, criaturas, compradores e vendas em um único sistema, com acompanhamento de temperatura da água em tempo real.

Problemas que resolve:

- Informações espalhadas em planilhas e cadernos: centraliza aquários, espécies, espécimes, compradores e vendas em um só sistema, com isolamento por loja;
- Falta de acompanhamento da temperatura: registra o histórico de medições, permite configurar limites e dispara alertas;
- Controle de vendas sujeito a erro: registra a venda de um espécime ou de um aquário e marca o item como vendido automaticamente, impedindo re-venda;
- Alimentação sem controle: permite agendar os horários de alimentação de cada aquário.

Melhorias trazidas:

- Dashboard com visão geral da loja (temperaturas atuais, itens disponíveis e alertas);
- Alertas com severidade visual (BAIXA, MEDIA, ALTA);
- API REST única, com autenticação JWT e documentação interativa no Swagger;
- Mesma base de código para Android, Desktop e Web.

***

## Tecnologias utilizadas

| Camada                           | Tecnologia                                               |
| -------------------------------- | -------------------------------------------------------- |
| Frontend (Android, Desktop, Web) | Kotlin Multiplatform + Compose Multiplatform |
| Backend                          | Kotlin + Spring Boot 4.1.x                               |
| Persistência                     | Spring Data JPA + Hibernate                              |
| Banco de dados                   | PostgreSQL serverless (Neon.tech)                        |
| API                              | REST com autenticação JWT                                |
| Build                            | Gradle (Kotlin DSL)                                      |

***

## Camadas da arquitetura e pastas

### Backend - `backend/src/main/kotlin/com/example/backend/`

| Camada     | Pasta         | Responsabilidade                      |
| ---------- | ------------- | ------------------------------------- |
| Controller | `controller/` | Endpoints REST                        |
| Service    | `service/`    | Regras de negócio                     |
| Repository | `repository/` | Acesso a dados (Spring Data JPA)      |
| Entity     | `entity/`     | Entidades JPA (uma por tabela)        |
| DTO        | `dto/`        | Objetos de entrada e saída em JSON    |
| Mapper     | `mapper/`     | Conversão Request / Entity / Response |
| Config     | `config/`     | Segurança, JWT e Swagger              |

### Frontend - `frontend/shared/src/commonMain/kotlin/projeto/integrador/sexto/`

| Camada      | Pasta            | Responsabilidade                                                        |
| ----------- | ---------------- | ----------------------------------------------------------------------- |
| Telas       | `ui/screens/`    | Uma pasta por entidade (Tela + ViewModel)                               |
| Componentes | `ui/components/` | Componentes reutilizáveis (Botao, CampoTexto, CampoSelect, CardSimples) |
| Navegação   | `ui/navigation/` | Rotas e ligação rota -> tela (`Rotas.kt`, `NavGraph.kt`)                |
| DTOs        | `data/dto/`      | Objetos de requisição e resposta                                        |
| API         | `data/remote/`   | Cliente HTTP (`AquaMonitorApi`)                                         |
| Sessão      | `data/local/`    | Armazenamento do token (`AuthStore`)                                    |

***

## Instalação, execução e acesso

Requisitos: JDK 21. O banco fica na nuvem (Neon), então não é preciso instalar PostgreSQL ou qualquer outro banco de dados localmente.

O arquivo `backend/.env` guarda as credenciais do banco e não vai para o repositório. Conforme a entrega do projeto em pastas compactadas, você já deve possuir o .env normalmente, mas caso não tenha: copie o `backend/.env.example` para `backend/.env` e solicite os valores ao grupo.&#x20;

Backend (API REST na porta 8081):

```powershell
cd backend
.\gradlew.bat bootRun
# Acesso à documentação interativa: http://localhost:8081/swagger
```

Frontend Desktop:

```powershell
cd frontend
.\gradlew.bat :desktopApp:run
```

Frontend Web:

```powershell
cd frontend
.\gradlew.bat :webApp:jsBrowserDevelopmentRun
# Acesso ao site: http://localhost:8080
```

Android (APK de debug):

```powershell
cd frontend
.\gradlew.bat :androidApp:assembleDebug
# APK será gerado em frontend/androidApp/build/outputs/apk/debug/androidApp-debug.apk

# Com um emulador ativo, execute:
.\gradlew.bat :androidApp:installDebug
# O aplicativo deve ser instalado normalmente
```

***

## Instruções para testes das telas

Para efetivamente testar a tela é necessário o cadastro de uma loja. Ao iniciar a aplicação, faça seu cadastro e então seu login. A partir do login, você verá a tela de Dashboard com botões para todas entidades. 

OBSERVAÇÃO: Algumas entidades, por regra de negócio e lógica de banco de dados, exigem IDs de outras entidades afiliadas à sua loja. Para estas entidades, recomendamos que você use o Swagger (http://localhost:8081/swagger) do projeto para capturar IDs de entidades da sua loja. Desta maneira você consegue inserir os IDs corretos que são pedidos em algumas entidades.

***

## Documentação complementar:

https://docs.google.com/document/d/1yzJRbS6hWBfkwuwOUhk17LZhf82p6Pu43wXA0qDXTPo/edit?tab=t.0#heading=h.v3s7ptbm7xwo
