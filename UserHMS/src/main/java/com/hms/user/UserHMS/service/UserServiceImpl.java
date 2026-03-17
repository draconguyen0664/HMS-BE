package com.hms.user.UserHMS.service;

import com.hms.user.UserHMS.dto.UserDTO;
import com.hms.user.UserHMS.entity.User;
import com.hms.user.UserHMS.exception.HmsException;
import com.hms.user.UserHMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDTO userDTO) throws HmsException {
        Optional<User> opt = userRepository.findByEmail(userDTO.getEmail());
        if (opt.isPresent()) {
            throw new HmsException("USER_ALREADY_EXISTS");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO.toEntity());
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) throws HmsException {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(() -> new HmsException("USER_NOT_FOUND"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new HmsException("INVALID_CREDENTIALS");
        }
        user.setPassword(null);
        return user.toDTO();
    }

    @Override
    public UserDTO getUserById(Long id) throws HmsException {
        return userRepository.findById(id).orElseThrow(() -> new HmsException("USER_NOT_FOUND")).toDTO();
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        throw new UnsupportedOperationException("Unimpledmented method 'updateUser'");
    }
}
