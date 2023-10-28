package com.digiboard.app.controller;

import com.digiboard.app.dao.LoginDao;
import com.digiboard.app.entity.Login;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginSession implements Serializable{
    
    private Login login;
    private String username;
    private String password;
    private boolean authenticated = false;
    
    @Inject
    LoginDao dao = new LoginDao();
    
    //Método de login que vai ser chamado pelo JSF
    public void login() throws IOException{
        login = (Login) dao.auth(this.username, this.password);
        if(login != null){
            password = "";
            this.authenticated = true;
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("adminpage.xhtml");
        }
    }
    
    //Verifica se o usuário está autenticado
    public void verifyAuthentication()throws IOException{
        if(!authenticated){
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }
    
    public void logout(){
        login = null;
        username = null;
        authenticated = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    
    
}
