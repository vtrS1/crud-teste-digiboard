package com.digiboard.app.dao;

import com.digiboard.app.entity.Login;
import com.digiboard.app.util.JPAUtil;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class LoginDao implements Serializable{
    
    //Método responsável por verificar um login no Banco de Dados
    public Login auth(String username, String password){
        EntityManager em = JPAUtil.getEntityManager();
        Login session = null;
        try {
            String sql = "SELECT l FROM Login l WHERE l.username = :username AND l.password = :password";
            Query consult = em.createQuery(sql);
            consult.setParameter("username", username);
            consult.setParameter("password", password);
            session = (Login) consult.getSingleResult();
            if(session != null){
                return session;
            }
        } catch (Exception e) {
            return null;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return null;
    }
    
}
