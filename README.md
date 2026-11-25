# Baozi Store — Desenvolvimento Web Back-End

API REST de clientes, produtos e pedidos. Java 21, Maven, Spring Boot, JPA e H2.

## Executar

No Ubuntu/WSL: `sudo apt update && sudo apt install -y openjdk-21-jdk maven`.
Na raiz (onde está o pom.xml): `mvn spring-boot:run`.
A API usa http://localhost:8080. O banco é criado em data/baozi.mv.db.

## Postman

Importe BaoziStore.postman_collection.json. Execute as 12 requisições na ordem da coleção. Os POST salvam os IDs automaticamente. Capture cada tela com método, URL, corpo (se aplicável), status e resposta. Faça os DELETE por último, na ordem pedido, produto, cliente. O status 204 não tem corpo.

## Regras

Pedido vincula um cliente e um produto existentes; quantidade positiva; estoque booleano indica disponibilidade, não número de unidades. PUT não implementado por ser opcional. POST retorna 201, GET 200, DELETE 204; registros inexistentes retornam 404. Dados inválidos retornam 400; conflito de vínculo ou produto indisponível retorna 409.
