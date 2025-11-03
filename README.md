# Spring Boot Product API

A robust REST API for managing an e-commerce product catalog. This project is built with Spring Boot and follows a professional, layered architecture with a focus on clean code, testability, and best practices.

## 🚀 Features

* **Full CRUD Operations:** Create, Read, Update, and Delete (CRUD) for products.
* **Layered Architecture:** Follows a strict Controller-Service-Repository pattern.
* **DTOs & Mappers:** Uses Data Transfer Objects (DTOs) and a Mapper component to decouple the API layer from the database (Entity) layer.
* **Validation:** Implements request validation using `jakarta.validation` to ensure data integrity.
* **Exception Handling:** A global exception handler provides clean, formatted JSON error responses for `404 Not Found` and `400 Bad Request`.
* **In-Memory Database:** Uses H2 (in-memory) for easy development and testing.
* **Unit Tested:** The service layer is 100% unit-tested with **JUnit 5** and **Mockito**, covering all business logic paths.

## 🛠️ Technologies Used

* **Java 17**
* **Spring Boot 3.x**
* **Spring Web:** For creating the REST controllers.
* **Spring Data JPA:** For database interaction.
* **H2 Database:** In-memory development database.
* **Lombok:** To reduce boilerplate code.
* **Maven:** For project build and dependency management.
* **JUnit 5 & Mockito:** For unit testing.

## 🏛️ Project Architecture

This project is built using a clean, 5-layer architecture:

1.  **Controller Layer (`@RestController`):** Handles HTTP requests, validates input (`@Valid`), and speaks only in DTOs.
2.  **Service Layer (`@Service`):** Contains all business logic. Orchestrates the mapper and repository.
3.  **Mapper Layer (`@Component`):** Handles the conversion between DTOs and database Entities.
4.  **Repository Layer (`@Repository`):** Handles all database communication, powered by Spring Data JPA.
5.  **Entity Layer (`@Entity`):** Represents the `products` table in the database.

This separation of concerns makes the application clean, maintainable, and easy to test.

## 🏁 How to Run Locally

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/product-api.git](https://github.com/your-username/product-api.git)
    cd product-api
    ```
2.  **Run the application:**
    * Open the project in your favorite IDE (like IntelliJ IDEA).
    * Navigate to `src/main/java/com/myportfolio/productapi/ProductApiApplication.java` and run the `main` method.
3.  The application will start on `http://localhost:8080`.

4.  **Access the H2 Database Console:**
    * Navigate to `http://localhost:8080/h2-console`
    * Make sure the **JDBC URL** is set to `jdbc:h2:mem:productdb`
    * **Username:** `sa`
    * **Password:** (leave blank)
    * Click "Connect" to see the `PRODUCTS` table.

## 🧪 How to Test

### Unit Tests

You can run all unit tests for the service layer from your IDE by right-clicking on the `src/test/java/com/myportfolio/productapi/service/impl/ProductServiceImplTest.java` file and selecting "Run 'ProductServiceImplTest'".

### API Endpoints (Manual Testing)

You can use any API client (like Postman) to test the endpoints.

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/products` | Creates a new product. |
| `GET` | `/api/products/{id}` | Gets a single product by its ID. |
| `GET` | `/api/products` | Gets a list of all products. |
| `PUT` | `/api/products/{id}` | Updates an existing product. |
| `DELETE` | `/api/products/{id}` | Deletes a product by its ID. |

#### Example: Create a Product (`curl`)

```bash
curl -X POST http://localhost:8080/api/products \
-H "Content-Type: application/json" \
-d '{
    "name": "Example Laptop",
    "description": "A powerful laptop.",
    "price": 1299.99,
    "quantityOfStock": 25
}'