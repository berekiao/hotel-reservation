# 🏨 Système de Réservation d'Hôtel (Jakarta EE + Grizzly)

Ce projet est une application web développée avec **Jakarta EE**, **GrizzlyServer**, **Hibernate (JPA)** et **PostgreSQL**. Il permet aux clients de réserver des chambres d'hôtel et de gérer leurs réservations via une API REST.

---

## 🚀 Installation et Exécution

### 1️⃣ **Prérequis**
- **Java 17** ou supérieur
- **Maven** (Apache Maven 3.6+)
- **PostgreSQL** (Base de données `hotel_db` avec un utilisateur PostgreSQL)

### 2️⃣ **Cloner le projet**
```sh
git clone https://github.com/ton-utilisateur/ton-repo.git
cd ton-repo

### Compiler le development
```
mvn clean compile
```

### Exécuter le projet
```
mvn exec:java -Dexec.mainClass="com.hotel.GrizzlyServer"
```

📌 Routes API REST
🔹 Chambres
GET /api/chambres/disponibles → Liste des chambres disponibles
POST /api/reservations → Réserver une chambre
GET /api/reservations/client/{nom} → Rechercher les réservations d’un client
🔹 Réservations
PUT /api/reservations/{id} → Modifier une réservation
DELETE /api/reservations/{id} → Annuler une réservation

🎨 Interfaces Web
L'application inclut une interface utilisateur en HTML/CSS + Bootstrap :

Accueil → index.html
Réserver une chambre → reservation.html
Rechercher une réservation → recherche.html

🛠 Technologies utilisées
Jakarta EE 10 (JAX-RS, JPA)
GrizzlyServer (Serveur web léger)
Hibernate (ORM pour PostgreSQL)
PostgreSQL (Base de données)
Bootstrap 5.3 (Interface utilisateur)
