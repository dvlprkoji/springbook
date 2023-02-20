package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.dao.UserLevelUpgradePolicy;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

//    public void upgradeLevels() {
//        List<User> users = userDao.getAll();
//        for (User user : users) {
//            if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
//                upgradeLevel(user);
//            }
//        }
//    }
//    protected void upgradeLevel(User user) {
//        user.upgradeLevel();
//        userDao.update(user);
//    }

    public void upgradeLevels() throws SQLException {
        Connection c = null;
        User user = null;
        // ...

        c.setAutoCommit(false); // Auto-Commit 해제
        try {
            // ...
            upgradeLevel(c, user);
            // ...
        } catch (SQLException e) {
            c.rollback();
            throw e;
        }
        // ...
    }

    protected void upgradeLevel(Connection c, User user) throws SQLException{
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
