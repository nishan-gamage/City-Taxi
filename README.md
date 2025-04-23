# 🚖 City Taxi – Smart Ride Booking Platform

City Taxi is a modern web-based ride-booking platform built with a full-stack architecture using **React.js** and **Spring Boot**. It offers real-time ride management, secure authentication, interactive maps, and smooth role-based access for **operators**, **drivers**, and **passengers**.

---

## 🧰 Tech Stack

### 🔹 Frontend
- **React.js** – Component-based architecture for responsive UI.
- **Zustand** – Lightweight global state management.
- **Tailwind CSS** – Utility-first CSS for elegant and mobile-friendly design.
- **Shadcn/UI** – Modern pre-built UI components.
- **Axios** – For communication with RESTful APIs.
- **@vis.gl/react-google-maps** – Seamless integration of Google Maps.

### 🔹 Backend
- **Spring Boot** – Robust backend framework with embedded Tomcat.
- **Spring Data JPA** – Simplifies database access and operations.
- **Spring Security + JWT** – Authentication & authorization using JSON Web Tokens.
- **Swagger** – Auto-generated API documentation and testing.
- **Lombok** – Reduces boilerplate Java code with annotations.

### 🔹 Database
- **PostgreSQL** – Open-source relational database.
- Features: Indexing, Transaction Management, and Relational Modeling.

---

## 🚀 Key Features

- 📍 **Real-time location tracking** of drivers using Google Maps.
- 🧾 **Manual ride booking** interface for operators.
- 🔐 **JWT-based login** system with secure password handling.
- 🔄 **Live ride updates** with driver assignment and trip status.
- 📦 **Role-based access control** (Passenger, Driver, Operator).
- 🗺️ **Interactive destination selection** via map.
- 📊 **Admin dashboard** for system overview and user control.
- 🧪 **Swagger UI** for interactive API testing and documentation.

---

## 📸 Screenshots



### 🔐 Login Page  
![Login Page](login.jpg)

### 🗺️ Driver Map View  
![Map View](map-view.jpg)

### 🚕 Booking Interface  
![Booking](booking.jpg)

### 🧑‍💼 Operator Dashboard  
![Dashboard](admin-dashboard.jpg)

---

## 🏗️ System Architecture

```text
┌────────────┐     REST API      ┌──────────────┐     PostgreSQL
│  React JS  │  ─────────────▶  │ Spring Boot  │ ─────────────────▶ Database
└────────────┘   (Axios)         └──────────────┘     (Spring Data JPA)
       │                                 ▲
       └──────────── Google Maps ────────┘
