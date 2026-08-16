1 .Construa o desenho básico com a estrutura de classes e pacotespara  uma aplicação que recebe dados por uma controller, salava no banco e envia evento para uma fila, utiliza arquitetura hexagonal.

2 .Gere as instruções de um arquivo docker-compose para que eu consiga subir containers docker para rodar um banco postgres, e também quero criar uma fila fifo com nome processa-pagamento, e também sua DLQ, essa fila fifo terá visibilty timeout de 30s, e o dedublipcation baseado no id da mensagem que é o id que é a chave primária do dado salvo.

3. configuração do application properties para conectar com o banco

4. como ficaria a classe que publica a mensagem na fila usando a configuração de fila que foi feita para usar localstack?

5. preciso evitar duplicação do pagamento, além da mensagem duplicada na fila, uma forma seria um identificador no header, mas só isso não é suficiente, qual outro mecanismo eu poderia implementar para evitar que o mesmo pagamento chegue duplicado na api?

6. Como posso criar um script para adicionar um valor ao banco na tabela idempotency para quando rodar os testes validar um cenário.

7. Crie a consulta do pagamento por id trazendo a lista de status. O relacionamento está mapeado apenas do lado de StatusPagamentoEntity unidirecional,

8. Escreva a classes consumer que lerá da fila padrão, essa classe deve ter o deletation policy como onsucess, caso receba uma exceção do tipo nonreprocessable que precisará ser criada pois é customizada a consumer deve entender o deletetion policy como sucesso, remover a mesnagem da fila e enviar a mensagem direto para a dlq, usando o método producer que já existe no projeto, demais exceções as mensagens devem permancer na fila para serem processadas após o visibility timeout, e após as 3 tentativas sem sucesso serem enviadas para a dlq.

9. sim, faça também o ProcessaPagamentoUseCase

10. Analise a classe consumer PagamentoConsumer, ela está com um schedule para ler as mensagens da fila, foi uma sugestào anterior da IA para controlar algumas regras de negócio e mandar a mensagem manualmente para a dlq, mas não está funcionando direito quando a questão é mannter a mensagem na fila para reprocessamento, seria melhor alterar e começar a fazer o consumo da fila por meio do @SqsListner e colococar o deletation_police como anotação no início da classe 