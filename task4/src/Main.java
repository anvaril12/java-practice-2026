import domain.User;
import repository.UserRepository;
import repository.UserRepositoryJdbcImpl;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

/**
 * Lesson 4
 *
 * @author Marsel Sidikov (AIT TR)
 */
public class Main {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/java2026";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "qwerty007";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            UserRepository userRepository = new UserRepositoryJdbcImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n1. Регистрация пользователя");
                System.out.println("2. Вход в систему");
                System.out.println("3. Найти пользователя по id");
                System.out.println("4. Обновить описание профиля");
                System.out.println("5. Показать всех пользователей");
                System.out.println("0. Выход");
                System.out.print("Выберите команду: ");

                String command = scanner.nextLine();

                switch (command) {
                    case "1":
                        System.out.println("регистрация — заглушка");
                        break;
                    case "2":
                        System.out.println("вход — заглушка");
                        break;
                    case "3":
                        System.out.println("поиск по id — заглушка");
                        break;
                    case "4":
                        System.out.println("обновление — заглушка");
                        break;
                    case "5": {
                        List<User> users = userRepository.findAll();
                        if (users.isEmpty()) {
                            System.out.println("пользователей нет");
                        } else {
                            System.out.println("\nсписок пользователей");
                            for (User user : users) {
                                System.out.println(user);
                            }
                            System.out.println("всего: " + users.size() + " пользователей");
                        }
                        break;
                    }
                    case "0":
                        System.out.println("Выход...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("ошибка");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("ошибка подключения к бд: " + e.getMessage());
        }
    }
}