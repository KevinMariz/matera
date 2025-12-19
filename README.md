### Documentação da API Bancária - Transações e Saldo
Esta API foi desenvolvida para gerenciar lançamentos de débito e crédito em contas bancárias, garantindo a integridade dos dados em ambientes de alta concorrência.

### 🛠 Tecnologias Utilizadas
Java 17+

Spring Boot 3.x (Web, Data JPA, Validation)

H2 Database (Banco de dados em memória)

Lombok (Para redução de código boilerplate)

JUnit 5 & AssertJ (Para testes de integração e concorrência)

### 🚀 Como Executar
Certifique-se de ter o Maven instalado.

Execute o comando:

Bash

mvn spring-boot:run
A API estará disponível em http://localhost:8080.

O console do banco de dados H2 pode ser acessado em http://localhost:8080/h2-console 

### 📌 Endpoints da API
### 1. Obter Saldo da Conta

Retorna o saldo atual de uma conta específica.

URL: /api/contas/{id}/saldo

Método: GET

Resposta de Sucesso:

Code: 200 OK

Payload:
### JSON

{
"accountId": 1,
"saldo": 1500.50
}

### Resposta de Erro:

Code: 404 NOT FOUND (Conta não existe)

### 2. Realizar Lançamentos (Débito/Crédito)
   Permite realizar um ou mais lançamentos em uma única requisição. A operação é atômica: se um lançamento falhar (ex: saldo insuficiente), nenhum será processado.

URL: /api/contas/{id}/transacao

Método: POST

Corpo da Requisição (JSON):

JSON

[
{
"type": "CREDIT",
"amount": 1000.00,
"description": "Depósito Mensal"
},
{
"type": "DEBIT",
"amount": 50.00,
"description": "Taxa de Manutenção"
}
]

### Parâmetros do Objeto:

type: String (CREDIT ou DEBIT).

amount: Number (Deve ser positivo).

description: String (Opcional).

### esposta de Sucesso:

Code: 201 CREATED

### Resposta de Erro:

Code: 400 BAD REQUEST (Saldo insuficiente ou dados inválidos).

Code: 404 NOT FOUND (Conta não encontrada).

### 🔒 Consistência e Concorrência
Para atender aos requisitos de segurança e evitar o problema de "Lost Update" (quando duas threads tentam atualizar o saldo simultaneamente), a API utiliza:

Pessimistic Locking (SELECT FOR UPDATE): Ao iniciar o processamento de transações, a API bloqueia a linha da conta no banco de dados. Qualquer outra tentativa de leitura/escrita para aquela conta específica aguardará a conclusão da transação atual.

Transactional Integrity: O uso da anotação @Transactional do Spring garante que o lote de lançamentos siga o princípio ACID.

### 🧪 Testes de Qualidade
A aplicação inclui testes de integração que validam:

Concorrência: Múltiplas threads tentando debitar valores simultâneos da mesma conta.

Atomicidade: Garantia de que o saldo não é alterado se um dos itens da lista de transações for inválido.

Validação de Regras: Impedimento de saldo negativo para operações de débito.