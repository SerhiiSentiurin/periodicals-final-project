package periodicals.epam.com.project.logic.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import periodicals.epam.com.project.infrastructure.web.exception.ApplicationException;
import periodicals.epam.com.project.logic.dao.UserDAO;
import periodicals.epam.com.project.logic.entity.User;
import periodicals.epam.com.project.logic.entity.dto.UserDTO;

@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDao;

    public User getUserByLogin(UserDTO userDto) {
        log.info("Try to log in user: " + userDto.getLogin());

        User user = userDao.getUserByLogin(userDto.getLogin())
                .orElseThrow(() -> new ApplicationException("cannot find user with this login"));

        if (!user.getPassword().equals(userDto.getPassword())) {
            throw new ApplicationException("password is incorrect");
        }
        log.info("user: " + userDto.getLogin() + " was entered");
        return user;
    }
}
