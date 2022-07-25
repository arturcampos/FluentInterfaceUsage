# Capital Gains

Capital Gains é uma aplicação CLI simples usada para calcular operações de compra e venda de ações

## Definições

As operações estarão na ordem em que elas ocorreram, ou seja, a segunda operação na lista aconteceu
depois da primeira e assim por diante. 

As operações serão apenas de compra (BUY) e venda (SELL)

1. O arquivo não deve conter entradas inválidas.
2. Operações de compra não tem taxa.
3. Operações de venda tem taxa quando o valor é maior que 20000.00.
4. Operações de venda podem ter prejuizo
5. Os prejuizos precisam ser devem ser descontados dos lucros.
6. As taxas de venda são 20% do valor dos lucros.

## Bibliotecas e ferramentas escolhidas

Para o projeto, foi usado o mínimo possível de ferramentas.

1. Java 11
2. [Jackson](https://github.com/FasterXML/jackson) - Para parseamento do input JSON
3. [JUnit5's Jupiter](https://junit.org/junit5/docs/current/user-guide/) - Para testes.
4. [Maven](https://maven.apache.org)
5. [Docker](https://www.docker.com)

## Setup e Build

Com maven instalado, na raiz do projeto, o comando para gerar um jar executavel pode ser executado pelo terminal:

``$ mvn package``

A execução do comando deve gerar uma pasta /target, com o arquivo `capitalgains-final.jar`.

## Executando o jar

Para executar o projeto, pode ser utilizado o script `capitalgains.sh`.
Para executá-lo, execute a etapa Setup e Build com sucesso.

Confira também se o script pode ser executado no seu sistema:

``$ chmod +x ./capital-gains.sh``

Por fim, para executar o script pode ser feito da seguinte maneira:

``$ ./capital-gains.sh < your operations file``

Na pasta `testcases` existem algumas operações já pré configuradas de acordo com o teste, que podem ser executadas para acompanhar o output:

``$ ./capital-gains.sh < ./testcases/case1.json``

## Executando com docker

É possível executar a aplicação usando Docker, seguindo alguns passos simples no terminal:

Para construir a imagem:

``$ docker build -t capitalgains .``

Então após a construção da imagem:

``$ docker run --rm -i capitalgains < your operations file``

Ou se quiser executar os casos de teste salvos:

``$ docker run --rm -i capitalgains < ./testcases/case2.json``

OBS: O Dockerfile foi escrito visando a facilidade de execução da aplicação sem a necessidade de instalação de outras ferramentas, o cenário aplicado pode não se aplicar em ambientes produtivos, onde o build do artefato .jar pode ser feito em uma etapa separada em um CI, e a imagem docker utilizada apenas para a execução do mesmo.

## Acessando a javadoc

Uma pasta javadoc é incluída por padrão no projeto, mas uma nova pode ser gerada executando esse comando no terminal:

``$ mvn javadoc:javadoc``

Então uma pasta `javadoc` será gerada na raiz do projeto.

Para acessá-la, apenas abra o arquivo `/javadoc/index.html` no seu browser.

## Testando a aplicação

Para executar apenas os testes da aplicação, pode ser executado o seguinte comando no terminal:

``$ mvn clean compile test``

## Explicação das decisões tomadas

- A aplicação foi feita pensando em uma fluent interface no OperationService, responsável por coordenar a leitura do input JSON, conversão para a operação correta e executá-la.

  - Essa opção foi escolhida pensando em uma cadeia de execução de passos, mas também pensando em permitir o uso/configuração fácil de alguns métodos sem a necessidade de manter o controle de vários retornos de 
    métodos.

- O OperationService atual controla o grupo de operações contidos dentro de cada lista do json de entrada. Após a execução de cada grupo de operações, os valores armazenados devem ser zerados e contabilizados 
  novamente para o novo grupo de operações. Ess controle é feito pelo TaxService existente dentro de cada instancia de OperationService

- O TaxService pode ser melhorado para um Visitor Pattern por exemplo. Optei por não seguir esse caminho pelo tempo e por não ter conhecimento de implementação deste padrão, apesar de ter conhecimento teório do mesmo.

- O TaxSerivce realiza os calculos em métodos privados e executa as operações nos métodos publicos orquestrados pelo OperationService 
  - (privado)Calcula o preço médio poderado
  - (privado)Calcula o prejuízo
  - (privado)Calcula o lucro
  - (publico) doBuy - realiza as operações de compra
  - (publico) doSell - realiza as operações de venda

## Fluxo de desenvolvimento

- A aplicação foi desenvolvida seguindo TDD, focando primeiro na descrição de um comportamento, escrita do código e refinamento do mesmo.