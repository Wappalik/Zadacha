package jm.task.core.jdbc.dao;

import com.mysql.cj.protocol.Resultset;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getSessionFactory();
    Transaction transaction;

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()){
           transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS chmoshniki (id BIGINT(10) PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR (64), lastname VARCHAR (64), age TINYINT (10))").executeUpdate();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
      try (Session session = sessionFactory.openSession()){
          transaction = session.beginTransaction();
        session.createSQLQuery("DROP TABLE IF EXISTS chmoshniki").executeUpdate();
          transaction.commit();

      } catch (Exception e) {
          if (transaction != null) {
              transaction.rollback();
          }
      }
      }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name,lastName,age);
            session.save(user);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById (long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class,id);
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List <User> list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
           return session.createQuery("from User",User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()){
           transaction = session.beginTransaction();
           session.createQuery("delete User ").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
