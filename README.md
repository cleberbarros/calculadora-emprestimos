# Calculadora de Empréstimos

Aplicação web desenvolvida para cálculo de empréstimos, com geração do cronograma de pagamentos, amortização, provisão de juros e evolução do saldo devedor.

O cálculo segue as regras definidas no desafio técnico, utilizando base de 360 dias para o cálculo dos juros.

## Tecnologias

### Backend

- Java 17
- Spring Boot
- Maven
- JUnit 5

### Frontend

- Angular 19
- TypeScript
- HTML
- CSS
- Reactive Forms

## Estrutura do projeto

O projeto está dividido em backend e frontend:

```text
calculadora-emprestimos/
├── src/            # Backend Spring Boot
├── frontend/       # Frontend Angular
├── pom.xml
├── mvnw
└── README.md
```

## Executando o backend

Na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

O backend será iniciado em:

```text
http://localhost:8080
```

Endpoint utilizado para realização do cálculo:

```text
POST /api/emprestimos/calcular
```

## Executando o frontend

Acesse o diretório do frontend:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Inicie a aplicação:

```bash
npm start
```

A aplicação estará disponível em:

```text
http://localhost:4200
```

## Funcionalidades

A aplicação permite informar:

- Data inicial
- Data final
- Data do primeiro pagamento
- Valor do empréstimo
- Taxa de juros

Após o cálculo, é apresentado o cronograma do empréstimo contendo as datas de cálculo e pagamento, amortizações, provisões de juros, juros pagos e evolução dos saldos.

## Regras principais

- A data final deve ser posterior à data inicial.
- A data do primeiro pagamento deve ser posterior à data inicial e anterior à data final.
- A data final é sempre considerada uma data de pagamento.
- A data inicial, a data final e os finais de mês dentro do período são considerados no cronograma.
- Os pagamentos são gerados mensalmente a partir da data do primeiro pagamento.
- Quando o dia de pagamento não existe em determinado mês, é utilizado o último dia disponível desse mês.
- O cálculo dos juros utiliza base de 360 dias.

## Testes

### Backend

Na raiz do projeto:

```bash
./mvnw test
```

### Frontend

Dentro do diretório `frontend`:

```bash
npx ng test --no-watch --browsers=ChromeHeadless
```