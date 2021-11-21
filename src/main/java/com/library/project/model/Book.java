package com.library.project.model;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true, length = 320)
    private String title;
    @Column(nullable = false, length = 100)
    private String author;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(nullable = false)
    private byte isAvailable;

    public Book(String title, String author, Category category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = 1;
    }

    public Book() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(byte isAvailable) {
        this.isAvailable = isAvailable;
    }
}
