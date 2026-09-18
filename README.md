# Identity Service

Identity Service là một RESTful authentication/authorization service được xây dựng bằng **Spring Boot**, cung cấp các chức năng quản lý user, role, permission và xác thực bằng **JWT**.

## ✨ Features

- User registration
- Internal user creation cho staff
- Login bằng username/password
- JWT access token
- JWT introspection
- Refresh token
- Logout / token invalidation
- User management
- Role management
- Permission management
- Role-based authorization
- Permission-based authorization thông qua JWT scope
- BCrypt password hashing
- MySQL persistence
- Global exception handling
- CORS configuration

---

## 🛠 Tech Stack

| Technology | Version / Notes |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.1 |
| Spring Web MVC | REST API |
| Spring Data JPA | Persistence |
| Spring Security | Authentication / Authorization |
| OAuth2 Resource Server | JWT resource server |
| MySQL | Database |
| MapStruct | DTO ↔ Entity mapping |
| Lombok | Boilerplate reduction |
| Nimbus JOSE JWT | JWT signing / verification |
| Dotenv Java | Environment configuration |
| Maven | Build tool |

---

## 📁 Project Structure

```text
identity-service/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/ecommere/identity_service/
│   │   │   ├── configuration/
│   │   │   │   ├── ApplicationInitConfig.java
│   │   │   │   ├── CustomJwtDecoder.java
│   │   │   │   ├── JwtAuthenticationEntrypoint.java
│   │   │   │   └── SecurityConfig.java
│   │   │   │
│   │   │   ├── constant/
│   │   │   │   └── RoleDefine.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthenticationController.java
│   │   │   │   ├── InternalUserController.java
│   │   │   │   ├── PermissionController.java
│   │   │   │   ├── RoleController.java
│   │   │   │   └── UserController.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   ├── response/
│   │   │   │   └── ApiResponse.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Permission.java
│   │   │   │   └── InvalidateToken.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │
│   │   └── resources/
│   │       └── application.yaml
│   │
│   └── test/
│
├── .env
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

## 🏗 Architecture

Service sử dụng kiến trúc phân lớp:

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ├── Repository ──► MySQL
  │
  └── Mapper ──────► Entity / DTO

Security
  │
  └── JWT Decoder
          │
          ▼
     Authorization
     Role / Permission
```

### Main layers

**Controller**

Expose REST APIs cho authentication, user, role và permission.

**Service**

Chứa business logic như login, tạo user, refresh token, logout và quản lý role/permission.

**Repository**

Spring Data JPA repositories dùng để truy cập MySQL.

**Entity**

Các entity chính:

- `User`
- `Role`
- `Permission`
- `InvalidateToken`

**DTO**

Request/response models được sử dụng giữa client và API.

---

# ⚙️ Configuration

Application chạy với context path:

```text
/identity
```

Default port:

```text
8080
```

Do đó base URL khi chạy local là:

```text
http://localhost:8080/identity
```

### Environment variables

Tạo file `.env` hoặc cấu hình environment variables:

```env
DB_URL=jdbc:mysql://localhost:3306/user_db
USER_NAME=root
PASSWORD=your_database_password

SIGNER_KEY=your_long_random_jwt_secret
VALID=1
REFRESHABLE=120
```

### Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection string tới MySQL |
| `USER_NAME` | MySQL username |
| `PASSWORD` | MySQL password |
| `SIGNER_KEY` | Secret dùng để ký/verify JWT |
| `VALID` | Access token validity tính bằng phút |
| `REFRESHABLE` | Khoảng thời gian refresh token tính bằng giây |

> **Security:** Không commit password, JWT signer key hoặc các secret thật vào GitHub. Nếu secret đã từng được commit, hãy rotate/revoke secret đó trước khi publish repository.

---

# 🗄 Database

Service sử dụng:

```text
MySQL
```

Database mặc định được cấu hình trong project là:

```text
user_db
```

Tạo database:

```sql
CREATE DATABASE user_db;
```

Hibernate hiện được cấu hình:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

Điều này có nghĩa service **không tự động tạo/update database schema**.

Bạn cần chuẩn bị schema tương ứng trước khi chạy application.

Các domain chính:

```text
Users
Role
Permission
InvalidateToken
```

User và Role có quan hệ many-to-many.

Role và Permission cũng có quan hệ many-to-many.

---

# 🚀 Getting Started

## 1. Requirements

Cần cài:

- JDK 25
- Maven hoặc Maven Wrapper
- MySQL 8.x hoặc compatible version
- Git

Kiểm tra Java:

```bash
java -version
```

Kiểm tra Maven:

```bash
mvn -version
```

---

## 2. Clone repository

```bash
git clone https://github.com/HUU7911/identity-service.git
cd identity-service
```

---

## 3. Configure environment

Tạo `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/user_db
USER_NAME=root
PASSWORD=your_password
SIGNER_KEY=your_random_secret
VALID=1
REFRESHABLE=120
```

Không sử dụng secret thật được commit trong repository.

---

## 4. Start MySQL

Đảm bảo MySQL đang chạy và database tồn tại:

```sql
CREATE DATABASE user_db;
```

Sau đó import schema của project nếu bạn có file SQL tương ứng.

---

## 5. Run application

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

Hoặc dùng Maven:

```bash
mvn spring-boot:run
```

Application chạy tại:

```text
http://localhost:8080/identity
```

---

# 🔐 Authentication

Authentication sử dụng JWT với thuật toán:

```text
HS256
```

JWT chứa các thông tin chính:

```json
{
  "sub": "user-id",
  "iss": "huumod.com",
  "iat": "...",
  "exp": "...",
  "jti": "...",
  "scope": "ROLE_USER permission_a permission_b"
}
```

Scope được tạo từ:

```text
ROLE_<ROLE_NAME>
<PERMISSION_NAME>
```

Ví dụ:

```text
ROLE_ADMIN USER_READ USER_WRITE
```

Spring Security sử dụng JWT scope để xác định authorities.

---

# 👤 User API

Base path:

```text
/identity/users
```

## Create user

```http
POST /identity/users/create
Content-Type: application/json
```

Request:

```json
{
  "username": "john",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "birthDate": "2000-01-01"
}
```

User được tạo thông qua service với role mặc định:

```text
USER
```

---

## Get user

```http
GET /identity/users/{id}
Authorization: Bearer <access-token>
```

---

## Get all users

```http
GET /identity/users
Authorization: Bearer <admin-token>
```

Endpoint này yêu cầu:

```text
ROLE_ADMIN
```

---

## Delete user

```http
DELETE /identity/users/delete/{id}
Authorization: Bearer <access-token>
```

---

## Get current user

```http
GET /identity/users/myInfor
Authorization: Bearer <access-token>
```

---

# 🔑 Authentication API

Base path:

```text
/identity/auth
```

## Login

```http
POST /identity/auth/login
Content-Type: application/json
```

Request:

```json
{
  "username": "john",
  "password": "password123"
}
```

Response:

```json
{
  "results": {
    "token": "<JWT>",
    "authenticated": true
  }
}
```

---

## Introspect token

```http
POST /identity/auth/introspect
Content-Type: application/json
```

Request:

```json
{
  "token": "<JWT>"
}
```

Response:

```json
{
  "results": {
    "valid": true
  }
}
```

---

## Refresh token

```http
POST /identity/auth/refresh
Content-Type: application/json
```

Request:

```json
{
  "token": "<JWT>"
}
```

Service sẽ invalidate token cũ và tạo JWT mới.

---

## Logout

```http
POST /identity/auth/logout
Content-Type: application/json
```

Request:

```json
{
  "token": "<JWT>"
}
```

JWT ID (`jti`) được lưu vào `InvalidateToken` để invalidate token.

---

# 👥 Internal User API

Endpoint:

```http
POST /identity/internal/users/create
```

Endpoint này yêu cầu:

```text
ROLE_ADMIN
```

Request:

```json
{
  "username": "staff01",
  "password": "password123",
  "firstName": "Staff",
  "lastName": "User",
  "email": "staff@example.com",
  "birthDate": "2000-01-01"
}
```

User được tạo thông qua internal flow với role mặc định:

```text
STAFF
```

---

# 🛡️ Role API

Base path:

```text
/identity/roles
```

## Create role

```http
POST /identity/roles/create
Authorization: Bearer <access-token>
Content-Type: application/json
```

Request:

```json
{
  "name": "ADMIN",
  "description": "System administrator",
  "permissions": [
    "USER_READ",
    "USER_WRITE"
  ]
}
```

---

## Get all roles

```http
GET /identity/roles
Authorization: Bearer <access-token>
```

---

## Delete role

```http
DELETE /identity/roles/delete/{role}
Authorization: Bearer <access-token>
```

---

# 🔒 Permission API

Base path:

```text
/identity/permission
```

## Create permission

```http
POST /identity/permission/create
Authorization: Bearer <access-token>
Content-Type: application/json
```

Request:

```json
{
  "name": "USER_READ",
  "description": "Read user information"
}
```

---

## Get all permissions

```http
GET /identity/permission
Authorization: Bearer <access-token>
```

---

## Delete permission

```http
DELETE /identity/permission/delete/{permission}
Authorization: Bearer <access-token>
```

---

# 🔐 Authorization

Spring Security được cấu hình với:

```java
@EnableMethodSecurity
```

JWT authorities được lấy trực tiếp từ scope:

```text
ROLE_ADMIN
ROLE_USER
USER_READ
USER_WRITE
...
```

Một số endpoint sử dụng role authorization:

```java
.hasRole("ADMIN")
```

Service cũng sử dụng:

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

# 🌐 CORS

Current configuration cho phép các origin development:

```text
http://localhost:5173
http://192.168.1.10:3000
```

Allowed:

```text
Headers: *
Methods: *
Credentials: true
```

Khi deploy production, nên thay các origin development bằng domain frontend thực tế.

---

# 📦 Build

Build project:

```bash
./mvnw clean package
```

JAR được tạo trong:

```text
target/
```

Chạy JAR:

```bash
java -jar target/identity-service-0.0.1-SNAPSHOT.jar
```

---

# 🧪 Testing

Project sử dụng Spring Boot test dependencies.

Chạy test:

```bash
./mvnw test
```

Hoặc:

```bash
mvn test
```

---

# 📮 API Testing

Có thể sử dụng:

- Postman
- Insomnia
- cURL
- Bruno

Ví dụ login bằng cURL:

```bash
curl -X POST http://localhost:8080/identity/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

Sau khi nhận JWT:

```bash
curl http://localhost:8080/identity/users \
  -H "Authorization: Bearer <JWT>"
```

---

# ⚠️ Notes

### 1. Database schema

`ddl-auto` đang là:

```yaml
ddl-auto: none
```

Service không tự tạo database tables.

### 2. JWT secret

`SIGNER_KEY` phải được giữ bí mật và đủ mạnh.

Không commit `.env` chứa secret thật.

### 3. Token invalidation

Logout và refresh lưu JWT ID vào `InvalidateToken`. Cần đảm bảo `CustomJwtDecoder` kiểm tra token đã bị invalidate trước khi cho phép authentication.

### 4. Production CORS

CORS hiện tại chủ yếu phục vụ development. Khi deploy production cần cập nhật allowed origins.

### 5. Password response

`UserResponse` hiện chứa field `password`. Với production API, không nên trả password/hash password về client. Nên loại bỏ field này khỏi response DTO.

### 6. Endpoint typo cần kiểm tra

Trong `SecurityConfig`, public matcher hiện đang khai báo:

```text
/users/reate
```

Trong `UserController`, endpoint thực tế là:

```text
/users/create
```

Nếu muốn user registration không cần authentication, matcher nên được sửa thành:

```text
/users/create
```

### 7. Refresh endpoint

`/auth/refresh` hiện không nằm trong danh sách public endpoints của `SecurityConfig`, vì vậy request refresh có thể yêu cầu authentication trước khi đi vào controller. Nếu thiết kế refresh token endpoint là public, cần thêm endpoint này vào public matchers.

---

# 🔄 Authentication Flow

```text
┌──────────┐
│  Client  │
└────┬─────┘
     │ username + password
     ▼
┌──────────────────┐
│ /auth/login      │
└────────┬─────────┘
         │
         ▼
   Check User
         │
         ▼
   BCrypt verify
         │
         ▼
    Generate JWT
         │
         ▼
      Client
         │
         │ Authorization: Bearer JWT
         ▼
┌──────────────────┐
│ Protected API    │
└────────┬─────────┘
         │
         ▼
   JWT Decoder
         │
         ▼
 Role / Permission
 validation
         │
         ▼
      Response
```

---

# 🔄 Refresh Flow

```text
Client
  │
  │ refresh token
  ▼
/auth/refresh
  │
  ▼
Verify JWT
  │
  ▼
Invalidate old JTI
  │
  ▼
Load User
  │
  ▼
Generate new JWT
  │
  ▼
Client
```

---

# 🚪 Logout Flow

```text
Client
  │
  │ JWT
  ▼
/auth/logout
  │
  ▼
Verify JWT
  │
  ▼
Extract JTI
  │
  ▼
Save JTI → InvalidateToken
  │
  ▼
Token invalidated
```

---

# 🧑‍💻 Development

Một số package chính:

```text
configuration
constant
controller
dto
entity
exception
mapper
repository
service
```

Business logic nên được đặt trong `service`, không nên đặt trực tiếp trong controller.

---

# 📄 License

License chưa được định nghĩa trong project hiện tại.

Nếu project được public trên GitHub, nên thêm một license phù hợp, ví dụ MIT, Apache-2.0 hoặc license riêng của project.

---

# 👨‍💻 Author

**HUU7911**

Repository:

```text
https://github.com/HUU7911/identity-service
```
