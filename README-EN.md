# Capital Gains

Capital ains is a simple CLI application used for calculate buying and selling stocks operations

## Definitions

Operations are going to be in order, the second operation in the list took place after the first one and so on.

There are two operations buying (BUY) and selling (SELL)

1. The input file must not have any invalid information.
2. Buying operations have no taxes.
3. Selling operations have taxes only if the value or profit is bigger then 20000.00.
4. Operações de venda podem ter prejuizo
5. Expenses must be subtracted from profits.
6. Selling taxes are 20% of profit value.

## Libraries and Tools chosen

For the project, was used the least amount possible of libraries/tools:

1. Java 11
2. [Jackson](https://github.com/FasterXML/jackson) - For JSON parsing
3. [JUnit5's Jupiter](https://junit.org/junit5/docs/current/user-guide/) - For testing.
4. [Maven](https://maven.apache.org)
5. [Docker](https://www.docker.com) 

## Setup and build

With maven installed, on the root of project, the command for assembly an executable jar can be done via terminal:

``$ mvn package``

The command should generate a /target folder, with the `capitalgains-final.jar` file.

## Running the jar

Running the project can be done via the local script `capital0-gains.sh`.
To make use of the script, be sure to successfully execute the Setup and Build stage.

Also make sure the script its executable on your terminal:

``$ chmod +x ./capital-gains.sh``

Finally, running the script can be done via terminal command:

``$ ./capital-gains.sh < your operations file``

On the `testcases` folder it's a set of files where you can execute the application and test the output via terminal:

``$ ./capital-gains.sh < ./testcases/case1.json``

## Running with docker

It's possible to execute the application via Docker, following two simple steps:

To build the image:

``$ docker build -t capitalgains .``

Then after the success:

``$ docker run --rm -i capitalgains < your operations file``

or running the testcases:

``$ docker run --rm -i capitalgains < ./testcases/case3.json``

OBS: The Dockerfile was written focusing to make easy to run the application without the need of installing third tools to build it, the scenario applied may not be applied to production environments where we can use a CI separatedly to build the .jar artifact and a docker image just to run the built jar.

## Accessing the javadoc

A javadoc folder it's included on the default folder, but a new and fresh one can be generating using the command from your terminal:

``$ mvn javadoc:javadoc``

Then a `javadoc` folder will be generated on the root folder of the project.

To access it, just open the file `/javadoc/index.html` on your browser.

## Testing the application

To run only the tests of the application, it can be achieved from running via terminal:

``$ mvn clean compile test``

## Decisions taken explained

- The application design relies on a fluent interface built on OperationService, responsible for coordinate reading the current JSON input, parsing the right operation and executing it.

  - This option has been chosen to focus on a chain of execution steps, but allowing easy use/setup of some methods without the need to keep storing the return of each method executed.

- The current OperationService holds a group of operations received though a json. After each group execution, the values has to be cleaned so in that way, the next group can be processed with its own values. The 
  TaxService is the controller of those values and there is one taxService per OperationService instance.

- The TaxService might be improved using Visitor Pattern. I decided to not use it keeping in mind that I've never implemented it, only read and studied the concepts.

- The TaxSerivce also do the calculation using private methods and process execution using public ones. Public methods are handled by the operationService.
  - (private)Calculate the wighted average cost
  - (private)Calculate the operations expenses
  - (private)Calculate the operations profits
  - (public) doBuy - executes the buying operations
  - (public) doSell - executes the selling operations

## Development flow

- The application was developed following TDD, focusing first on behavior description, writing code then refinement.