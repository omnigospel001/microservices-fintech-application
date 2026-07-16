# FinTech Microservices Application

A production-grade, peer-to-peer **FinTech banking platform** built on a robust microservices architecture. Users register to receive auto-generated 10-digit account numbers, then perform deposits, withdrawals, and real-time transfers with layered email notifications, distributed tracing, and full settlement tracking.

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-green.svg)](https://spring.io/projects/spring-cloud)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-minikube-326CE5.svg)](https://kubernetes.io/)
[![Kafka](https://img.shields.io/badge/Kafka-KRaft-231F20.svg)](https://kafka.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Features](#features)
4. [Technology Stack](#technology-stack)
5. [Microservices Breakdown](#microservices-breakdown)
6. [Communication Patterns](#communication-patterns)
7. [Security Architecture](#security-architecture)
8. [Database Design](#database-design)
9. [Kafka Topics & Events](#kafka-topics--events)
10. [Project Structure](#project-structure)
11. [Prerequisites](#prerequisites)
12. [Local Development (Docker Compose)](#local-development-docker-compose)
13. [Kubernetes Deployment (minikube)](#kubernetes-deployment-minikube)
14. [API Reference](#api-reference)
15. [Configuration](#configuration)
16. [Environment Variables](#environment-variables)
17. [Troubleshooting](#troubleshooting)
18. [License](#license)

---

## Project Overview

This application simulates the foundational operations of a real-world multi-user FinTech / banking system:

- **User Registration** → Auto-generated 10-digit account number
- **Deposit** → Funds credited to user balance
- **Withdrawal** → Funds debited from user balance
- **Transfer** → Peer-to-peer fund movement between registered users
- **Settlement** → Transaction lifecycle tracking (Pending → Settled)
- **Notifications** → Real-time Deposit, Withdrawal, Transfer, and Settlement email alerts via Gmail SMTP
- **Observability** → Distributed tracing with Zipkin across all services

Each microservice is independently deployable, owns its own data store, and communicates via synchronous (Feign) and asynchronous (Kafka) patterns.

---

## Architecture

```
                    ┌──────────────────────────────────────┐
                    │         Ingress / API Gateway         │
                    │     Spring Cloud Gateway (:8222)      │
                    │         JWT Global Filter             │
                    └──────────────┬───────────────────────┘
                                   │
         ┌─────────────────────────┼─────────────────────────┐
         │                         │                         │
    ┌────▼─────┐            ┌──────▼──────┐          ┌──────▼──────┐
    │ Config   │            │  Discovery  │          │   Zipkin    │
    │ Server   │            │   Server    │          │  (Tracing)  │
    │ (:8888)  │            │   (:8761)   │          │  (:9411)    │
    └──────────┘            └─────────────┘          └─────────────┘

    ┌─────────────────────────────────────────────────────────────┐
    │                      Business Services                       │
    ├──────────┬──────────┬──────────┬──────────┬────────────────┤
    │  User    │ Deposit  │Withdrawal│ Transfer │  Settlement    │
    │(:8091)   │(:8070)   │(:9029)   │(:8475)   │  (:8060)       │
    │ MongoDB  │PostgreSQL│   ---    │PostgreSQL│  PostgreSQL    │
    └──────────┴──────────┴──────────┴──────────┴────────────────┘
         │           │           │           │            │
         └───────────┴─────┬─────┴───────────┴────────────┘
                           │
              ┌────────────▼────────────┐
              │   Notification Service  │
              │       (:8040)           │
              │  Kafka Consumer + SMTP  │
              └─────────────────────────┘

    ┌─────────────────────────────────────────────────────────────┐
    │                     Data & Messaging Layer                   │
    ├────────────┬────────────┬────────────────┬──────────────────┤
    │ PostgreSQL │  MongoDB   │  Kafka (KRaft) │     Ingress      │
    │ ( deposit, │  ( user )  │ (Notifications)│   (fintech.local)│
    │  transfer, │            │                │                  │
    │  settlement│            │                │                  │
    └────────────┴────────────┴────────────────┴──────────────────┘

    ┌─────────────────────────────────────────────────────────────┐
    │                     Management UI Layer                      │
    ├────────────────────────────┬────────────────────────────────┤
    │         pgAdmin            │         Mongo Express          │
    │      (:5050 / :30050)      │       (:8081 / :30081)         │
    └────────────────────────────┴────────────────────────────────┘
```

---

## Features

| Feature | Description |
|---------|-------------|
| **Auto Account Generation** | Every registered user receives a unique, random 10-digit account number |
| **JWT Authentication** | Stateless auth with HMAC-SHA256 tokens (10h expiry); enforced at Gateway |
| **Balance Ledger** | Centralized deposit table acts as the debit/credit ledger for all transactions |
| **P2P Transfers** | Users can transfer funds to any other user by account number; sender/receiver both notified |
| **Settlement Tracking** | Every financial event (deposit, withdrawal, transfer) is tracked through its lifecycle |
| **Email Notifications** | Layered alerts: Deposit Credit, Withdrawal Debit, Transfer Credit/Debit, Settlement Status |
| **Distributed Tracing** | Zipkin integration across all services via Micrometer Brave |
| **Centralized Config** | Spring Cloud Config Server serves all service configurations from Git / filesystem |
| **Service Discovery** | Netflix Eureka enables dynamic service registration and client-side load balancing |
| **Docker Support** | One-command full-stack launch via Docker Compose |
| **Kubernetes Support** | Complete raw YAML manifests for minikube deployment |

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.5 |
| **Cloud Platform** | Spring Cloud 2025.0.0 |
| **API Gateway** | Spring Cloud Gateway (WebFlux) |
| **Service Discovery** | Netflix Eureka Server / Client |
| **Config Management** | Spring Cloud Config Server (Native Backend) |
| **Inter-Service Calls** | OpenFeign |
| **Messaging** | Apache Kafka 7.4.0 (KRaft mode — no ZooKeeper) |
| **Relational DB** | PostgreSQL (deposit, transfer, settlement services) |
| **Document DB** | MongoDB (user service) |
| **Auth** | Spring Security + JWT (jjwt 0.12.6) |
| **Observability** | Zipkin + Micrometer Tracing Brave |
| **Email** | JavaMailSender (Gmail SMTP) |
| **Build Tool** | Maven 3.9 |
| **Containerization** | Docker + Docker Compose |
| **Orchestration** | Kubernetes (minikube) |
| **Utilities** | Lombok, Jakarta Validation |

---

## Microservices Breakdown

### Infrastructure Services

| Service | Port | Role | Technology |
|---------|------|------|------------|
| **Config Server** | `8888` | Centralized configuration from `classpath:/configurations` | Spring Cloud Config |
| **Discovery Server** | `8761` | Eureka service registry; disables self-registration | Netflix Eureka |
| **Gateway Server** | `8222` | Routes all requests; enforces JWT auth (skips `/user/save`, `/user/login`) | Spring Cloud Gateway |

### Business Services

| Service | Port | DB | Role |
|---------|------|-----|------|
| **User Service** | `8091` | MongoDB | Registration, login, JWT issuance, profile lookup |
| **Deposit Service** | `8070` | PostgreSQL (`deposit` DB) | Fund deposits, balance storage, internal debit/credit endpoints for withdrawals & transfers |
| **Withdrawal Service** | `9029` | None (pure orchestration) | Orchestrates withdrawals via Feign → Deposit Service; sends Kafka event |
| **Transfer Service** | `8475` | PostgreSQL (`transfer` DB) | P2P transfer orchestration (debit sender, credit receiver, persist `Transactions`) |
| **Notification Service** | `8040` | None (Kafka consumer) | Consumes all Kafka topics; sends HTML email alerts via Gmail SMTP |
| **Settlement Service** | `8060` | PostgreSQL (`settlement` DB) | Tracks every financial event lifecycle: `PENDING` → `SETTLED` / `FAILED` / `REVERSED` |

### Data & UI Layer

| Component | Port | Role |
|-----------|------|------|
| **PostgreSQL** | `5432` | Stores `deposit`, `transfer`, and `settlement` databases |
| **MongoDB** | `27017` | Stores `users` collection |
| **Kafka** | `9092` / `29092` | Event bus for deposit, withdrawal, transfer, settlement notifications |
| **Zipkin** | `9411` | Distributed trace collection and visualization |
| **pgAdmin** | `5050` | PostgreSQL management UI |
| **Mongo Express** | `8081` | MongoDB management UI |

---

## Communication Patterns

### Synchronous (OpenFeign)

```
deposit-service      ──Feign──► user-service
withdrawal-service   ──Feign──► deposit-service
withdrawal-service   ──Feign──► user-service
transfer-service     ──Feign──► deposit-service
transfer-service     ──Feign──► user-service
settlement-service   ──Feign──► user-service
```

Every Feign client method returns `Optional<T>`. Callers use `.orElseThrow(() -> new NotFoundException(...))`.

### Asynchronous (Kafka)

| Producer | Topic | Consumer(s) | Group ID |
|----------|-------|-------------|----------|
| Deposit Service | `deposit-topic` | Notification Service | `deposit-email-group` |
| | | Settlement Service | `settlement-deposit-group` |
| Withdrawal Service | `withdrawal-topic` | Notification Service | `withdrawal-email-group` |
| | | Settlement Service | `settlement-withdrawal-group` |
| Transfer Service | `transfer-topic` | Notification Service | `transfer-receiver-group` |
| | | Notification Service | `transfer-sender-group` |
| | | Settlement Service | `settlement-transfer-group` |
| Settlement Service | `settlement-topic` | Notification Service | `settlement-email-group` |

---

## Security Architecture

### JWT Token Flow

```
┌──────────┐        ┌──────────────┐        ┌─────────────┐
│  Client  │ ─────► │ Gateway (:8222) │ ──► │ User Service │
│          │        │ (validate JWT)  │     │ (issue JWT)  │
└──────────┘        └──────────────┘        └─────────────┘
```

1. **Token Generation** (`user-service`): Issued on successful login. Signed with HMAC-SHA256. Subject = user email. Expiry = 10 hours.
2. **Token Validation** (`gateway-server`): GlobalFilter validates all requests except `/user/save` and `/user/login`.
3. **User Identity Forwarding**: After validation, the Gateway adds `X-User-Email` and `X-User-Id` headers for downstream services.
4. **Password Hashing**: BCryptPasswordEncoder in `user-service`.

### Configured Secret

The JWT secret is a 64-character hex string stored in `config-[AWS_SECRET_KEY_REDACTED]/gateway-server.yml` and `config-[AWS_SECRET_KEY_REDACTED]/user-service.yml`:
```
404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

---

## Database Design

### PostgreSQL (`deposit` database)

**Table: `deposit`**
| Column | Type | Constraints |
|--------|------|-------------|
| `id` | integer (identity) | PK |
| `deposit_amount` | numeric(38,2) | |
| `user_id` | varchar(255) | |
| `create_date` | timestamp | not null, auto-populated |

### PostgreSQL (`transfer` database)

**Table: `transactions`**
| Column | Type |
|--------|------|
| `id` | integer (identity) |
| `sender_first_name` | varchar(255) |
| `sender_last_name` | varchar(255) |
| `sender_email` | varchar(255) |
| `sender_account_number` | bigint |
| `receiver_first_name` | varchar(255) |
| `receiver_last_name` | varchar(255) |
| `receiver_email` | varchar(255) |
| `receiver_account_number` | bigint |
| `transfer_amount` | numeric(38,2) |
| `transfer_date` | timestamp |

### PostgreSQL (`settlement` database)

**Table: `settlements`**
| Column | Type | Constraints |
|--------|------|-------------|
| `id` | integer (identity) | PK |
| `transaction_type` | varchar(20) | enum: DEPOSIT, WITHDRAWAL, TRANSFER |
| `reference_id` | varchar(100) | not null |
| `user_id` | varchar(100) | not null |
| `amount` | numeric(19,2) | not null |
| `status` | varchar(20) | enum: PENDING, SETTLED, FAILED, REVERSED |
| `created_at` | timestamp | not null |
| `settled_at` | timestamp | |

### MongoDB (`user` database)

**Collection: `users`**
| Field | Type |
|-------|------|
| `_id` | ObjectId |
| `firstName` | string |
| `lastName` | string |
| `email` | string |
| `password` | string (BCrypt) |
| `accountNumber` | long (10-digit, unique) |

---

## Kafka Topics & Events

| Topic | Event Payload | When Produced |
|-------|--------------|---------------|
| `deposit-topic` | `DepositNotificationRequest` | After successful deposit |
| `withdrawal-topic` | `WithdrawalNotificationRequest` | After successful withdrawal |
| `transfer-topic` | `TransferNotificationRequest` | After successful transfer |
| `settlement-topic` | `SettlementNotificationRequest` | When settlement status changes to `SETTLED` |

Kafka uses **KRaft mode** (no ZooKeeper) and runs as a single-node cluster for local development.

---

## Project Structure

```
fintech-microservices/
├── config-server/                  # Centralized configuration
│   └── src/main/resources/
│       └── configurations/
│           ├── application.yml
│           ├── gateway-server.yml
│           ├── discovery-server.yml
│           ├── user-service.yml
│           ├── deposit-service.yml
│           ├── withdrawal-service.yml
│           ├── transfer-service.yml
│           ├── notification-service.yml
│           └── settlement-service.yml
├── discovery-server/               # Eureka service registry
├── gateway-server/                 # Spring Cloud Gateway + JWT filter
├── user-service/                   # MongoDB + Auth
├── deposit-service/                # PostgreSQL + Kafka Producer
├── withdrawal-service/             # Feign client + Kafka Producer
├── transfer-service/               # PostgreSQL + Feign + Kafka Producer
├── notification-service/           # Kafka Consumer + Gmail SMTP
├── settlement-service/             # PostgreSQL + Kafka C/P + Settlement logic
├── k8s/                            # Kubernetes raw YAML manifests
│   ├── 00-namespace.yml
│   ├── 01-secrets.yml
│   ├── 02-configmap-postgres-init.yml
│   ├── 10-postgresql.yml
│   ├── 11-mongodb.yml
│   ├── 12-kafka.yml
│   ├── 13-zipkin.yml
│   ├── 14-pgadmin.yml
│   ├── 15-mongo-express.yml
│   ├── 20-config-server.yml
│   ├── 21-discovery-server.yml
│   ├── 22-gateway-server.yml
│   ├── 30-user-service.yml
│   ├── 31-deposit-service.yml
│   ├── 32-withdrawal-service.yml
│   ├── 33-transfer-service.yml
│   ├── 34-notification-service.yml
│   ├── 35-settlement-service.yml
│   ├── 40-ingress.yml
│   ├── build-images.sh / .ps1
│   ├── deploy.sh / .ps1
│   └── delete-all.sh / .ps1
├── docker-compose.yml              # Full Docker Compose stack
├── Dockerfile                      # Multi-stage parameterized build
├── init-multiple-dbs.sql           # PostgreSQL init script
├── .env                            # Local env variables
└── README.md                       # This file
```

---

## Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker & Docker Desktop**
- **Docker Compose** (v2+)
- **minikube** (for K8s deployment)
- **kubectl**
- **Git Bash / PowerShell / WSL**

---

## Local Development (Docker Compose)

### 1. Build & Run

```bash
# Navigate to project root
cd fintech-microservices

# Start the entire stack
docker compose up --build -d
```

### 2. Verify Services

```bash
# Check all containers
docker ps

# View logs
docker logs -f config-server
docker logs -f gateway-server
docker logs -f user-service
```

### 3. Access Points

| Service | URL |
|---------|-----|
| Gateway | `http://localhost:8222` |
| Eureka Dashboard | `http://localhost:8761` |
| Config Server | `http://localhost:8888` |
| pgAdmin | `http://localhost:5050` |
| Mongo Express | `http://localhost:8081` |
| Zipkin | `http://localhost:9411` |

### 4. Stop Everything

```bash
docker compose down -v
```

> **Note:** Use `-v` to wipe volumes (and databases) for a completely fresh restart.

---

## Kubernetes Deployment (minikube)

### 1. Start minikube

```bash
minikube start --driver=docker --memory=6144 --cpus=4
minikube addons enable ingress
```

### 2. Build Images (inside minikube's Docker daemon)

**PowerShell:**
```powershell
cd k8s
.\build-images.ps1
```

**Bash:**
```bash
cd k8s
./build-images.sh
```

### 3. Deploy All Manifests

**PowerShell:**
```powershell
.\deploy.ps1
```

**Bash:**
```bash
./deploy.sh
```

The script deploys in dependency order:
1. Namespace, Secrets, ConfigMap
2. Data Layer (PostgreSQL, MongoDB, Kafka, Zipkin)
3. UI Tools (pgAdmin, Mongo Express)
4. Platform Layer (Config Server, Discovery Server, Gateway)
5. Business Services (User, Deposit, Withdrawal, Transfer, Notification, Settlement)
6. Ingress

### 4. Add Host Entry

**Windows (Admin PowerShell):**
```powershell
Add-Content -Path "C:\Windows\System32\drivers\etc\hosts" -Value "$(minikube ip) fintech.local"
```

**Linux / Mac:**
```bash
sudo sh -c "echo $(minikube ip) fintech.local >> /etc/hosts"
```

### 5. Verify

```bash
kubectl get pods -n fintech -w
```

### 6. Access Points

| Service | URL / Command |
|---------|---------------|
| Gateway (Ingress) | `http://fintech.local` |
| Eureka Dashboard | `minikube service discovery-server -n fintech` |
| pgAdmin | `minikube service pgadmin -n fintech` |
| Mongo Express | `minikube service mongo-express -n fintech` |
| Zipkin | `minikube service zipkin -n fintech` |

### 7. Teardown

```powershell
.\delete-all.ps1    # PowerShell
./delete-all.sh      # Bash
```

---

## API Reference

All API calls go through the **Gateway** (`http://localhost:8222` or `http://fintech.local` in K8s). The only public endpoints that skip JWT are **User Registration** and **Login**.

### Authentication

```http
POST /user/save
Content-Type: application/json

{
  "firstname": "John",
  "lastname": "Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

```http
POST /user/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "secret123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Authenticated Requests:**
```http
GET /user/{id}
Authorization: Bearer <token>
```

### Deposit

```http
POST /deposit
Authorization: Bearer <token>
Content-Type: application/json

{
  "depositAmount": 500.00,
  "userId": "<mongo-user-id>"
}
```

```http
GET /deposit/balance/{userId}
Authorization: Bearer <token>
```

### Withdrawal

```http
PUT /withdraw
Authorization: Bearer <token>
Content-Type: application/json

{
  "withdrawalAmount": 100.00,
  "depositId": 1,
  "userId": "<mongo-user-id>"
}
```

### Transfer

```http
PUT /transfer
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountNumber": 7798452110,
  "transferAmount": 200.00,
  "depositId": 1,
  "userId": "<mongo-user-id>"
}
```

### Settlement

```http
GET /settlement
Authorization: Bearer <token>

GET /settlement/{id}
GET /settlement/user/{userId}
GET /settlement/status/PENDING

POST /settlement/process/{id}      # Manually settle a pending item
POST /settlement/batch             # Batch process all pending settlements
```

---

## Configuration

All service configurations are managed by the **Config Server** and stored in:

```
config-server/src/main/resources/configurations/
```

Each service has a dedicated `{service-name}.yml` file containing:
- Server port
- Database connection (datasource / MongoDB)
- JPA / Hibernate settings (`ddl-auto: update`)
- Kafka producer/consumer settings
- JWT secret (shared)
- Email (SMTP) configuration

The Config Server uses a **native filesystem backend** (`spring.profiles.active: native`).

---

## Environment Variables

### Docker Compose Overrides

The `docker-compose.yml` passes the following environment variables to containerized services:

```yaml
CONFIG_SERVER_URI: optional:configserver:http://config-server:8888
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE: http://discovery-server:8761/eureka
EUREKA_INSTANCE_HOSTNAME: <service-name>
EUREKA_INSTANCE_PREFER_IP_ADDRESS: "true"
SPRING_DATASOURCE_URL: jdbc:postgresql://ms_pg_sql:5432/<db>
SPRING_DATA_MONGODB_HOST: mongodb
SPRING_KAFKA_BOOTSTRAP_SERVERS: ms_kafka_ms:29092
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS: ms_kafka_ms:29092
SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS: ms_kafka_ms:29092
```

### Kubernetes

In K8s, these same values are injected via manifest `env` blocks, with credentials sourced from the `fintech-secrets` Secret.

---

## Troubleshooting

### PostgreSQL "null value in column create_date"

**Cause:** Deposit and Transfer services shared the same database with `ddl-auto: update`, causing schema corruption.

**Fix:** Each service now uses its own isolated database (`deposit`, `transfer`, `settlement`).

```bash
docker compose down -v
docker compose up --build -d
```

### Kafka Connection Refused

**Cause:** Services inside K8s try to connect to `localhost:9092`.

**Fix:** Ensure `SPRING_KAFKA_BOOTSTRAP_SERVERS` is set to `kafka:29092` (Docker) or `kafka:29092` (K8s).

### Eureka Shows No Instances

**Cause:** Services register with hostname instead of IP, which is unresolvable in K8s.

**Fix:** `EUREKA_INSTANCE_PREFER_IP_ADDRESS=true` is set in all K8s manifests.

### Config Server Not Found

**Cause:** Services start before Config Server is ready.

**Fix:** K8s `depends_on` with `condition: service_healthy` ensures Config Server is up before dependents. In Docker Compose, healthchecks enforce the same ordering.

### Images Not Found in K8s

**Cause:** Images were built in host Docker, not minikube's Docker daemon.

**Fix:** Run `eval $(minikube docker-env)` before `docker build`.

---

## License

This project is licensed under the MIT License.

---

**Built with the real-world multi-user FinTech application in mind.**
