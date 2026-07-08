package ru.itis.shop.user.api;

import ru.itis.shop.user.api.dto.UserDto;
import ru.itis.shop.user.application.UserService;
import ru.itis.shop.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserConsoleOperations {

    private final UserService userService;
    private final Scanner scanner;

    public UserConsoleOperations(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        printUserMenu();

        String command = scanner.nextLine();

        switch (command) {
            case "1": {
                signUp();
            }
            break;
            case "2": {
                signIn();
            }
            break;
            case "3": {
                findById();
            }
            break;
            case "4": {
                updateByEmail();
            }
            break;
            case "5": {
                findAll();
            }
            break;
            case "6": {
                findByProfileDescription();
            }
            break;
            case "7": {
                findByEmail();
            }
            break;
            case "0": {
                System.exit(0);
            }
            break;
            default: {
                System.out.println("неизвестная команда");
            }
        }
    }

    private static void printUserMenu() {
        System.out.println("1. Регистрация пользователя");
        System.out.println("2. Вход в систему");
        System.out.println("3. Найти пользователя по id");
        System.out.println("4. Обновить описание пользователя по почте");
        System.out.println("5. Получить информацию обо всех пользователях");
        System.out.println("6. Показать информацию о пользователях с заданным описанием профиля");
        System.out.println("7. Показать информацию о пользователя по email");
        System.out.println("0. Выход");
    }

    private void signUp() {
        System.out.println("Сейчас будем регистрировать пользователя");
        System.out.println("Введите name:");
        String name = scanner.nextLine();
        System.out.println("Введите email:");
        String email = scanner.nextLine();
        System.out.println("Введите password:");
        String password = scanner.nextLine();
        System.out.println("Введите описание профиля:");
        String profileDescription = scanner.nextLine();

        userService.signUp(name, email, password, profileDescription);
    }

    private void signIn() {
        System.out.println("Вы можете войти в приложение");
        System.out.println("Введите email:");
        String email = scanner.nextLine();
        System.out.println("Введите password:");
        String password = scanner.nextLine();

        if (userService.signIn(email, password)) {
            System.out.println("Вы вошли в приложение");
        } else {
            System.out.println("Email или пароль не верны");
        }
    }

    private void findById() {
        System.out.print("введите id: ");
        try {
            Integer id = Integer.parseInt(scanner.nextLine());
            Optional<UserDto> userOptional = userService.findById(id);

            if (userOptional.isPresent()) {
                UserDto user = userOptional.get();
                System.out.println("id: " + user.getId());
                System.out.println("email: " + user.getEmail());
                System.out.println("описание: " + user.getProfileDescription());
            } else {
                System.out.println("пользователь с id " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("ошибка: id должен быть числом");
        }
    }

    private void updateByEmail() {
        System.out.print("email пользователя: ");
        String email = scanner.nextLine();
        System.out.print("новое описание профиля: ");
        String newDescription = scanner.nextLine();

        try {
            userService.updateProfileByEmail(email, newDescription);
            System.out.println("описание обновлено");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void findAll() {
        List<UserDto> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("пользователей нет");
            return;
        }

        for (UserDto user : users) {
            System.out.println(user.getId() + " | " + user.getEmail() + " | " + user.getProfileDescription());
        }
        System.out.println("всего: " + users.size() + " пользователей");
    }

    private void findByProfileDescription() {
        System.out.print("введите описание профиля: ");
        String description = scanner.nextLine();

        List<UserDto> users = userService.findAllByProfileDescription(description);

        if (users.isEmpty()) {
            System.out.println("пользователи с описанием '" + description + "' не найдены");
        } else {
            for (UserDto user : users) {
                System.out.println(user.getId() + " | " + user.getEmail() + " | " + user.getProfileDescription());
            }
            System.out.println("всего: " + users.size() + " пользователей");
        }
    }

    private void findByEmail() {
        System.out.print("введите email: ");
        String email = scanner.nextLine();

        try {
            UserDto user = userService.getUserByEmail(email);
            System.out.println("id: " + user.getId());
            System.out.println("email: " + user.getEmail());
            System.out.println("описание: " + user.getProfileDescription());
        } catch (Exception e) {
            System.out.println("пользователь с email " + email + " не найден");
        }
    }
}