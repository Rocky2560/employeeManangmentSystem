Employee Management System
A simple Employee Management System (EMS) that supports Create, Read, Update, and Delete (CRUD) operations for managing employee records. It includes employee details like name and profile images.

Features

Create: Add new employees with their name and image.

Read: View all employees or details of a specific employee.

Update: Edit employee details.

Delete: Remove employee records.

Image Upload: Allows profile image upload.


Technologies

Backend: Java, Spring Boot

Frontend: Thymeleaf

Database: H2 (default), MySQL/PostgreSQL (configurable)

Image Storage: Local or cloud (configurable)

Setup


Clone the repo:

git clone https://github.com/your-username/employee-management-system.git

Configure Database: Update application.properties for your preferred database.

Build & Run:

mvn clean install
mvn spring-boot:run
Access: Open http://localhost:8080 in your browser.

Usage

Add Employee: Click Add Employee, fill out details, and save.

View Employees: See the employee list on the homepage.

Update/Delete: Edit or delete employee records as needed.


API Endpoints

Create: POST /add (name, image)

Read: GET /home (list), GET /employee/{id} (single)

Update: POST /edit/{id} (name, image)

Delete: GET /delete/{id}
