package thebook.fshop.service;  // Add this line to resolve the missing package issue

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thebook.fshop.entity.User;
import thebook.fshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByType(String type) {
        return userRepository.findByUserType(type);
    }
}
