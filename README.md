# ebiblioteka

It's a a web application for managing a library. It's written in Java, HTML and CSS.

Used technologies:
-Spring Boot
-Spring Web MVC
-Spring Data Jpa
-Spring Security
-Thymeleaf
-MySQL

The app functionalities:
-registration new accounts(by admin)
-login to the app based on account roles
-the system recognizes account role(admin,user) of the account and after successful login redirects to a different page(admin panel, user panel)
-mail service for passowrd reset purpose, book borrowing notification
-spring security secures subpages based on account role
-user can search for books, reserve/borrow/return books, change password
-admin can add/delete users/books, manage reservations
-everything is stored in MySQL database
-frontend is generated using Thymeleaf

It is a group student project made(with documentation) by 3 PJAIT students 2021/2022. 
