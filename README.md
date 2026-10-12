# 🍔 Online Food Ordering System

A full-stack, enterprise-grade Online Food Ordering System built with **Spring Boot**, **Spring Security**, **Spring Data JPA**, **Thymeleaf**, **Bootstrap 5**, and **TiDB Cloud (MySQL)**.

---

## 🌐 Live Deployment & Repository Links

- **🚀 Live Deployed App:** [https://food-ordering-backend-zb9o.onrender.com/](https://food-ordering-backend-zb9o.onrender.com/)
- **📂 Frontend Repository:** [food-ordering-frontend](https://github.com/girivardhan-pasupuleti/food-ordering-frontend) *(UI Templates & Static Assets)*
- **⚙️ Backend Repository:** [food-ordering-backend](https://github.com/girivardhan-pasupuleti/food-ordering-backend) *(Spring Boot Backend, REST/MVC, TiDB Cloud)*
- **☁️ Cloud Hosting:** [Render](https://render.com/)
- **🗄️ Cloud Database:** [TiDB Cloud](https://tidbcloud.com/) (Distributed MySQL-compatible Cloud Database)

---

## 👥 Demo Login Credentials

The database comes pre-seeded with test accounts for all user roles:

| Role | Email | Password | Dashboard Features |
|------|-------|----------|-------------------|
| **Admin** | `admin@food.com` | `admin123` | View total users, orders, food items, categories, and manage system resources |
| **Restaurant** | `restaurant@food.com` | `restaurant123` | Add/manage menu items, track real-time orders, update order status |
| **Customer** | `customer@food.com` | `customer123` | Browse menus, add items to cart, checkout with multiple payment methods, order history |

---

## 🚀 Key Features

### 👤 Customer Experience
- **Interactive Landing Page:** Explore available categories and featured dishes.
- **Cart & Checkout:** Real-time quantity management and total calculations.
- **Order Tracking:** View order status history (`PENDING`, `CONFIRMED`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`).
- **Flexible Payments:** Support for Cash on Delivery, UPI, and Cards.

### 🏪 Restaurant Management
- **Menu Management:** Add, edit, and organize dishes by category with pricing and image URLs.
- **Order Management:** View incoming orders and transition order lifecycle statuses.
- **Dashboard Metrics:** Track active menu items and processing orders.

### 🛡️ Administrative Portal
- **System Overview:** High-level metrics for registered customers, restaurant partners, and total orders.
- **Category & User Governance:** Manage food categories and monitor user accounts.

### 🔒 Security & Architecture
- **Role-Based Access Control (RBAC):** Custom authorization rules for `ROLE_ADMIN`, `ROLE_RESTAURANT`, and `ROLE_CUSTOMER`.
- **Spring Security Integration:** BCrypt password encryption, session handling, and protected POST logout endpoints.
- **Cloud-Ready Configuration:** Dynamic environment variable bindings for port (`${PORT}`) and database credentials.

---

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot 4.x
- **Security:** Spring Security (RBAC, BCrypt)
- **Persistence:** Spring Data JPA, Hibernate ORM
- **Database:** TiDB Cloud (Serverless MySQL)
- **Frontend / Templating:** Thymeleaf, Bootstrap 5.3.3, Bootstrap Icons
- **Build Tool:** Apache Maven
- **Deployment:** Render Web Service (Docker / Native Java runtime)

---

## 💻 Local Setup & Development

### Prerequisites
- Java 21 JDK installed
- Maven 3.8+ installed
- MySQL or TiDB Cloud instance

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/girivardhan-pasupuleti/food-ordering-backend.git
   cd food-ordering-backend
