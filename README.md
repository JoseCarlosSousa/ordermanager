# Simple Order Manager API

This is a simple order management system developed as a technical exercise. The API allows creating and managing orders, items, users, and stock movements, automatically fulfilling pending orders as stock becomes available.

## 🚀 How to Run the Project

### Prerequisites
* **Java 21** (or Java 17+)
* **PostgreSQL** (running on port `5432`)
* **Maven**

### Steps to Run
1. Clone the repository and navigate to the project root:
   ```bash
   git clone git@github.com:JoseCarlosSousa/ordermanager.git
   cd ordermanager
   ```
2. Configure your PostgreSQL database credentials in `src/main/resources/application.yaml`.
3. Create a local database named `ordermanager` (or the name configured in your yaml).
4. Build and run the application using Maven:
   ```bash
   mvn clean spring-boot:run
   ```

---

## 📖 API Documentation & Testing

### 🟢 Swagger UI (Interactive Documentation)
The API documentation is interactive and available online. You can view all available routes, models, and execute requests directly from your browser using the links below:

* 🌐 **Production Environment:** [https://ordermanager-1z0k.onrender.com/swagger-ui/index.html](https://ordermanager-1z0k.onrender.com/swagger-ui/index.html)
* 🔍 **Direct Shortcut to `findAll` Users:** [https://ordermanager-1z0k.onrender.com/swagger-ui/index.html#/User/findAll](https://ordermanager-1z0k.onrender.com/swagger-ui/index.html#/User/findAll)
* 💻 **Local Environment (When running locally):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


### 📦 Postman Collection
An automated Postman collection is included in this repository to help you test the endpoints quickly, covering JSON, XML, and YAML content types.

* **Collection File Path:** `/postman/OrderManager.postman_collection.json`
* **How to use:**
  1. Open Postman.
  2. Click on the **Import** button in the top-left corner.
  3. Drag and drop the JSON file located inside the `postman/` folder of this project.
  4. Make sure your local server is running on port `8080` and start testing the routes!

---

## 🛠️ Main Specification Features
* **Automatic Fulfillment:** Orders are evaluated against current stock upon creation. Creating a new `StockMovement` automatically triggers an allocation algorithm to fulfill incomplete orders.
* **Content Negotiation:** Endpoints support fully structured data formats via JSON, XML, and YAML.
* **Global Exception Handling:** Clean, custom errors matching the requested media type (`ExceptionResponse`) instead of generic server crashes.
* **Logging System:** All business actions, stock allocations, emails sent, and operational errors are thoroughly tracked in the logs.
