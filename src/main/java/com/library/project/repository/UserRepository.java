package com.library.project.repository;

import com.library.project.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u from UserEntity u where u.email = :email")
    UserEntity getUserByEmail(String email);

    UserEntity findByResetPasswordToken(String token);
}
