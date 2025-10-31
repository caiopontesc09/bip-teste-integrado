# Sistema de Benefícios - BIP Teste Integrado

Sistema completo para gerenciamento de benefícios com arquitetura multi-camadas, desenvolvido com Spring Boot, EJB e Angular.

## 🏗️ Arquitetura

```
bip-teste-integrado/
├── backend-module/          # API REST Spring Boot
├── ejb-module/             # Lógica de negócio EJB
├── frontend/               # Interface Angular
├── db/                     # Scripts SQL
└── docs/                   # Documentação
```

## 🚀 Tecnologias

### Backend
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistência
- **H2 Database** - Banco em memória
- **Swagger/OpenAPI** - Documentação da API
- **Maven** - Gerenciamento de dependências

### EJB Module
- **Jakarta EE** - Enterprise Java Beans
- **JUnit 5** - Testes unitários

### Frontend
- **Angular 20** - Framework frontend
- **Angular Material** - Componentes UI
- **TypeScript** - Linguagem
- **RxJS** - Programação reativa
- **SCSS** - Estilos

## 📋 Funcionalidades

- ✅ **CRUD de Benefícios** - Criar, listar, atualizar e excluir
- ✅ **Transferência de Valores** - Entre benefícios diferentes
- ✅ **Validações de Negócio** - Saldo suficiente, valores positivos
- ✅ **Interface Responsiva** - Design moderno com Material
- ✅ **API Documentada** - Swagger UI disponível
- ✅ **Testes Unitários** - Cobertura da lógica de negócio

## 🛠️ Como Executar

### 1. Backend (Spring Boot)
```bash
cd backend-module
mvn spring-boot:run
```
API disponível em: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html

### 2. Frontend (Angular)
```bash
cd frontend/angular-app
npm install
npm start
```
Interface disponível em: http://localhost:4200

### 3. Testes EJB
```bash
cd ejb-module
mvn test
```

## 📊 Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/beneficios` | Lista todos os benefícios |
| POST | `/api/v1/beneficios` | Cria novo benefício |
| DELETE | `/api/v1/beneficios/{id}` | Remove benefício |
| POST | `/api/v1/beneficios/transfer` | Transfere valor entre benefícios |

## 🗄️ Banco de Dados

### Schema (H2)
- **beneficios** - Tabela principal com id, nome, descrição, valor, ativo



## 🎨 Interface

### Componentes Angular
- **BeneficioListComponent** - Tabela com listagem
- **BeneficioFormComponent** - Formulário de criação
- **TransferFormComponent** - Interface de transferência

### Recursos UI
- Design Material com tema personalizado
- Formulários reativos com validação
- Notificações em tempo real
- Navegação por abas
- Layout responsivo

## 🧪 Testes

### Backend
- Testes de integração com TestContainers
- Validação de endpoints REST
- Testes de regras de negócio

### EJB
- Testes unitários com JUnit 5
- Mocks para dependências
- Cobertura de cenários de erro

## 📁 Estrutura Detalhada

```
backend-module/
├── src/main/java/com/example/backend/
│   ├── controller/         # Controllers REST
│   ├── service/           # Serviços de negócio
│   ├── entity/            # Entidades JPA
│   ├── repository/        # Repositórios
│   ├── dto/              # Data Transfer Objects
│   └── config/           # Configurações
└── src/main/resources/
    ├── application.properties
    ├── schema.sql
    └── data.sql

ejb-module/
├── src/main/java/com/example/ejb/
│   ├── Beneficio.java     # Entidade
│   └── BeneficioEjbService.java
└── src/test/java/         # Testes unitários

frontend/angular-app/
├── src/app/
│   ├── components/        # Componentes Angular
│   ├── services/         # Serviços HTTP
│   └── app.*            # Componente raiz
└── src/styles.scss       # Estilos globais
```

## 🔧 Configuração

### Variáveis de Ambiente
- `SERVER_PORT=8080` - Porta do backend
- `ANGULAR_PORT=4200` - Porta do frontend

### CORS
Configurado para aceitar requisições do frontend Angular (localhost:4200)

