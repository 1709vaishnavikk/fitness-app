# 🏋️‍♀️ AI-Powered Fitness App – Microservices-Based Intelligent Workout Advisor

**AI-Powered Fitness App** is a full-stack, AI-integrated fitness application built on a robust **microservices architecture**. It intelligently delivers personalized recommendations based on user activity, profile data, and AI insights. Designed with performance, modularity, and real-world use cases in mind.

---

## 🔧 Stack Overview

* **Frontend**: React
* **Backend**: Spring Boot microservices
* **Security**: OAuth2 flow with Keycloak
* **Databases**: PostgreSQL (User Service), MongoDB (Activity & AI Engine)
* **Service Communication**: Feign Client & RabbitMQ
* **AI Engine**: Integrated with Google Gemini API
* **Infrastructure**: Eureka Discovery, Config Server, API Gateway

---

## ⚙️ Microservices

* **User Service**: Manages user profiles (age, height, weight, goal weight, water intake)
* **Activity Service**: Logs daily activities (type, duration, calorie burn)
* **AI Engine Service**: Consumes activity and user data to generate AI-based fitness recommendations

> AI suggestions are triggered automatically for each activity completed, ensuring personalized feedback for every session.

---

## 🔁 Communication & Messaging

* **Feign Client** – between User and Activity services
* **RabbitMQ** – to pass events from Activity to AI Engine

---

## 🔐 Authentication & Authorization

* Secure OAuth2 flow managed by **Keycloak**
* Token-based access control across services

---

## 🖼️ Screenshots

> ![WhatsApp Image 2025-07-15 at 21 02 57_c7b4f1c8](https://github.com/user-attachments/assets/a85f2d96-2b73-44ce-aad7-47796f905a9e)


> *(Add screenshots of profile, activity logs, and AI recommendations)*

---

## 🚀 How It Works

1. User logs an activity (e.g., running 30 minutes)
2. Activity service sends data (type, duration, calories) to RabbitMQ
3. AI Engine consumes the message, fetches user profile, and generates recommendations using Google Gemini
4. The result is pushed to the frontend as an AI-driven fitness tip

---

## 🛠️ Running the Project

### 🔹 Prerequisites

* Keycloak must be installed and running (for OAuth2)
* RabbitMQ instance must be available locally
* PostgreSQL and MongoDB databases must be set up

### 🔹 Run Locally (Recommended)

1. Clone the repo
2. Start Keycloak and RabbitMQ
3. Start each service via IDE (user, activity, ai-engine, eureka, config-server, api-gateway)
4. Run the React frontend

### 🐳 Docker Support

Each service includes a Dockerfile. You can use `docker-compose` to spin up all services.

```bash
# Build and start all services
docker-compose up --build
```

> ⚠️ Localhost setup is the most stable. Docker support is provided for containerization and future deployment.

---

## 📄 License

All rights reserved. This project is for educational and portfolio demonstration only.

---

## 👤 Author

**Vaishnavi Kshirsagar**
🔗 [LinkedIn](https://linkedin.com/in/your-profile)

> 🚀 *Smarter workouts. Personalized health. Powered by microservices and AI.*
