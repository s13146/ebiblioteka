package com.library.project;

import com.library.project.model.UserEntity;
import com.library.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserEntityRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("januszpol@wp.pl");
        userEntity.setPassword("12345");
        userEntity.setFirstName("Janusz");
        userEntity.setLastName("Kowalski");
        userEntity.setIsEnabled(0);
        userEntity.setReservationsCount(0);


        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserEntity existUserEntity = entityManager.find(UserEntity.class, savedUserEntity.getId());

        assertThat(existUserEntity.getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    public void getUserByEmail(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("januszpol@wp.pl");
        userEntity.setPassword("12345");
        userEntity.setFirstName("Janusz");
        userEntity.setLastName("Kowalski");
        userEntity.setIsEnabled(0);
        userRepository.save(userEntity);
        String email = "januszpol@wp.pl";
        UserEntity findUserEntity = userRepository.getUserByEmail(email);
        assertThat(findUserEntity).isNotNull();
    }

}
