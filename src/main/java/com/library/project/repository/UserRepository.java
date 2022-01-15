package com.library.project.repository;

import com.library.project.model.Book;
import com.library.project.model.Reservation;
import com.library.project.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u from UserEntity u where u.email = :email")
    UserEntity getUserByEmail(String email);

    UserEntity findByResetPasswordToken(String token);

    @Query("SELECT u FROM UserEntity u WHERE u.email LIKE %?1%")
    List<UserEntity> getUsersByEmail(String keyword);
}
