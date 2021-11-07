package com.library.project;

import com.library.project.model.User;
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
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        User user = new User();
        user.setEmail("januszpol@wp.pl");
        user.setPassword("12345");
        user.setFirstName("Janusz");
        user.setLastName("Kowalski");
        user.setEnabled(true);


        User savedUser = userRepository.save(user);

        User existUser = entityManager.find(User.class,savedUser.getId());

        assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void getUserByEmail(){
        User user = new User();
        user.setEmail("januszpol@wp.pl");
        user.setPassword("12345");
        user.setFirstName("Janusz");
        user.setLastName("Kowalski");
        user.setEnabled(true);
        userRepository.save(user);
        String email = "januszpol@wp.pl";
        User findUser = userRepository.getUserByEmail(email);
        assertThat(findUser).isNotNull();
    }

}
