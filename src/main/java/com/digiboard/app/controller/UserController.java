package com.digiboard.app.controller;

import com.digiboard.app.dao.UserDao;
import com.digiboard.app.entity.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

@Named
@ViewScoped
public class UserController implements Serializable{
    
    @Inject
    private UserDao dao;
    
    private String searchText = "";
    
    private UploadedFile uploadedFile;
    
    private User user = new User();
    
    private List<User> users;
    
    //Busca os usuarios antes do carregamento total do html
    @PostConstruct
    public void init(){
        users = dao.getUsers(searchText);
    }
    
    public void search(){
        users = dao.getUsers(searchText);
    }
    
    //Método de adicionar um usuario
    public void addUser()throws IOException{
        //Chama o metodo de salvar a imagem
        saveImage();
        //Salva o usuário no banco de dados
        dao.addUser(getUser());
        user = new User();
        //Redireciona para outra página após chamar este método
        FacesContext.getCurrentInstance().getExternalContext().redirect("adminpage.xhtml");
    }
    
    //Método que atualiza meu usuário
    public void update() throws IOException{
        dao.update(this.user);
        FacesContext.getCurrentInstance().getExternalContext().redirect("adminpage.xhtml");
        user = new User();
    }
    
    //Seta um usuario para modo edição
    public void edit(User user){
        this.user = user;
    }
    
    //Método de deletar um usuário
    public void deleteUser(User user){
        users.remove(user);
        dao.delete(user.getId());
    }
    
    //Evento disparado ao selecionar imagem
    public void handleFileUpload(FileUploadEvent event){
        this.uploadedFile = event.getFile();
        this.user.setPhoto("/resources/img/uploads/" + event.getFile().getFileName());
    }
    
    //Método que salva a imagem selecionada
    public void saveImage(){
        if(uploadedFile != null){
            File newArchive = new File("C:/Users/Victor/Documents" + this.uploadedFile.getFileName());
            try {

                if(newArchive.exists()){
                    newArchive.delete();
                }

                newArchive.createNewFile();

                InputStream inputStream = uploadedFile.getInputStream();

                FileOutputStream outputStream = new FileOutputStream(newArchive);

                // Cria um buffer de bytes para ler os dados do arquivo enviado e escrever no novo arquivo
                byte[] buffer = new byte[4096];

                // Variável para armazenar a quantidade de bytes lidos do arquivo enviado
                int bytesRead = -1;

                // Lê os dados do arquivo enviado e escreve no novo arquivo
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Força a escrita dos dados no novo arquivo
                outputStream.flush();

                // Fecha o InputStream e o OutputStream para liberar os recursos
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
}