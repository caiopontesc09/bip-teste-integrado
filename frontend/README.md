# Frontend Angular - Sistema de Benefícios

## Descrição
Interface moderna e responsiva desenvolvida em Angular com Material Design para o sistema de benefícios.

## Funcionalidades
- ✨ Interface moderna com Angular Material
- 📱 Design responsivo
- 🎨 Tema personalizado com gradientes
- 📊 Tabela interativa de benefícios
- 📝 Formulários reativos com validação
- 🔄 Transferência de valores entre benefícios
- 🎯 Notificações em tempo real
- 🚀 Componentes standalone

## Tecnologias
- Angular 20
- Angular Material
- TypeScript
- SCSS
- RxJS
- HttpClient

## Como executar

### 1. Navegar para o diretório
```bash
cd angular-app
```

### 2. Instalar dependências
```bash
npm install
```

### 3. Iniciar o servidor de desenvolvimento
```bash
npm start
```

A aplicação estará disponível em: http://localhost:4200

## Estrutura do Projeto
```
angular-app/
├── src/
│   ├── app/
│   │   ├── components/          # Componentes da aplicação
│   │   │   ├── beneficio-list.component.ts
│   │   │   ├── beneficio-form.component.ts
│   │   │   └── transfer-form.component.ts
│   │   ├── services/            # Serviços
│   │   │   └── beneficio.service.ts
│   │   ├── app.ts              # Componente principal
│   │   ├── app.html            # Template principal
│   │   ├── app.scss            # Estilos principais
│   │   └── app.config.ts       # Configuração da aplicação
│   ├── styles.scss             # Estilos globais
│   └── index.html              # Página principal
└── package.json
```

## Componentes

### BeneficioListComponent
- Lista todos os benefícios em uma tabela Material
- Permite excluir benefícios
- Atualização em tempo real

### BeneficioFormComponent
- Formulário para criar novos benefícios
- Validação reativa
- Campos: nome, descrição, valor

### TransferFormComponent
- Formulário para transferir valores entre benefícios
- Seleção de origem e destino
- Validação de valores

## API Integration
O frontend se conecta com a API REST em `http://localhost:8080/api/v1/beneficios`

## Melhorias Implementadas
- Interface moderna com Material Design
- Navegação por abas
- Formulários reativos com validação
- Notificações de sucesso/erro
- Design responsivo
- Componentes reutilizáveis
- Tipagem TypeScript completa