package repository;

import domain.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    private final Connection connection;

    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {
        System.out.println("save заглушка");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        System.out.println("findByEmail заглушка");
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String id) {
        System.out.println("findById заглушка");
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM account";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getBoolean("is_active"),
                        resultSet.getString("email")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("ошибка при получении пользователей: " + e.getMessage());
        }
        return users;
    }
}
