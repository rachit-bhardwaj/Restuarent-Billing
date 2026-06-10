# 🍽️ Restaurant Billing System

A desktop-based **Restaurant Billing System** developed using **Java Swing** and **MongoDB**. The application helps restaurants manage food items, process customer orders, generate bills, save incomplete orders, and retrieve them later.

---

## 📌 Features

* 🔐 User Login System
* 🍕 Add, Update, and Delete Food Items
* 🔍 Search Food by Name, Category, and Price
* 🛒 Customer Order Management
* 🧾 Bill Generation
* 💾 Save Incomplete Orders
* 🔄 Retrieve Saved Orders Using Mobile Number
* 🖨️ Print and Store Bills in MongoDB
* 📊 View Total Orders

---

## 🛠️ Technologies Used

* Java
* Java Swing
* MongoDB
* MongoDB Java Driver
* VS Code

---

## 📂 Project Structure

```
Restaurant_Billing/
│
├── src/                  # Java source files
├── lib/                  # MongoDB driver libraries
├── bin/                  # Compiled class files
├── .vscode/              # VS Code configuration
├── SRS/                  # Software Requirement Specification
├── README.md
└── manifest.txt
```

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

* Login
* Food Management
* Order Section
* Billing Section
* Saved Order Retrieval
* Total Orders

---

## 🚀 Future Enhancements

* PDF Bill Export
* GST Calculation
* Sales Dashboard
* Inventory Management
* Employee Management
* Customer Loyalty Program

---

## 👨‍💻 Developer

**Rachit Bhardwaj**

B.Tech Computer Science & Engineering

---

## 📄 License

This project is created for educational and learning purposes.
