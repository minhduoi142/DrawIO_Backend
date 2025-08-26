package com.Minh.DrIO.Repository;


import com.Minh.DrIO.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByIdToken(String idToken);
}
