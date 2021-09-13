package tn.kidzone.spring.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import tn.kidzone.spring.entity.User;
import tn.kidzone.spring.entity.User.Role;
import tn.kidzone.spring.service.IUserService;

@Scope(value = "session")
@Controller(value = "frontController")
@ELBeanName(value = "frontController")
@Join(path = "/", to = "/front/index.jsf")
public class FrontController {

    @Autowired
    IUserService us;

    private String login;
    private String password;
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Date birthDate;
    private String profil;
    private String confirmPassword;
    private User user;
    private Role[] roles;
    private Role role;
    private Role role1;
    private Boolean loggedIn;
    private User authenticatedUser;
    private Date creationDate;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role[] getRoles() {
        return User.Role.values();
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole1() {
        return role1;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String doLogin() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String navigateTo = "null";
        User u = new User();
        u.setPassword(password);
        User user = us.authenticate(login, password);
        System.out.println(u);
        if (user != null) {
            navigateTo = "/front/index.xhtml?faces-redirect=true";
            loggedIn = true;
            authenticatedUser = user;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful login"));
        } else {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed",
                    "Login Failed: please check your username/password and try again.");
            FacesContext.getCurrentInstance().addMessage("form:btn-login", facesMessage);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Form validation failed"));
            PrimeFaces.current().ajax().update("form:messages");
        }
        return navigateTo;
    }

    public String doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/front/index.xhtml?faces-redirect=true";
    }

    public String doRegister() {
        String navigateTo = "/front/pages/register.xhtml";
        User u = new User(firstName, lastName, email, password, "user-1.jpg", address, birthDate, role, new Date());
        if (!password.equals(confirmPassword)) {
            FacesMessage facesMessage =

                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "Register Failed: password and confirm password must be the same.");

            FacesContext.getCurrentInstance().addMessage(":form:confirmPassword", facesMessage);
        } else {
            User saveUser = us.addUser(u);
            /*
             * String uploadDir = "user-photos/" + saveUser.getId();
             * FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
             */
            authenticatedUser = saveUser;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful Register"));
            navigateTo = "/front/index.jsf?faces-redirect=true";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful Register"));
            loggedIn = true;
            login = saveUser.getEmail();
        }
        return navigateTo;
    }

    private String passwordHashed;

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public String updateUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (authenticatedUser == null || !loggedIn)
            return "/front/index.xhtml";
        String navigateTo = "/front/pages/account.xhtml";
        if (!password.equals(confirmPassword)) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed",
                    "Update Account Failed: password and confirm password must be the same.");

            FacesContext.getCurrentInstance().addMessage(":form:confirmPassword", facesMessage);
        } else {
            if (!password.equals("")) {
                authenticatedUser.setPassword(password);
                // u.setPassword(password);
            }
            us.updateUser(authenticatedUser);
            navigateTo = "/front/index.jsf?faces-redirect=true";
        }
        return navigateTo;
    }
}
