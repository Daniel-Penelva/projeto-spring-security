# Anotações `@DynamicUpdate` e `@DynamicInsert`

As anotações `@DynamicUpdate` e `@DynamicInsert` são normalmente usadas em mapeamentos de entidades em sistemas de persistência de dados, como Hibernate em Java. Essas anotações estão associadas ao Hibernate, um framework de mapeamento objeto-relacional.

1. **@DynamicUpdate:**
   - A anotação `@DynamicUpdate` é usada para indicar que apenas as colunas que foram realmente modificadas serão incluídas na cláusula SQL UPDATE gerada pelo Hibernate.
   - Isso significa que, se apenas alguns campos de uma entidade foram modificados, apenas esses campos serão incluídos na declaração UPDATE, em vez de todos os campos da entidade.
   - Essa anotação pode ser útil em cenários em que deseja otimizar as atualizações no banco de dados, evitando a atualização de colunas que não foram alteradas.

Exemplo:
```java
@Entity
@DynamicUpdate
public class Usuario {
    // mapeamento de campos
}
```

2. **@DynamicInsert:**
   - A anotação `@DynamicInsert` é semelhante à `@DynamicUpdate`, mas se aplica à cláusula SQL INSERT.
   - Quando usa `@DynamicInsert`, apenas as colunas que têm valores não nulos serão incluídas na cláusula SQL INSERT gerada pelo Hibernate.
   - Isso é útil quando tem muitas colunas em uma tabela, mas muitas delas têm valores padrão e você deseja otimizar a geração de instruções SQL.

Exemplo:
```java
@Entity
@DynamicInsert
public class SuaEntidade {
    // mapeamento de campos
}
```

Em resumo, ambas as anotações `@DynamicUpdate` e `@DynamicInsert` são utilizadas para otimizar as operações de atualização e inserção no banco de dados, reduzindo a quantidade de dados incluídos nas instruções SQL geradas pelo Hibernate.

# Anotação `@NamedQuery`
A anotação `@NamedQuery` é usada no contexto do Java Persistence API (JPA), que é uma especificação de Java para gerenciamento de persistência de dados em bancos de dados relacionais. Essa anotação é usada para associar uma consulta JPQL (Java Persistence Query Language) nomeada a uma entidade JPA. JPQL é uma linguagem de consulta semelhante ao SQL, mas opera em objetos persistentes.

Analisando a anotação:

```java
@NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario WHERE u.email=:email")
```

- **`@NamedQuery`:** Esta anotação é usada para definir consultas nomeadas em JPA. Consultas nomeadas são consultas JPQL predefinidas que podem ser referenciadas por um nome em vez de fornecer a consulta diretamente no código.

- **`name = "Usuario.findByEmail"`:** Este é o nome atribuído à consulta nomeada. No exemplo, a consulta é chamada "Usuario.findByEmail". Você pode referenciar essa consulta pelo nome em vez de escrever a consulta JPQL toda vez que precisar.

- **`query = "SELECT u FROM Usuario WHERE u.email=:email"`:** Esta é a consulta JPQL associada à consulta nomeada. A consulta em si está buscando uma entidade `Usuario` onde o valor do atributo `email` seja igual ao parâmetro `:email`. A parte `:email` é um parâmetro que você precisa fornecer ao executar a consulta.

Exemplo de uso:

```java
@NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario WHERE u.email=:email")
public class Usuario{
    ...
}
```

Neste exemplo, a consulta JPQL definida pela anotação `@NamedQuery` é referenciada pelo nome "Usuario.findByEmail". Em seguida, um parâmetro é definido (`email`) e a consulta é executada para obter uma lista de usuários que correspondem ao email fornecido. Isso fornece uma maneira mais limpa e reutilizável de definir consultas JPQL em comparação com a inclusão direta da consulta no código.

# UsuarioDao

O `UsuarioDao` está relacionado ao uso de consultas nomeadas no contexto do Spring Data JPA. A anotação `@NamedQuery` não está explicitamente presente neste script, mas o método `findByEmail` é um exemplo do uso implícito de consultas nomeadas no Spring Data JPA.

A interface `UsuarioDao` estende `JpaRepository<Usuario, Integer>`. `JpaRepository` que é uma interface do Spring Data JPA que fornece operações de CRUD (Create, Read, Update, Delete) para a entidade `Usuario`.

O método em questão é o seguinte:

```java
Usuario findByEmail(@Param("email") String email);
```

Explicação do método:

- **`Usuario`:** Indica que o método retorna um objeto da entidade `Usuario`.

- **`findByEmail`:** Este é um método de consulta derivado do nome, o que significa que o Spring Data JPA irá gerar automaticamente uma consulta JPQL com base no nome do método. Neste caso, o método procura um objeto `Usuario` pelo atributo `email`.

- **`@Param("email")`:** Esta anotação é usada para associar o parâmetro `email` do método ao placeholder `:email` na consulta JPQL gerada automaticamente. Isso é necessário porque o método usa uma consulta derivada do nome e, portanto, o nome do parâmetro no método (`email`) precisa ser associado ao nome do parâmetro na consulta (`:email`).

Exemplo de uso:

```java
Usuario usuario = usuarioDao.findByEmail("exemplo@dominio.com");
```

Neste exemplo, o Spring Data JPA automaticamente traduzirá o método `findByEmail` para uma consulta JPQL como "SELECT u FROM Usuario u WHERE u.email = :email", onde `:email` será substituído pelo valor passado como argumento para o método. Isso proporciona uma maneira fácil e rápida de realizar consultas simples sem a necessidade de escrever consultas JPQL manualmente.