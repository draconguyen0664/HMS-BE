package com.hms.user.UserHMS.service;

import com.hms.user.UserHMS.dto.UserDTO;
import com.hms.user.UserHMS.exception.HmsException;

public interface UserService {
    public void registerUser(UserDTO userDTO) throws HmsException;
    public UserDTO loginUser(UserDTO userDTO) throws HmsException;
    public UserDTO getUserById(Long id) throws HmsException;
    public void updateUser(UserDTO userDTO);
}
