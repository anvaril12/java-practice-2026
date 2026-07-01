package ru.itis.shop.user.infrastructure.persistence;

import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

public class UserFileRepository implements UserRepository {

    private final String fileName;

    private final UserMapper userMapper;

    public UserFileRepository(String fileName, UserMapper userMapper) {
        this.fileName = fileName;
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String id = UUID.randomUUID().toString();
            user.setId(id);
            writer.write(userMapper.toLine(user));
            writer.newLine();
            System.out.println(" Пользователь сохранён с id: ");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            String line = reader.readLine();

            while (line != null) {

                User user = userMapper.fromLine(line);

                if (user.getEmail().equals(email)) {
                    return Optional.of(user);
                }

                line = reader.readLine();
            }

            return Optional.empty();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = userMapper.fromLine(line);
                if (user.getId().equals(id)) {
                    return Optional.of(user);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка чтения файла: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(User user) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder fileContent = new StringBuilder();
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                User existingUser = userMapper.fromLine(line);
                if (existingUser.getId().equals(user.getId())) {
                    fileContent.append(userMapper.toLine(user)).append("\n");
                    found = true;
                } else {
                    fileContent.append(line).append("\n");
                }
            }
            reader.close();

            if (!found) {
                throw new IllegalArgumentException("Пользователь с данным id не найден");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(fileContent.toString());
            writer.close();

            System.out.println("Данные обновлены");

        } catch (IOException e) {
            throw new IllegalStateException("Ошибка " + e.getMessage());
        }
    }
}
