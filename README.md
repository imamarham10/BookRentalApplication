---

# Book Rental Application

Welcome to the Book Rental Application! This application allows users to manage books, authors, and rentals.

## Setup Instructions

To set up and run the application locally, follow these steps:

1. **Clone the Repository**: Clone the repository to your local machine using the following command:
   ```
   git clone https://github.com/imamarham10/BookRentalApplication.git
   ```

2. **Navigate to the Project Directory**: Change to the project directory:
   ```
   cd BookRentalApplication
   ```

3. **Install Dependencies**: Install the necessary dependencies using Maven:
   ```
   mvn clean install
   ```

4. **Configure Database**: Configure your database settings in the `application.properties` file located in the `src/main/resources` directory.

5. **Run the Application**: Run the application using Maven:
   ```
   mvn spring-boot:run
   ```

6. **Access the Application**: Access the application by navigating to `http://localhost:8080` in your web browser.

## API Endpoints

### Books

- **Get All Books**: `GET /api/books/`
- **Get Book by ID**: `GET /api/books/by-id?id={id}`
- **Get Book by Author Name**: `GET /api/books/by-author?name={name}`
- **Get Book currently rented**: `GET /api/books/currently-rented`
- **Get Book available for rent**: `GET /api/books/available-for-rent`
- **Add Book**: `POST /api/books/{id}`
  - Request Body:
    ```json
    {
        "title": "Sample Book",
        "isbn": "1234567890",
        "publicationYear": 2022
    }
    ```
- **Update Book**: `PUT /api/books/{id}?author_id={id}`
  - Request Body:
    ```json
    {
    "title": "Sample Book 2",
    "isbn": "978-3-16-148410-2",
    "publicationYear": 2021,
    "availableForRent": true
    }
    ```
- **Delete Book**: `DELETE /api/books/{id}`

### Authors

- **Get All Authors**: `GET /api/author/`
- **Get Author by ID**: `GET /api/author/by-id?author_id={id}`
- **Add Author**: `POST /api/author`
  - Request Body:
    ```json
    {
        "name": "John Doe",
        "biography": "Test Author Biography"
    }
    ```
- **Update Author**: `PUT /api/author?author_id={id}`
  - Request Body:
    ```json
    {
        "name": "Updated Author Name",
        "biography": "Updated Author Biography"
    }
    ```
- **Delete Author**: `DELETE /api/author?author_id={id}`

### Rentals

- **Get All Rentals**: `GET /api/rentals/`
- **Get Rental by Renter Name**: `GET /api/rentals/by-renterName?name={name}`
- **Add Rental using Book Id**: `POST /api/rentals?book_id={id}`
  - Request Body:
    ```json
     {
      "renterName": "John",
      "rentalDate": "2024-03-30",
      "returnDate": null
    }
    ```
- **Update Rental**: `PUT /api/rentals?rental_id={id}`
  - Request Body:
    ```json
    {
        "returnDate": "2024-04-23"
    }
    ```
- **Delete Rental**: `DELETE /api/rentals?rental_id={id}`

## All the Sample Requests are present in Postman Collection folder

---
