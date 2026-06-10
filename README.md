# 🍽️ Restaurant Billing System

A desktop-based **Restaurant Billing System** developed using **Java Swing** and **MongoDB**. The application helps restaurants manage food items, process customer orders, generate bills, and store orders.

---

## 📌 Features

* 🔐 User Login System
* 🍕 Add, Update, and Delete Food Items
* 🔍 Search Food by Name, Category, and Price
* 🛒 Customer Order Management
* 🧾 Bill Generation
* 🖨️ Store Bills in MongoDB
* 📊 View Total Orders

---

## 🛠️ Technologies Used

* Java
* Java Swing
* MongoDB
* MongoDB Java Driver
* VS Code

---


## ⚙️ Prerequisites

* Java JDK 17 or above
* MongoDB Server
* VS Code (Recommended)

---

## ▶️ How to Run

Compile the project:

```bash
javac -cp "lib/*" -d bin src/*.java
```

Run the application:

```bash
java -cp "lib/*;bin" App
```

---

## 🗄️ Database

### Database: `FoodDB`

Collection:

* `Foods`

### Database: `BillingSystem`

Collections:

* `Bills`
* `SavedOrders`

---

## 📸 Main Modules

* Login -> Login, Registration, Forgot, PasswordUtils
* Food Management -> Main, Change
* Order & Billing Section -> Order, Billing, SaveOrder
* Total Orders -> Total
* Admin Section -> Admin, Adminmain, Adminchange, AdminTotal, Employee

---
## 🚀Admin Login

* Username: rachit
* Password: 12345

---
## 🚀 Future Enhancements

* PDF Bill Export
* GST Calculation
* Sales Dashboard

---

## 👨‍💻 Developer

**Rachit Bhardwaj**

B.Tech Computer Science & Engineering

---

## 📄 License

This project is created for educational and learning purposes.
