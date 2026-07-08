package ru.itis.shop.user.application;

import ru.itis.shop.user.api.dto.UserDto;
import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        return new UserDto(user.getId(), user.getEmail(), user.getProfileDescription());
    }

    public void signUp(String name, String email, String password, String profileDescription) {
        User user = new User(name, email, password, profileDescription);
        userRepository.save(user);
    }

    public boolean signIn(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get().getPassword().equals(password);
        } else return false;
    }

    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getProfileDescription()));
    }

    public void updateProfileByEmail(String email, String newDescription) {
        userRepository.updateProfileDescriptionByEmail(email, newDescription);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getProfileDescription()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<UserDto> findAllByProfileDescription(String profileDescription) {
        return userRepository.findAllByProfileDescription(profileDescription).stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getProfileDescription()))
                .collect(java.util.stream.Collectors.toList());
    }
}
