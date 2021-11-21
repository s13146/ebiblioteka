package com.library.project.model;

import javax.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //our reservation entity will have a foreign key column named user_id reffering to the primary key attribute of out user entity
    private UserEntity userEntity;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(nullable = false)
    private byte isBooked;
    @Column(nullable = false)
    private byte isReadyForPickUp;
    @Column(nullable = false)
    private byte isTaken;
    @Column(nullable = false)
    private byte isReturned;


    public Reservation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public byte getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(byte isBooked) {
        this.isBooked = isBooked;
    }

    public byte getIsReadyForPickUp() {
        return isReadyForPickUp;
    }

    public void setIsReadyForPickUp(byte isReadyForPickUp) {
        this.isReadyForPickUp = isReadyForPickUp;
    }

    public byte getIsTaken() {
        return isTaken;
    }

    public void setIsTaken(byte isTaken) {
        this.isTaken = isTaken;
    }

    public byte getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(byte isReturned) {
        this.isReturned = isReturned;
    }
}
