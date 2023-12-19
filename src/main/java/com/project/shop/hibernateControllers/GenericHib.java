package com.project.shop.hibernateControllers;

import com.project.shop.model.Comment;
import com.project.shop.model.Product;
import com.project.shop.model.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.List;

public class GenericHib {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager em;

    public GenericHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public <T> void create(T entity) {
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }
    public <T> void update(T entity) {
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }
    public <T> void delete(Class <T> entityClass, int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            var object = em.find(entityClass, id);
            em.remove(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }


    public <T> T getEntityById(Class<T> entityClass, int id) {
        T result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            result = em.find(entityClass, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public <T> List<T> getAllRecords(Class<T> entityClass) {
        EntityManager em = null;
        List<T> result = new ArrayList<>();
        try {
            em = getEntityManager();
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = em.createQuery(query);
            result = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return result;
    }
    public void deleteComment(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var comment = em.find(Comment.class, id);

            if (comment.getClass() == Review.class) {
                Review review = (Review) comment;
                Product product = em.find(Product.class, review.getProduct().getId());
                product.getReviews().remove(review);
                em.merge(product);
            } else {
                comment.getReplies().clear();
                em.remove(comment);

            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
