package springbook;

import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        UserDao dao = new DaoFactory().userDao();

        User user = new User();
        user.setId("koji");
        user.setName("안재홍");
        user.setPassword("passw0rd");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
