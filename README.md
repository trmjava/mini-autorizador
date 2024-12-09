curl -X POST http://localhost:8080/cartoes -H "Content-Type: application/json" -u username:password -w "\n%{http_code}\n" -d '{"numeroCartao": "6549873025634501", "senha": "1234"}'

curl -X GET http://localhost:8080/cartoes/6549873025634501 -H "Content-Type: application/json" -u username:password -w "\n%{http_code}\n"

curl -X POST http://localhost:8080/transacoes -H "Content-Type: application/json" -u username:password -w "\n%{http_code}\n" -d '{"numeroCartao": "6549873025634501", "senhaCartao": "1234", "valor": 10.00}'

