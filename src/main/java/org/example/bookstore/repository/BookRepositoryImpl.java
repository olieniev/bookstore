package org.example.bookstore.repository;

import java.util.List;
import org.example.bookstore.exception.DataProcessingException;
import org.example.bookstore.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert book into DB: "
            + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all books from DB.", e);
        }
    }

    @Override
    public Book getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Book.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Cannot find a book in DB with id: "
                    + id, e);
        }
    }
}
