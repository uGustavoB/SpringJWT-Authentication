
# 🔐 Spring JWT Authentication API

Uma API REST segura para autenticação e gerenciamento de usuários, desenvolvida em Spring Boot com JWT. Ideal para quem quer aprender ou implementar autenticação robusta em projetos Java.

![Badge](https://img.shields.io/badge/Spring%20Boot-3.2.0-green) ![Badge](https://img.shields.io/badge/Java-23-blue) ![Badge](https://img.shields.io/badge/Swagger-3.0-success)

## ✨ Funcionalidades Principais
- **Autenticação JWT** com tokens expiráveis
- **Controle de Acesso Baseado em Roles** (ROLE_USER, ROLE_ADMIN)
- CRUD completo de usuários
- Validação de dados com mensagens customizadas
- Documentação interativa via OpenAPI 3
- Proteção contra operações não autorizadas

## 🛠 Tecnologias Utilizadas
- **Spring Boot 3**
- Spring Security 6
- JSON Web Tokens (JWT)
- Lombok
- Hibernate/JPA
- PostgreSQL
- Swagger/OpenAPI 3

## 🚀 Começando

### Pré-requisitos
- Java 17+
- Maven
- PostgreSQL

### Instalação
1. Clone o repositório:
```bash
git clone https://github.com/ugustavob/SpringJWT-Authentication.git
```
2. Configure o banco de dados:
```
CREATE DATABASE springjwt;

create table roles
(
    role    varchar(50) not null,
    user_id uuid        not null
        constraint fk_user
            references users
            on delete cascade
);

create table users
(
    id       uuid default gen_random_uuid() not null
        primary key,
    name     varchar(255)                   not null,
    email    varchar(255)                   not null
        unique,
    password varchar(255)                   not null
);
```
3. 3.  Crie um arquivo  `.env`  na raiz do projeto:
```
spring.application.name=SpringJWT-Authentication  
server.port=8080  
spring.datasource.url=jdbc:postgresql:///springjwt  
spring.datasource.driver-class-name=org.postgresql.Driver  
spring.datasource.username=postgres  
spring.datasource.password=postgres  
api.security.token.secret=secret-key
```
4.  Execute a aplicação:
```
mvn spring-boot:run
```

## 📚 Documentação da API

Acesse após iniciar a aplicação:

-   Swagger UI:  `http://localhost:8080/swagger-ui/index.html#/`
    
-   OpenAPI Spec:  `http://localhost:8080/v3/api-docs`
    

### Endpoints Principais
Autenticação
| Método | Endpoint | Descrição |
|--|--|--|
| POST | /auth/login | Autentica usuário |
| POST | /auth/register| Cria novo usuário |

Usuários
| Método | Endpoint | Descrição | Acesso |
|--|--|--|--|
| GET| /users/me | Obter usuário autenticado | ROLE_USER |
| PUT| /users/me | Editar usuário autenticado | ROLE_USER |
| GET| /users/ | Ver todos os usuários | ROLE_ADMIN |
| DELETE| /users/{uuid} | Deletar usuário | ROLE_ADMIN |
| POST| /users/{uuid}/roles/ | Atribuir role ao usuário | ROLE_ADMIN |

## 📄 Licença

MIT License - veja  [LICENSE](https://github.com/uGustavoB/SpringJWT-Authentication?tab=MIT-1-ov-file)  para detalhes.

----------

Feito por Gustavo Gabriel.
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue)](https://www.linkedin.com/in/gustavobatistaa/)
