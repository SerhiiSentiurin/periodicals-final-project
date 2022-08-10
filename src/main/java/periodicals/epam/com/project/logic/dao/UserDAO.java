package periodicals.epam.com.project.logic.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import periodicals.epam.com.project.logic.entity.Admin;
import periodicals.epam.com.project.logic.entity.Reader;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.UserRole;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public Optional<User> getUserByLogin(String login) {
        String getUserByLogin = "SELECT * FROM user WHERE login=?";
        User foundUser;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getUserByLogin)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                if (role.equals("ADMIN")) {
                    foundUser = new Admin();
                    foundUser.setUserRole(UserRole.ADMIN);
                } else {
                    foundUser = new Reader();
                    foundUser.setUserRole(UserRole.READER);
                }
                foundUser.setId(id);
                foundUser.setLogin(login);
                foundUser.setPassword(password);
                return Optional.of(foundUser);
            }
        }
        return Optional.empty();
    }
}
