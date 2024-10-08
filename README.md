# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

-- **Server Sequence Diagarmas** --


[Server Design]([https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUpjBKhOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUHFcEwNA+BAIQ5yvGnSXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1UidUE01a1SlHUoPUKHxgVKw4C+1LGiWukms06W622n1+h24wy5U6Ai5lCJQpFQSKqJVYYAIZgANXzSFtCOhUHiiUwKeBMsKR0uMGuHTuk3Kqyegb99QgAGt0O2pvtHcck4VsuZygAmJxObotoZisYwDuPKbd1K9gdoIerA7oDiYfPMA13GD1YA7mvnOvjorUJs9KajdTAcnjKYAUSg3jT+d1DgwnkHdJgORsQVOScMHKAAWJwAGYFxfFl33ub9f2gcoAK9ICYBAwcYAPThjwLGAACFgDLFA0QxG8gUoesHxKK4ULfD8Ox-P9yj0W1IVo2IwNHRj72gsA4LnZC+lfVQ0M-PouKwoUqJgfj0UEoizBIk8+TQFAAA9sBQcAUFtSg-3o1MmIg1jpNQjiMO4mBYQ4NQdWIAhEhgCAADMYHM6BMSEiCmLE8oAFZJN6NjZIchTMLTFy3KgDyEiSXz-ISoLNMPLSj08bw-ECaB2HJGAABkIGiJIAjSDIsmQcw2RsypagaZoWgMdQ0oXUVRlmF43g4cDihEwpayfAZW2XFYvkGpZZoBW8xuY+UECqnlYUq6rUXUrFR1ENlNXDMkKQNWlptGE0iTdC0ORgbleQNQVhRgPqxGO6V7wTcpnu0A7nRkMMWXKT1vSDAMgxDT7zUje7oxgWNg3+hMmImtNtp5bNc1IoQfDdHylQqja0Esu9ZUfNNnympdRlmrsg23QcJmC0bIInRqYJgWd52imnbhmlmGZ7ftmb6ITcp0yk0BzAmifW6qyZEimWObOz2PQ+KnJ8YIg2gJAAC9TL6EbKdCznxJgeCAEYF3V2LNcUtMdc3fWjeWYijylvTDOMzIzISpX2eYyaZLkziEvKJLVHc7BPPSvyAqgILTbHKCLYiqL+jDuKnaj1yY5SuO0u8xOspNvLK4K3x-ACLwUHQYn7F8ZhavSTJMDE5q2fKCppC-cqv3qL8Oq61Qet1kXrxC5Nlv-PWUoNqs0HKAAeTcmbQfIg+7p0UHKBWW62qqW92jFsVRjVgdJGByTAJHIan9BrqZcM7vKR6Yyh7QhTCDfRdJjDCMKt5RI3jGqRMLobonSMCgbgmQH7-x3C-M0EZCiWhkHAikhgwEowgWjOe5RtqnzUDjHSxDfTkHxuGQmKp0bfR7s2VOysOY5EtjzBcntca6TkKkN2pkg4MMprZHOjtI5vUnnwxextNIz3TmwiSts+aiPknnCRrtpHLBytpMi0sDJGRMraJ2giVZPhiuHRySlo6x3jqXTKf5srMODmFGAkVebUxURHJy1ii62IyknRxldcrVyKgESEtpyrQhgAAcWXKyNu9VO4W13qrCo0Sh4dXsMuboSD0DCRBONQhMANGG2XmvXJW8d4gJUAfaEsTXxbTqXEs+sQL74KvtAkGd9EGMwASg26cMP48i-nGH+r0Kmhk6bDap+9Ebf3kADSBQMpnulvhSSJsQag+XqWoWE-S36DPTBYVANAYDIFiHYrJr5Jmvy+jM8oOyNlYEvrPBiGMmmvmxgWbhZBqEsiqaYqmUwrlqE-BUfoIKACS0hPzW2nAhWCTw6qZDPILcWTwdAIFAH2VFdMhbAuXAAOWXCzPYMBGhOPNgo7mWcCWvjBRC5c0LYXwsRVMZF+pLr3HRVMTF2LcXcq+CC4loxSXkq4d7Tc3hDYCPRtZRhHj7JiO1pI-hWjKWiQzlbJwSjFUa1UeIl2C8ZXqsrt7fRftTIwGMXKoRqs9UOwNd4guNiS7+PLrItmVKpyuNpfbCxWsrEut8W6suDiJYkWCV4GugRsA+CgNgbg8BdSZBicuFI7cGpsJSU2aodQmitBBTk3pO4FzCuXBqwpbzsLGqXvHcpJb0D5FmNTcteLxaaVtQ2CBoMU0oB2bCOAfadktP2pfKBtzSTdIhhU-ZX10H3U-nM0Z8hf7FMbYA6+wDu173KLghZ47lmTtWWDTIA6QXQy3e-RGNo02jHAXvAh1bk1ejPcuL5eYyK-LdACnd9q6UoGZeUOFCLPVm01dSjhfMoUwuA6y7RXsyKUWogJLAtrAUiKVU6pSvFVI0T2qzcD8ifXwXceY3O4jcNqQxBGxDp5VWaN-SHIF-qKMqpKe7QjadWEkZ1VJTxljnYMZNbR7hejfaGOtYHdDf6zGseVUG5KqUvLuvDWB7j8AtVuP41hrxinC7KYTvYwKomo2FVrpYOB61kgwAAFIQB5HewwAQ+UgD7Ek7NGHWqUgLS0ItkjN4LkTcASzUA4AQHWlAWYMHK1nGfRxspMB14bubc2OYWLQvhciysAA6iwSFw8WjkXKgoOAABpIVTLYMwBA7BTtc8UnygAFYObQAO+zPIR34fPoso6W7SjTr9I-LcfSbmoOvUu-dwA10TKAfK3dy7kYHvaRO1Bp0wDnqq3O2GC6hm8h2S9MIMGxsDPuU5wU0AFRKhVIeoBpQ-BaDfaMWEMGBoZcoFlwKJ2Dm7cpNgR7hgDs-2SBkVICoUA2eC6F3r950blA62199pDvk6W-eGJjLUegauI1zKDWOzVIZUqOjHCryMKaExxmR2OeNcxtjp-VemKe1qpwT5gl5rwyeY5hhngnsIIEAsBK8hFqeaepfBJCyjdO8+Kfz3CgvQIIbEz7Ax-spMWU55jsn2HErBsM3YgJXGWGi59dpyXPPA066U8XFTYaTOK7MzGuulYwiIC9LAYA2BE2EGXhmxJXcvN9wHkPEerRjD5LZHDphVS-3yhANwPAChPeDvj1mbrrSYerbdBCDYEAaAGlUHs77872TlEiMMXPhh8wIDeh0DQt3+t4fLzQHZBfttoJL+mJvlflS3ziRno9a3G8V6RpvVvRedsd7LxXmX1eKl1-aa81M5RXd4A-T8v56gSfCKj3Imn7Cs4St0cry1RjpMNbtXJgTFv85W78bb5OhvnFab9VftRPi9eqbt1w3KQA](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUpjBKhOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUHFcEwNA+BAIQ5yvGnSXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1UidUE01a1SlHUoPUKHxgVKw4C+1LGiWukms06W622n1+h24wy5U6Ai5lCJQpFQSKqJVYYAIZgANXzSFtCOhUHiiUwKeBMsKR0uMGuHTuk3Kqyegb99QgAGt0O2pvtHcck4VsuZygAmJxObotoZisYwDuPKbd1K9gdoIerA7oDiYfPMA13GD1YA7mvnOvjorUJs9KajdTAcnjKYAUSg3jT+d1DgwnkHdJgORsQVOScMHKAAWJwAGYFxfFl33ub9f2gcoAK9ICYBAwcYAPThjwLGAACFgDLFA0QxG8gUoesHxKK4ULfD8Ox-P9yj0W1IVo2IwNHRj72gsA4LnZC+lfVQ0M-PouKwoUqJgfj0UEoizBIk8+TQFAAA9sBQcAUFtSg-3o1MmIg1jpNQjiMO4mBYQ4NQdWIAhEhgCAADMYHM6BMSEiCmLE8oAFZJN6NjZIchTMLTFy3KgDyEiSXz-ISoLNMPLSj08bw-ECaB2HJGAABkIGiJIAjSDIsmQcw2RsypagaZoWgMdQ0oXUVRlmF43g4cDihEwpayfAZW2XFYvkGpZZoBW8xuY+UECqnlYUq6rUXUrFR1ENlNXDMkKQNWlptGE0iTdC0ORgbleQNQVhRgPqxGO6V7wTcpnu0A7nRkMMWXKT1vSDAMgxDT7zUje7oxgWNg3+hMmImtNtp5bNc1IoQfDdHylQqja0Esu9ZUfNNnympdRlmrsg23QcJmC0bIInRqYJgWd52imnbhmlmGZ7ftmb6ITcp0yk0BzAmifW6qyZEimWObOz2PQ+KnJ8YIg2gJAAC9TL6EbKdCznxJgeCAEYF3V2LNcUtMdc3fWjeWYijylvTDOMzIzISpX2eYyaZLkziEvKJLVHc7BPPSvyAqgILTbHKCLYiqL+jDuKnaj1yY5SuO0u8xOspNvLK4K3x-ACLwUHQYn7F8ZhavSTJMDE5q2fKCppC-cqv3qL8Oq61Qet1kXrxC5Nlv-PWUoNqs0HKAAeTcmbQfIg+7p0UHKBWW62qqW92jFsVRjVgdJGByTAJHIan9BrqZcM7vKR6Yyh7QhTCDfRdJjDCMKt5RI3jGqRMLobonSMCgbgmQH7-x3C-M0EZCiWhkHAikhgwEowgWjOe5RtqnzUDjHSxDfTkHxuGQmKp0bfR7s2VOysOY5EtjzBcntca6TkKkN2pkg4MMprZHOjtI5vUnnwxextNIz3TmwiSts+aiPknnCRrtpHLBytpMi0sDJGRMraJ2giVZPhiuHRySlo6x3jqXTKf5srMODmFGAkVebUxURHJy1ii62IyknRxldcrVyKgESEtpyrQhgAAcWXKyNu9VO4W13qrCo0Sh4dXsMuboSD0DCRBONQhMANGG2XmvXJW8d4gJUAfaEsTXxbTqXEs+sQL74KvtAkGd9EGMwASg26cMP48i-nGH+r0Kmhk6bDap+9Ebf3kADSBQMpnumQLEepahYT9LfoM9MFhUA0BgGs5gGUsmvkma-L6MzygbMibERZBCGIYyaa+bGBZuFkGoSyKppiqZTDOWoT8FR+gAoAJLSE-NbacCFYJPDqpkM8gtxZPB0AgUAfZEV0yFv85cAA5ZcLM9gwEaE482CjuZZxxa+IFILlzgshdC2FUx4X6kuvcZFUxUXosxeyr4AL8WjEJcSrh3tNzeENgI9G1lGEePsmI7Wkj+FaNJaJDOVsnBKNlRrVR4iXYLwlcqyu3t9F+1MjAYxUqhGqy1Q7HV3iC42JLv48usi2ZkqnK4yl9sLFaysQ63xTqy4OIliRYJXga6BGwD4KA2BuDwF1JkGJy4UjtwamwlJTZqh1CaK0AFOTek7gXPy5cKrClPOwvqpe8dykFvQPkWY1Ni1YvFppS1DYIGgwTSgDZsI4Bdo2S0-al8oGXNJN0iGFTtlfXQfdT+czRnyF-sU2tgDr7APbXvcouCFnDuWaO90MAwaZB7QC6Ga736IxtEm0Y4C96PNTOUPtXpj3LjeXmMiny3Q-I3daqlKB6XlChTC11ZtVXko4XzMFELAOMu0V7MilFqICSwJa35Ii5V2qUrxVSNE9qs1A-Ij18F3HmNzuI7DakMQhvg6eRVmjv0hz+d6sjCqSnu3w2nVhRGNVSU8ZY52dGDXUe4Xo32hjzWB1Qz+sxzH5V+uSqlLyzrg0gc4-ANVbjeMYa8fJwuimE72MCsJsNhVa6WDgetZIMAABSEAeTXsMAELlIA+xJPTWh1qlIc0tDzZIzeC5Y3AHM1AOAEB1pQFmFB0tZxy3Lr9G7MpMB14rvrc2OYaLguhfCysAA6iwUFw8WjkXKgoOAABpPldLoMwCA7BVtc8UnygAFZ2bQD22zPIB24fPg8jp+7Sjjr9I-LcfSLmoIvXO7dwAl0TKAdKzd87kY7vaSO1Bp0wAnqq1O2GM6hm8g2S9MIUGxsDOuQ5wU0AFRKhVLuoBpQ-BaBfaMWEUGBoZcoFlwKJ2dm7cpNgR7hgDs-2SBkVICoUBWcC8F3rs9Ysdba6+0h7ydKfvDAxlqPQVWEa5hBzHRqEMqUHejmVpG5MCbYzIrHXGuY2y09qnT5PK2U-x8wS814pOMfQ-T-j2EECAWAleQiVP1PkvgkhZR2mefFL57hAXoE4MiZ9gY-2EmLIc4x6TzDiV-X6bsQEjjLCRces0xL7nvrtcKeLkpoNRmFcmYjXXSsYREBelgMAbAsbCDLxTYkruHm+4DyHiPVoxh8lsnRlcJaTzGs1MPdwPACgPe9vj1mbrrTeurbdBCDYEAaAGlUFs77072TlEiMMXPhh8wIDeh0DQt213Z-LzQDZBfttoJL+mJvlflS3ziRnvda2cNd6RpvVvRedsd7LxX6X1eKl1-abDh96nXdvo+V89QxPhFMLD2Bj1uORW6KV6aoxkmGtWpk3x83+dLd+Jt8nA3BSjdcxNzan1aifG6+U7brhuUgA))

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
