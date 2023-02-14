package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            Boolean changed = null;
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                changed = true;
                user.setLevel(Level.SILVER);
            }
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                changed = true;
                user.setLevel(Level.GOLD);
            }
            else if (user.getLevel() == Level.GOLD) {
                changed = false;
            }
            else {
                changed = false;
            }
            if (changed) userDao.update(user);
        }
    }
}
