package org.example.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.user.UserRegistrationRequestDto;
import org.example.bookstore.dto.user.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.mapper.UserMapper;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.RoleName;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.ShoppingCartService;
import org.example.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail((requestDto.getEmail()))) {
            throw new RegistrationException("User already exists with an email: "
                + requestDto.getEmail());
        }
        User registeredUser = userMapper.toModel(requestDto);
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
        registeredUser.setRoles(Set.of(userRole));
        registeredUser.setPassword(passwordEncoder.encode(registeredUser.getPassword()));
        userRepository.save(registeredUser);
        shoppingCartService.createShoppingCart(registeredUser);
        return userMapper.toUserResponseDto(registeredUser);
    }
}
