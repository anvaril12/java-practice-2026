package ru.itis.shop.user.infrastructure.persistence;

import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        System.out.println("Сохраняем в базу данных...");
        users.add(user);
        System.out.println("Пользователь сохранён в памяти");
    }

    @Override
    public User findById(String id) {
        System.out.println("Получаем из базы данных...");
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
