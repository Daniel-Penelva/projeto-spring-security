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