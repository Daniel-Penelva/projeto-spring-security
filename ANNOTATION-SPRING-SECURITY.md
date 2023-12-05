# Spring Security

## Classe JwtUtil

Esta classe `JwtUtil` é uma classe utilitária para lidar com a geração e validação de tokens JWT (JSON Web Tokens). Tokens JWT são utilizados para autenticação e autorização em sistemas web.

```java
package com.apirest.projetospringsecurity.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

    private String secret = "springboot";

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

```

### **Variável `secret`:**
   - `private String secret = "springboot";`
   - Define uma chave secreta usada para assinar e verificar a autenticidade do token JWT. É essencial manter esta chave em segredo para garantir a segurança do sistema.

### Analisando os métodos dessa classe:

### 1. **`extractUsername(String token)`:**
   - Extrai o nome de usuário (subject) do token JWT.
   - Usa o método `extractClaims` para obter o subject.

```java
public String extractUsername(String token) {
    return extractClaims(token, Claims::getSubject);
}
```

### 2. **`extractExpiration(String token)`:**
   - Extrai a data de expiração do token JWT.
   - Usa o método `extractClaims` para obter a data de expiração.

```java
public Date extractExpiration(String token) {
    return extractClaims(token, Claims::getExpiration);
 }
```

### 3. **`extractClaims(String token, Function<Claims, T> claimsResolver)`:**
   - Extrai outras informações (claims) do token usando um resolvedor de claims personalizado, que é passado como um `Function`. Isso permite extrair diferentes informações do token de forma flexível.
   - Extrai informações (claims) específicas do token usando um resolvedor de claims personalizado (`claimsResolver`).
   - Usa o método `extractAllClaims` para obter todos os claims do token e aplica o resolvedor.

```java
public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
}
```

### 4. **`extractAllClaims(String token)`:**
   - Extrai todos os claims do token JWT.
   - Usa a biblioteca `io.jsonwebtoken` para fazer o parsing do token e obter o corpo (claims).

```java
public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
}
```

### 5. **`isTokenExpired(String token)`:**
   - Verifica se o token JWT está expirado comparando a data de expiração com a data atual.

```java
private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
}
```

### 6. **`generateToken(String username, String role)`:**
   - Gera um novo token JWT com um nome de usuário e uma função (role) associado.
   - Cria um conjunto de claims, incluindo a função, usando o método `createToken`.

```java
public String generateToken(String username, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);
    return createToken(claims, username);
}
```

### 7. **`createToken(Map<String, Object> claims, String subject)`:**
   - Cria um token JWT com base em um conjunto de claims e um subject (nome de usuário). Configura informações como data de emissão, data de expiração e algoritmo de assinatura.
   - Configura informações como data de emissão, data de expiração e algoritmo de assinatura.
   - Usa a biblioteca `io.jsonwebtoken` para construir e assinar o token.

```java
private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, secret).compact();
}
```

### 8. **`validateToken(String token, UserDetails userDetails)`:**
   - Valida se um token JWT é válido comparando o nome de usuário extraído do token com o nome de usuário fornecido pelos detalhes do usuário (`UserDetails`). Também verifica se o token não está expirado.
   - Valida se um token JWT é válido:
      - Extrai o nome de usuário do token.
      - Compara o nome de usuário extraído com o nome de usuário fornecido pelos detalhes do usuário (`UserDetails`).
      - Verifica se o token não está expirado.

```java
public Boolean validateToken(String token, UserDetails userDetails){
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
```

### Descrição de alguns dos métodos da biblioteca `io.jsonwebtoken` utilizados nesta classe:

A classe utiliza a biblioteca `io.jsonwebtoken` para manipular tokens JWT. 

- **`Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()`:**
   - Decodifica o token JWT (`parseClaimsJws`) utilizando a chave secreta (`setSigningKey(secret)`) e recupera o corpo do token (`getBody()`), que contém as informações (claims).

- **`Jwts.builder()...signWith(SignatureAlgorithm.HS256, secret).compact()`:**
   - Constrói um novo token JWT (`builder()`), configura claims, data de emissão, data de expiração, assina o token com um algoritmo de assinatura (`HS256` no caso) e compacta o token para uma String.

Esta classe é um componente de serviço (`@Service`) que encapsula a lógica relacionada a tokens JWT em um serviço reutilizável. É frequentemente utilizada em sistemas de autenticação e autorização, onde os tokens JWT são usados para representar informações de autenticação e/ou autorização.

## Classes e Interfaces utilizados

## Interface `UserDetailsService`
A interface `UserDetailsService` faz parte do framework Spring Security e é usada para carregar detalhes específicos do usuário. Sua implementação é fundamental para integrar o Spring Security com um sistema de autenticação personalizado.

A responsabilidade principal da interface `UserDetailsService` é fornecer uma maneira de carregar os detalhes do usuário com base em um identificador, como um nome de usuário. Normalmente, esses detalhes incluem informações como a senha do usuário, suas permissões (papéis ou autoridades), e outros dados relevantes para a autenticação e autorização.

A interface `UserDetailsService` define um único método:

```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

- **`loadUserByUsername(String username)`:**
   - Este método é responsável por carregar os detalhes do usuário com base no nome de usuário fornecido.
   - Retorna um objeto que implementa a interface `UserDetails`, que fornece informações sobre o usuário, incluindo seu nome de usuário, senha (ou credencial), e as autoridades associadas.

A implementação padrão fornecida pelo Spring Security é a `org.springframework.security.core.userdetails.UserDetailsService`. No entanto, pode criar sua própria implementação personalizada dessa interface para integrar o Spring Security com o sistema de autenticação de seu aplicativo.

Ao configurar o Spring Security, você associará sua implementação `UserDetailsService` ao mecanismo de autenticação, permitindo que o Spring Security use seus detalhes personalizados do usuário durante o processo de autenticação.

## Classe `CustomerDetailsService`

A classe `CustomerDetailsService` é uma implementação personalizada da interface `UserDetailsService`, que é uma parte fundamental do Spring Security. A classe é responsável por carregar os detalhes do usuário durante o processo de autenticação. 

```java
package com.apirest.projetospringsecurity.jwt;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apirest.projetospringsecurity.daos.UsuarioDao;
import com.apirest.projetospringsecurity.entities.Usuario;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioDao usuarioDao;

    private Usuario userDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Dentro de loadUserByUsername {}", username);

        userDetails = usuarioDao.findByEmail(username);

        if (!Objects.isNull(userDetails)) {
            return new org.springframework.security.core.userdetails.User(userDetails.getEmail(),
                    userDetails.getPassword(), new ArrayList<>());

        } else {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    public Usuario getUserDetail() {
        return userDetails;
    }

}
```

Principais pontos deste script:

1. **Anotações e Imports:**
   - A classe está anotada com `@Service` para indicar ao Spring que ela é um componente de serviço a ser gerenciado pelo contêiner de injeção de dependências.
   - Utiliza diversas importações de classes e pacotes do Spring Security, como `UserDetailsService`, `UserDetails`, e `UsernameNotFoundException`.

2. **Injeção de Dependência:**
   - Utiliza a anotação `@Autowired` para injetar a dependência de um objeto `UsuarioDao`. Essa dependência será usada para interagir com o banco de dados para recuperar os detalhes do usuário.

3. **Método `loadUserByUsername`:**
   - Implementa o método da interface `UserDetailsService`. Este método é chamado durante o processo de autenticação para carregar os detalhes do usuário com base no nome de usuário (geralmente, o email).
   - Usa o `UsuarioDao` para buscar um usuário no banco de dados com o email fornecido.
   - Se o usuário for encontrado, cria um objeto `UserDetails` do Spring Security com as informações do usuário (email, senha e uma lista vazia de autoridades).
   - Se o usuário não for encontrado, lança uma exceção `UsernameNotFoundException`.

4. **Atributo `userDetails`:**
   - Declara um atributo `userDetails` para armazenar temporariamente os detalhes do usuário recuperados durante a autenticação.

5. **Método `getUserDetail`:**
   - Fornece um método público para obter os detalhes do usuário após a autenticação. Esse método pode ser útil em outras partes do código que precisam acessar os detalhes do usuário após a autenticação.

6. **Logging:**
   - Usa o Logger `log` do Lombok para registrar mensagens informativas, como a execução do método `loadUserByUsername`.

Em resumo, essa classe atua como um serviço de detalhes do usuário para o Spring Security. Ela implementa a interface `UserDetailsService`, fornecendo uma lógica personalizada para carregar os detalhes do usuário do banco de dados com base no email durante o processo de autenticação. O objeto `UsuarioDao` é utilizado para interagir com o banco de dados.

## Classe `OncePerRequestFilter`

A classe `OncePerRequestFilter` é uma classe utilitária fornecida pelo Spring Security que é usada para garantir que um filtro seja executado apenas uma vez por solicitação HTTP. Essa classe estende a classe `GenericFilterBean` e adiciona a lógica necessária para garantir a execução única de um filtro durante o ciclo de vida de uma solicitação.

A principal característica dessa classe é que ela herda da classe `GenericFilterBean` e implementa uma lógica especial para garantir que o método `doFilter` seja chamado apenas uma vez por solicitação, independentemente de como a cadeia de filtros está configurada.

Principais pontos sobre a classe `OncePerRequestFilter`:

1. **Execução Única por Solicitação:**
   - O nome "OncePerRequestFilter" sugere que a lógica do filtro seja executada apenas uma vez durante o processamento de uma solicitação HTTP, independentemente de quantas vezes a cadeia de filtros foi configurada.

2. **Método `doFilterInternal`:**
   - A classe estende a classe abstrata `OncePerRequestFilter` e requer a implementação do método `doFilterInternal`. Esse método contém a lógica específica do filtro que será executada durante o processamento da solicitação.

3. **Método `doFilter`:**
   - O método `doFilter` da classe `OncePerRequestFilter` contém a lógica que garante a execução única. Ele verifica se o filtro já foi chamado para a solicitação atual usando uma variável de controle armazenada no atributo da requisição.

4. **Variável de Controle:**
   - A variável de controle geralmente é armazenada no objeto `HttpServletRequest`. Isso permite que o filtro verifique se já foi executado durante a mesma solicitação.

Essa classe é uma escolha comum ao criar filtros no Spring Security, pois ajuda a garantir um comportamento consistente e previsível durante o processamento das solicitações.


## Interface `Claims`

A interface `Claims` faz parte da biblioteca Java JWT (JSON Web Tokens), que é frequentemente usada para criar, parsear e validar tokens JWT em aplicações Java. A biblioteca mais comum que implementa essa interface é a `io.jsonwebtoken.Claims` do pacote `io.jsonwebtoken`.

JSON Web Tokens (JWT) são uma forma compacta de representar informações de forma segura entre duas partes. Eles são frequentemente usados para autenticação e troca de informações entre sistemas.

A interface `Claims` define métodos para acessar as reivindicações (claims) contidas em um token JWT. Reivindicações são declarações sobre uma entidade (geralmente o usuário) e metadados adicionais. 

Alguns dos métodos principais definidos pela interface:

1. **`get(String name)`:**
   - Obtém o valor associado a uma reivindicação específica pelo nome.

2. **`put(String name, Object value)`:**
   - Associa um valor a uma reivindicação pelo nome. Isso é útil para adicionar ou substituir reivindicações em um token JWT.

3. **`getIssuedAt()`:**
   - Obtém a data e hora em que o token foi emitido.

4. **`getExpiration()`:**
   - Obtém a data e hora em que o token JWT expirará.

5. **`setExpiration(Date expiration)`:**
   - Define a data e hora de expiração do token JWT.

6. **`getSubject()`:**
   - Obtém o assunto do token, que geralmente é o identificador exclusivo do usuário.

7. **`setSubject(String subject)`:**
   - Define o assunto do token.

8. **`getIssuer()`:**
   - Obtém o emissor do token, que é a entidade que emitiu o token.

9. **`setIssuer(String issuer)`:**
   - Define o emissor do token.

Esses são apenas alguns exemplos, e há muitos outros métodos disponíveis na interface `Claims` que permitem acessar várias reivindicações e informações do token JWT.

Exemplo de como poderia usar a interface `Claims` em conjunto com a biblioteca JWT em Java:

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtExample {

    public static void main(String[] args) {
        String token = "seu-token-jwt-aqui";

        // Parse do token
        Claims claims = Jwts.parser().setSigningKey("chave-secreta").parseClaimsJws(token).getBody();

        // Acessando reivindicações
        String subject = claims.getSubject();
        Date expiration = claims.getExpiration();

        System.out.println("Assunto: " + subject);
        System.out.println("Data de expiração: " + expiration);
    }
}
```

No exemplo acima, o método `parseClaimsJws` é usado para parsear o token JWT e obter um objeto `Claims`. Em seguida, você pode usar os métodos da interface `Claims` para acessar as informações do token. Certifique-se de substituir "seu-token-jwt-aqui" pela string real do seu token e "chave-secreta" pela chave secreta usada para assinar o token.

## Classe `SecurityContextHolder`

A classe `SecurityContextHolder` é uma parte fundamental do Spring Security e é usada para armazenar e fornecer o contexto de segurança durante a execução de um aplicativo Spring. O contexto de segurança contém informações sobre a autenticação e autorização do usuário atualmente logado no sistema.

Principais aspectos da classe `SecurityContextHolder`:

1. **Armazenamento do Contexto de Segurança:**
   - A classe `SecurityContextHolder` armazena o contexto de segurança em um `ThreadLocal`, o que significa que cada thread em execução tem seu próprio contexto de segurança.
   - Um `ThreadLocal` é uma variável que fornece acesso a dados associados a um thread específico. Isso ajuda a garantir que o contexto de segurança seja exclusivo para cada thread em um ambiente multithread.

2. **Métodos Principais:**
   - **`getContext()`:**
     - Obtém o contexto de segurança atual associado ao thread em execução.
   - **`setContext(SecurityContext securityContext)`:**
     - Define o contexto de segurança para o thread em execução.
   - **`clearContext()`:**
     - Limpa o contexto de segurança associado ao thread em execução.

3. **Exemplo simples de Uso:**
   ```java
   // Obtendo o contexto de segurança atual
   SecurityContext context = SecurityContextHolder.getContext();

   // Criando um novo contexto de segurança
   SecurityContext newContext = new SecurityContextImpl();

   // Definindo o novo contexto de segurança para o thread atual
   SecurityContextHolder.setContext(newContext);

   // Limpa o contexto de segurança associado ao thread atual
   SecurityContextHolder.clearContext();
   ```

4. **Integração com Spring Security:**
   - Durante o processo de autenticação bem-sucedido, o contexto de segurança é geralmente preenchido com uma instância de `Authentication`, que contém informações sobre o usuário autenticado, como nome de usuário, credenciais e autoridades.
   - O `Authentication` é armazenado no contexto de segurança por meio do `SecurityContextHolder`. Este contexto é então acessado em várias partes do aplicativo para verificar informações de autenticação e autorização.

5. **Limpeza Automática em Threads:**
   - O Spring Security cuida da limpeza automática do contexto de segurança quando um thread é encerrado. No entanto, ao iniciar novos threads manualmente em um aplicativo, é importante garantir que o contexto de segurança seja gerenciado apropriadamente.

A classe `SecurityContextHolder` desempenha um papel crucial na gestão do contexto de segurança em aplicações Spring Security. Ele permite o acesso fácil e seguro às informações de autenticação e autorização do usuário em diferentes partes do aplicativo.

## Classe `UsernamePasswordAuthenticationToken`

A classe `UsernamePasswordAuthenticationToken` é uma implementação específica da interface `Authentication` fornecida pelo Spring Security. Ela é frequentemente usada para representar credenciais de autenticação durante o processo de autenticação em um aplicativo Spring.

Essa classe é particularmente útil quando um usuário fornece um nome de usuário e senha para autenticação. Ela carrega essas informações e, opcionalmente, pode carregar detalhes adicionais associados à autenticação, como detalhes do cliente, endereço IP, etc.

Detalhes importantes sobre a classe `UsernamePasswordAuthenticationToken`:

1. **Construtores:**
   - Ela possui vários construtores, mas o mais comum aceita dois parâmetros: o nome de usuário (`principal`) e a senha (`credentials`).
   ```java
   public UsernamePasswordAuthenticationToken(Object principal, Object credentials);
   ```

2. **Métodos principais:**
   - **`getPrincipal()`:**
     - Retorna o nome de usuário (ou outra informação principal associada à autenticação).
   - **`getCredentials()`:**
     - Retorna a senha (ou outras credenciais associadas à autenticação).
   - **`getDetails()`:**
     - Retorna detalhes adicionais associados à autenticação (pode ser `null`).
   - **`setDetails(Object details)`:**
     - Define detalhes adicionais associados à autenticação.

3. **Utilização em Autenticação:**
   - Durante o processo de autenticação, um objeto `UsernamePasswordAuthenticationToken` é geralmente criado para representar as credenciais do usuário.
   - Após a autenticação bem-sucedida, esse token é normalmente substituído por um `Authentication` mais completo, que pode incluir detalhes adicionais e permissões (autoridades).

4. **Exemplo simples de Uso:**
   ```java
   String username = "john_doe";
   String password = "senha_secreta";

   // Criando um token de autenticação com nome de usuário e senha
   UsernamePasswordAuthenticationToken authenticationToken =
       new UsernamePasswordAuthenticationToken(username, password);

   // Enviando o token para o provedor de autenticação (normalmente através do AuthenticationManager)
   Authentication authenticated = authenticationManager.authenticate(authenticationToken);

   // Após a autenticação bem-sucedida, o token pode ser armazenado no contexto de segurança
   SecurityContextHolder.getContext().setAuthentication(authenticated);
   ```

A classe `UsernamePasswordAuthenticationToken` é frequentemente usada em conjunto com o mecanismo de autenticação do Spring Security, e sua instância é geralmente criada a partir dos detalhes fornecidos pelo usuário durante a tentativa de login. Durante o processo de autenticação, esse token é submetido ao `AuthenticationManager`, que verifica as credenciais e, se bem-sucedido, retorna um objeto `Authentication` mais completo, normalmente uma instância de `org.springframework.security.core.userdetails.User`.

## Classe `WebAuthenticationDetailsSource`

A classe `WebAuthenticationDetailsSource` é uma implementação da interface `AuthenticationDetailsSource` fornecida pelo Spring Security. Essa classe é usada para criar instâncias de `WebAuthenticationDetails`, que contêm detalhes específicos da autenticação relacionados a solicitações web, como o endereço IP do cliente e a sessão HTTP.

A interface `AuthenticationDetailsSource` é usada pelo `AuthenticationProvider` para criar objetos `AuthenticationDetails` durante o processo de autenticação. A classe `WebAuthenticationDetailsSource` é específica para aplicativos da web e estende a funcionalidade básica para incluir detalhes específicos da web.

Principais características da classe `WebAuthenticationDetailsSource`:

1. **Criação de `WebAuthenticationDetails`:**
   - O método principal é `buildDetails(HttpServletRequest request)`, que é chamado para criar uma instância de `WebAuthenticationDetails` com base na solicitação web.

2. **Detalhes Específicos da Web:**
   - Além dos detalhes padrão fornecidos pela classe `AuthenticationDetails`, como o nome de usuário e a senha, a `WebAuthenticationDetails` contém detalhes específicos da web, como o endereço IP do cliente e a sessão HTTP.

3. **Exemplo simples de Uso:**
   ```java
   WebAuthenticationDetailsSource source = new WebAuthenticationDetailsSource();

   HttpServletRequest request = ...; // Obtenha a solicitação web

   WebAuthenticationDetails details = source.buildDetails(request);
   ```

4. **Integração com Spring Security:**
   - A classe `WebAuthenticationDetailsSource` é frequentemente usada em conjunto com um provedor de autenticação personalizado para incluir detalhes específicos da web durante o processo de autenticação.

## Classe `JwtFilter`

A classe `JwtFilter` implementa um filtro (`JwtFilter`) que é usado para validar tokens JWT em solicitações HTTP. O filtro estende a classe `OncePerRequestFilter`, garantindo que ele seja executado apenas uma vez por solicitação. 

```java
package com.apirest.projetospringsecurity.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Classe para validar o token */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    Claims claims = null;

    private String username = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().matches("/user/login|user/forgotPassword|/user/signup")) {
            filterChain.doFilter(request, response);

        } else {
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractAllClaims(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customerDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    new WebAuthenticationDetailsSource().buildDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    public Boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public Boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser(){
        return username;
    }

}
```

Principais pontos desse script:

1. **Anotação `@Component`:**
   - A classe é anotada com `@Component`, indicando que é um componente gerenciado pelo Spring. Isso permite que o Spring gerencie a injeção de dependência para esta classe.

2. **Injeção de Dependência:**
   - Há duas dependências injetadas por meio da anotação `@Autowired`: `JwtUtil` e `CustomerDetailsService`. A `JwtUtil` é utilizada para operações relacionadas a tokens JWT, enquanto a `CustomerDetailsService` é usada para carregar detalhes do usuário durante a autenticação.

3. **Atributos `claims` e `username`:**
   - Dois atributos (`claims` e `username`) são declarados na classe para armazenar informações extraídas do token JWT durante o processo de validação.

4. **Método `doFilterInternal`:**
   - Este método é invocado para cada solicitação HTTP e contém a lógica principal do filtro.
   - Verifica se a solicitação não está mapeada para endpoints específicos ("/user/login", "/user/forgotPassword", "/user/signup"). Se estiver, o filtro permite que a solicitação prossiga sem verificar o token.
   - Se a solicitação não está mapeada para os endpoints mencionados acima, o filtro extrai o token do cabeçalho de autorização e realiza a validação do token.
   - Se o token for válido, cria um `UsernamePasswordAuthenticationToken` e configura o contexto de segurança usando o `SecurityContextHolder`.
   - O método `filterChain.doFilter` é chamado para permitir que a solicitação continue seu fluxo através da cadeia de filtros.

5. **Métodos `isAdmin`, `isUser`, `getCurrentUser`:**
   - Esses métodos fornecem informações extraídas do token, como verificar se o usuário é um administrador (`isAdmin`), um usuário normal (`isUser`) e obter o nome de usuário atual (`getCurrentUser`).

6. **`WebAuthenticationDetailsSource`:**
   - É utilizado o `WebAuthenticationDetailsSource` para criar detalhes específicos da web (`WebAuthenticationDetails`) durante o processo de autenticação.

7. **Validação do Token:**
   - O filtro usa a `JwtUtil` para validar o token, garantindo que seja autêntico e que ainda não tenha expirado.

8. **Contexto de Segurança (`SecurityContextHolder`):**
   - O `UsernamePasswordAuthenticationToken` é configurado no contexto de segurança usando o `SecurityContextHolder`. Isso é essencial para que o Spring Security reconheça o usuário autenticado em solicitações subsequentes.

Em resumo, este filtro é responsável por validar tokens JWT em solicitações HTTP, garantindo que o usuário seja autenticado corretamente. Ele extrai informações do token para realizar verificações adicionais e configura o contexto de segurança do Spring Security.

# Autor
## Feito por: `Daniel Penelva de Andrade`