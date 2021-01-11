package live.jrmd.sidecar.services;

import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.Exception;

@Service
@Transactional
public class UserService extends Exception{

    @Autowired
    private UserRepository userDao;

    public void updateResetPasswordToken (String token, String email) throws UserNotFoundException {
        User user = userDao.getUserByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userDao.save(user);
        } else {
            throw new UserNotFoundException("Could not find a user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token){
        return userDao.getUserByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userDao.save(user);
    }

}
