package ru.pechatny.cloud.server.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.pechatny.cloud.server.HibernateSessionFactoryUtil;
import ru.pechatny.cloud.server.models.User;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public User get(int id) {
        {
            return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User.class, id);
        }
    }

    public Optional get(String login) {
        {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from User  where login = :paramLogin");
            query.setParameter("paramLogin", login);

            return query.list().stream().findFirst();
        }
    }

    public boolean save(String login, String password) {
        {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            User user = new User();
            user.login = login;
            user.password = password;
            session.save(user);

            session.getTransaction().commit();
            session.close();

            return user.id > 0;
        }
    }
}
