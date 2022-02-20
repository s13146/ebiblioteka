# ebiblioteka

It's a a web application for managing a library. It's written in Java, HTML and CSS.

Used technologies:<br />
-Spring Boot<br />
-Spring Web MVC<br />
-Spring Data Jpa<br />
-Spring Security<br />
-Thymeleaf<br />
-MySQLv

The app functionalities:<br />
-registration new accounts(by admin)<br />
-login to the app based on account roles<br />
-the system recognizes account role(admin,user) of the account and after successful login redirects to a different page(admin panel, user panel)<br />
-mail service for passowrd reset purpose, book borrowing notification<br />
-spring security secures subpages based on account role<br />
-user can search for books, reserve/borrow/return books, change password<br />
-admin can add/delete users/books, manage reservations<br />
-everything is stored in MySQL database<br />
-frontend is generated using Thymeleaf<br />

It is a group student project made(with documentation) by 3 PJAIT students 2021/2022. 
