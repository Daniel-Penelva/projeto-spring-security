# Camada de Serviço

## Classe `FaturaConstant`

A classe `FaturaConstant` contém constantes de string relacionadas a mensagens de erro ou informações específicas sobre faturas. Explicação linha por linha:

```java
public class FaturaConstant {

    // Constante que representa uma mensagem de erro genérica
    public static final String SOMETHING_WENT_WRONG = "Algo deu errado";

    // Constante que representa uma mensagem de erro relacionada a datas inválidas
    public static final String INVALID_DATA = "Datas inválidas";
    
}
```

Explicação das linhas:

1. **`public static final String SOMETHING_WENT_WRONG = "Algo deu errado";`**: Declara uma constante de string chamada `SOMETHING_WENT_WRONG` com o valor "Algo deu errado". O modificador `public` significa que essa constante pode ser acessada de fora da classe. `static` indica que a constante pertence à classe em vez de instâncias individuais da classe, e `final` indica que o valor da constante não pode ser alterado.

2. **`public static final String INVALID_DATA = "Datas inválidas";`**: Declara outra constante de string chamada `INVALID_DATA` com o valor "Datas inválidas". Da mesma forma que a primeira constante, esta também é pública, estática e final.

## Classe `FaturaUtil`
A classe  `FaturaUtil` contém um método utilitário para obter um objeto `ResponseEntity`. 

Explicação linha por linha:

```java
public class FaturaUtil {

    // Um construtor privado para garantir que a classe não seja instanciada diretamente
    private FaturaUtil() {

    }

    // Método estático que retorna um ResponseEntity<String> com base nos parâmetros fornecidos
    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus) {
        // Cria um novo ResponseEntity com a mensagem fornecida e o HttpStatus fornecido
        return new ResponseEntity<String>("Mensagem: " + message, httpStatus);
    }
}
```

Explicação das linhas:

1. **`public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus) {`**: Declara um método estático chamado `getResponseEntity`. Este método aceita dois parâmetros: uma mensagem (do tipo `String`) e um `HttpStatus`. Ele retorna um objeto `ResponseEntity` que encapsula a mensagem e o código de status HTTP.

2. **`return new ResponseEntity<String>("Mensagem: " + message, httpStatus);`**: Cria e retorna um novo objeto `ResponseEntity`. O conteúdo da resposta é uma string que combina a mensagem fornecida com a palavra "Mensagem:". O código de status HTTP é o que foi passado como argumento.

O propósito desse utilitário é fornecer uma maneira fácil e centralizada de criar objetos `ResponseEntity` para representar respostas HTTP no contexto relacionado a faturas. Isso pode ser útil em controladores de API, por exemplo, onde deseja padronizar a maneira como as respostas são construídas.

## Classe `UsuarioServiceImpl`
A classe UsuarioServiceImpl é uma implementação de serviço em Java usando o Spring Boot. 

Explicando as partes principais:

1. **Anotações:**
    - **`@Slf4j`:** Esta anotação é parte do projeto Lombok e gera automaticamente um logger SLF4J chamado `log` na classe. Isso elimina a necessidade de escrever manualmente código para criar e gerenciar logs.
    - **`@Service`:** Anotação do Spring que marca a classe como um serviço. É usado para identificar classes de serviço na camada de serviço em uma arquitetura típica de aplicativo Spring.

2. **`@AllArgsConstructor`:** Anotação do Lombok que gera automaticamente um construtor que recebe todos os campos da classe como argumentos. Neste caso, é usado para criar um construtor que aceita um parâmetro `UsuarioDao`, que é automaticamente injetado pelo Spring.

3. **`public class UsuarioServiceImpl implements UsuarioService {`:** A classe `UsuarioServiceImpl` implementa a interface `UsuarioService`.

4. **Campos:**
    - **`private UsuarioDao usuarioDao;`:** Um campo que representa um objeto `UsuarioDao`. Este campo é injetado pelo Spring.

5. **Método `signUp`:**
    - **`public ResponseEntity<String> signUp(Map<String, String> requestMap) {`:** Este método é usado para lidar com o processo de registro de usuários.
    - **`log.info("Registro interno de um usuário {}", requestMap);`:** Registra informações no log sobre o registro do usuário.
    - **`validateSignUpMap`:** Um método privado que verifica se o mapa de solicitação (`requestMap`) contém as chaves necessárias.
    - **`Usuario user = usuarioDao.findByEmail(requestMap.get("email"));`:** Obtém um usuário pelo e-mail usando o método `findByEmail` do `UsuarioDao`.
    - **`if (Objects.isNull(user)) { ... } else { ... }`:** Verifica se o usuário já existe no banco de dados e executa a lógica apropriada.
    - **`FaturaUtil.getResponseEntity(...)`:** Retorna um objeto `ResponseEntity` construído com base no resultado do processo.

6. **Método `validateSignUpMap`:**
    - **`private boolean validateSignUpMap(Map<String, String> requestMap) {`:** Método privado que valida se o mapa de solicitação contém as chaves necessárias.

7. **Método `getUserFromMap`:**
    - **`private Usuario getUserFromMap(Map<String, String> requestMap) {`:** Método privado que cria e retorna um objeto `Usuario` com base no mapa de solicitação.

O código é um serviço de registro de usuários com algumas verificações básicas e interações com o banco de dados por meio do `UsuarioDao`.

## Comunicação entre as classes `UsuarioServiceImpl`, `FaturaUtil` e `FaturaConstant`

A comunicação entre as classes `UsuarioServiceImpl`, `FaturaUtil` e `FaturaConstant` no contexto do script acima.

1. **`UsuarioServiceImpl`:**
   - É uma classe de serviço que lida com o registro de usuários.
   - Utiliza a anotação `@Service`, indicando que é um componente gerenciado pelo Spring como um serviço.
   - Injeta uma instância de `UsuarioDao` para interagir com o banco de dados.
   - Utiliza métodos utilitários da classe `FaturaUtil` para criar objetos `ResponseEntity` com base no resultado do registro.

2. **`FaturaUtil`:**
   - É uma classe de utilitários que fornece métodos estáticos para operações comuns.
   - Contém o método `getResponseEntity`, que cria e retorna um objeto `ResponseEntity` com base em uma mensagem e um código de status HTTP.
   - Não possui estado, pois todos os métodos são estáticos, e a classe é usada principalmente para fornecer funcionalidades compartilhadas.

3. **`FaturaConstant`:**
   - É uma classe que contém constantes de string relacionadas a mensagens específicas, como mensagens de erro ou informações sobre faturas.
   - As constantes são usadas para manter mensagens padronizadas que podem ser referenciadas em várias partes do código.

A comunicação ocorre da seguinte forma:

- **`UsuarioServiceImpl` chama métodos em `FaturaUtil`:**
  - No método `signUp` de `UsuarioServiceImpl`, são chamados métodos estáticos da classe `FaturaUtil` para obter objetos `ResponseEntity` com base em diferentes situações (sucesso, usuário já existente, dados inválidos, etc.).
  - Por exemplo, `FaturaUtil.getResponseEntity("Usuário cadastrado com Sucesso!", HttpStatus.CREATED)` é usado para criar uma resposta de sucesso.

- **`UsuarioServiceImpl` faz referência a constantes de `FaturaConstant`:**
  - No mesmo método `signUp`, são feitas referências a constantes de `FaturaConstant`. Por exemplo, `FaturaConstant.INVALID_DATA` é usado como mensagem quando os dados de registro não são válidos.
  - Essas constantes ajudam a padronizar as mensagens usadas na aplicação.

Em resumo, `FaturaUtil` fornece métodos utilitários para construir objetos `ResponseEntity`, enquanto `FaturaConstant` mantém constantes que são usadas para mensagens padronizadas. `UsuarioServiceImpl` utiliza essas classes para tornar seu código mais modular, reutilizável e fácil de manter.

# Autor
## Feito por: `Daniel Penelva de Andrade`