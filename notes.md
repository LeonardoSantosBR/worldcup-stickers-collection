- linha 1 é a declaração de pacote (*package*): Ela diz a qual "pacote" essa classe pertence. Pacote em Java é basicamente uma pasta/namespace que organiza e agrupa suas classes.

- Um @ é uma *annotation*: um "adesivo" de metadado que você cola em cima da classe, atributo ou método. Ela não executa nada sozinha — outras ferramentas (JPA, Hibernate, Lombok, Spring) leem esses adesivos e geram comportamento a partir deles.

- *Exception (Checked Exception)* Se um método pode lançar uma Exception, o Java obriga você a tratar ou declarar. utilizando try/catch ou declarando na função chamadora do método.

- *RuntimeException (Unchecked Exception)* São exceções que o compilador não exige tratamento. Você pode ignorar — e se acontecer, o programa quebra em runtime.

- *Inversão de Controle (IoC)* normalmente tu controlarias a criação dos objetos com new. Com IoC, tu entrega esse controle ao Spring — ele cria os objetos e te entrega prontos. O controle "inverte": sai das tuas mãos e vai pro container do Spring.

- *Bean* um objeto que o Spring cria, gerencia e guarda no container pra injetar quando alguém precisar.

- *JPA(Java Persistence API)* conjunto de regras e interfaces padronizadas pela linguagem Java para lidar com banco de dados usando Orientação a Objetos.A JPA não faz nada sozinha; ela apenas define como as coisas devem ser feitas.

- *Hibernate* é a implementação mais famosa da JPA. Enquanto a JPA dita as regras, o Hibernate faz o trabalho pesado debaixo dos panos. Ele conecta-se ao banco de dados, gera automaticamente comandos SQL (como SELECT, INSERT, UPDATE), lida com conexões e traduz dados estruturados em tabelas para objetos Java, e vice-versa.

- *Flyway* é ferramenta de código aberto para migração e versionamento de banco de dados Ele funciona como um sistema de controle de versão (semelhante ao Git), mas focado na estrutura e nos dados do seu banco de dados (tabelas, colunas, índices e procedures).

- *Lombok* é um Framework criado sob licença MIT, podendo ser usado livremente em qualquer projeto Java. Seu principal objetivo é diminuir a verbosidade das classes de mapeamento JPA, DTOs e Beans. Sua vantagem é evitar a repetição de código, como a criação de gets e sets para todos os atributos, métodos equals, hashCode, toString, construtores entre outros. Dessa forma o código fica mais limpo e claro.

- *SpringApplication* cria o "cérebro" da aplicação, que gerencia todos os seus componentes (Beans), injeções de dependência e configurações.
• Ele analisa as bibliotecas que estão no seu projeto (como banco de dados, segurança ou web) e configura tudo automaticamente para você não perder tempo com arquivos XML ou classes de configuração complexas.
• e o seu projeto for uma aplicação web, ele localiza e inicializa automaticamente um servidor (geralmente o Tomcat) na porta padrão 8080, fazendo com que seu app fique pronto para receber requisições HTTP imediatamente.

