package com.digiboard.app.dao;

import com.digiboard.app.entity.User;
import com.digiboard.app.util.JPAUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserDao implements Serializable{
    
    //Método reponsável por adicionar um usuário ao banco de dados
    public void addUser(User user){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            JPAUtil.closeEntityManager();
        }
    }
    
    //Método responsável por buscar todos usuários no banco de dados
    public List<User> getUsers(String filter){
        EntityManager em = JPAUtil.getEntityManager();
        List users = null;
        try {
            String sql = "SELECT u FROM User u WHERE (:filter is null OR u.name LIKE :filter)";
            Query consult = em.createQuery(sql);
            consult.setParameter("filter", filter.isEmpty() ? null : "%" + filter + "%");
            users = consult.getResultList();
        } finally {
            JPAUtil.closeEntityManager();
        }
        
        return users;
    }
    
    //Método responsável por atualizar um usuário no banco de dados
    public void update(User user){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }
    
    //Método responsável por deletar um usuário no banco de dados
    public void delete(Long id){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            User u = em.find(User.class, id);
            if(u != null){
                em.getTransaction().begin();
                em.remove(u);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }
    
}
