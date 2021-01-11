package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserById(Long id);

    //password reset by email
    User getUserByEmail(String email);
    User getUserByResetPasswordToken(String token);
}
