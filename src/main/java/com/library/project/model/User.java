package com.library.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true, length = 320)
    private String email;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(nullable = false, length = 35)
    private String firstName;
    @Column(nullable = false, length = 45)
    private String lastName;
    @Column(nullable = false,length = 1)
    private int isEnabled;
    @Column(nullable = true, length = 255)
    private String reservedBooks;
    @Column(nullable = true, length = 255)
    private String borrowedBooks;
    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getReservedBooks() {
        return reservedBooks;
    }

    public void setReservedBooks(String reservedBooks) {
        this.reservedBooks = reservedBooks;
    }

    public String getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(String borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }
}
