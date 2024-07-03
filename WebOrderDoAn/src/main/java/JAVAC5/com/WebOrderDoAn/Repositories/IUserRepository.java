package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
}