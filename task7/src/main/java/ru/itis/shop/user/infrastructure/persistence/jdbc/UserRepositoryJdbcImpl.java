package ru.itis.shop.user.infrastructure.persistence.jdbc;

import ru.itis.shop.infrastructure.persistence.jdbc.RowMapper;
import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    private final DataSource dataSource;

    private final RowMapper<User> userRowMapper = row -> new User(
            row.getInt("id"),
            row.getString("name"),
            row.getString("email"),
            row.getString("password"),
            row.getString("profileDescription")
    );

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO account (name, email, password, profileDescription) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getProfileDescription());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM account WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userRowMapper.mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("ошибка: " + e.getMessage());
        }

        return Optional.empty();
    }


    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM account WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(userRowMapper.mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("ошибка: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("select * from account")) {
                    while (resultSet.next()) {
                        users.add(userRowMapper.mapRow(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return users;
    }

    @Override
    public List<User> findAllByProfileDescription(String profileDescription) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE profileDescription = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, profileDescription);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(userRowMapper.mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("ошибка: " + e.getMessage());
        }

        return users;
    }

    @Override
    public void updateProfileDescriptionByEmail(String email, String newDescription) {
        String sql = "UPDATE account SET profileDescription = ? WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newDescription);
            statement.setString(2, email);

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new IllegalStateException("Пользователь с email " + email + " не найден");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка при обновлении профиля: " + e.getMessage());
        }
    }
}
