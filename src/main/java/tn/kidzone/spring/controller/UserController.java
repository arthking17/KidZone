package tn.kidzone.spring.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import tn.kidzone.spring.entity.User;
import tn.kidzone.spring.entity.Request.Subject;
import tn.kidzone.spring.entity.User.Role;
import tn.kidzone.spring.service.IRequestService;
import tn.kidzone.spring.service.IUserService;

@Scope(value = "session")
@Controller(value = "userController")
@ELBeanName(value = "userController")
@Join(path = "/back/index.jsf", to = "/back/index.jsf")
public class UserController {

	@Autowired
	IUserService us;

	@Autowired
	IRequestService rs;

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
	private Boolean loggedIn;
	private User authenticatedUser;
	private Date creationDate;

	private List<User> users;

	private User selectedUser;

	private List<User> selectedUsers;

	private UploadedFile originalImageFile;

	private List<User> filteredUsers;

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

	public Role[] getRoles() {
		return User.Role.values();
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<User> getUsers() {
		users = us.getAllUsers();
		return users;
	}

	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers1(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	private String profilConnected;

	public String getProfilConnected() {
		return profilConnected;
	}

	public void setProfilConnected(String profilConnected) {
		this.profilConnected = profilConnected;
	}

	public String doLogin() throws NoSuchAlgorithmException, InvalidKeySpecException {
		String navigateTo = "null";
		User u = new User();
		u.setPassword(password);
		User user = us.authenticate(login, password);
		System.out.println(u);
		if (user != null && user.getRole() == Role.ADMIN) {
			navigateTo = "/back/index.xhtml?faces-redirect=true";
			loggedIn = true;
			authenticatedUser = user;
		} else {
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed",
					"Login Failed: please check your username/password and try again.");
			FacesContext.getCurrentInstance().addMessage("form:btn", facesMessage);
		}
		return navigateTo;
	}

	public String doLogout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces-redirect=true";
	}

	public String doRegister() {
		String navigateTo = "/register.xhtml";
		User u = new User(firstName, lastName, email, password, "user-1.jpg", address, birthDate, role, new Date());
		if (!password.equals(confirmPassword)) {
			FacesMessage facesMessage =

					new FacesMessage("Register Failed: password and confirm password must be the same.");

			FacesContext.getCurrentInstance().addMessage("form:btn", facesMessage);
		} else {
			User saveUser = us.addUser(u);
			/*
			 * String uploadDir = "user-photos/" + saveUser.getId();
			 * FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			 */

			navigateTo = "/front/index.jsf?faces-redirect=true";
			authenticatedUser = saveUser;
			loggedIn = true;
		}
		return navigateTo;
	}

	public String showAddUserForm() {
		String navigateTo = "/back/pages/users/add.xhtml?faces-redirect=true";
		User u = new User();
		this.setId(u.getId());
		this.setAddress(u.getAddress());
		this.setBirthDate(u.getBirthdayDate());
		this.setEmail(u.getEmail());
		this.setFirstName(u.getFirstName());
		this.setLastName(u.getLastName());
		this.setRole(u.getRole());
		this.setCreationDate(u.getCreatedDate());
		this.setPasswordHashed(u.getPassword());
		return navigateTo;
	}

	public String addUser() throws IOException {
		if (authenticatedUser == null || !loggedIn || authenticatedUser.getRole() != Role.ADMIN)
			return "/login.xhtml";
		System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + fileUpload);
		String navigateTo = "/back/pages/users/add.xhtml";
		User u = new User(firstName, lastName, email, password, "user-1.jpg", address, birthDate, role, new Date());
		System.out.println(this.originalImageFile);
		Path src = Paths.get("");
		Path dst = Paths.get("");
		Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
		User saveUser = us.addUser(u);
		navigateTo = "/back/pages/users/index.xhtml?faces-redirect=true";
		return navigateTo;
	}

	public String deleteUser() {
		if (authenticatedUser == null || !loggedIn || authenticatedUser.getRole() != Role.ADMIN)
			return "/login.xhtml";
		String navigateTo = "/back/pages/users/edit.xhtml";
		System.out.println(this.originalImageFile);
		us.deleteUser(user);
		navigateTo = "/back/pages/users/index.xhtml?faces-redirect=true";
		return navigateTo;
	}

	UploadedFile fileUpload;

	public UploadedFile getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(UploadedFile fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String editUser(User u) {
		// if (authenticatedUser == null || !loggedIn || authenticatedUser.getRole() !=
		// Role.ADMIN)
		// return "/login.xhtml";
		String navigateTo = "/back/pages/users/edit.xhtml?faces-redirect=true";
		// User u = us.getUser(this.selectedUser.getId());
		this.setId(u.getId());
		this.setAddress(u.getAddress());
		this.setBirthDate(u.getBirthdayDate());
		this.setEmail(u.getEmail());
		this.setFirstName(u.getFirstName());
		this.setLastName(u.getLastName());
		this.setRole(u.getRole());
		this.setCreationDate(u.getCreatedDate());
		this.setPasswordHashed(u.getPassword());
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
		if (authenticatedUser == null || !loggedIn || authenticatedUser.getRole() != Role.ADMIN)
			return "/login.xhtml";
		String navigateTo = "/back/pages/users/edit.xhtml";
		User u = new User();
		if (password.equals("")) {
			u = new User(id, firstName, lastName, email, passwordHashed, "user-1.jpg", address, birthDate, role,
					creationDate);
		} else {
			u = new User(id, firstName, lastName, email, password, "user-1.jpg", address, birthDate, role,
					creationDate);
			u.setPassword(password);
		}
		System.out.println(this.originalImageFile);
		us.updateUser(u);
		navigateTo = "/back/pages/users/index.xhtml?faces-redirect=true";
		return navigateTo;
	}

	public UploadedFile getOriginalImageFile() {
		return originalImageFile;
	}

	public void handleFileUpload(FileUploadEvent event) {
		this.originalImageFile = null;
		UploadedFile file = event.getFile();
		fileUpload = event.getFile();
		System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + file);
		if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
			this.originalImageFile = file;
			FacesMessage msg = new FacesMessage("Successful", this.originalImageFile.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public void openNew() {
		this.selectedUser = new User();
	}

	public void saveUser() {
		if (this.selectedUser.getId() == null) {
			this.users.add(this.selectedUser);
			User saveUser = us.addUser(this.selectedUser);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Added"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
		}

		PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:users");
	}

	public void deleteSelectedUser() {
		System.out.println("ddddddddddddddddddd" + this.selectedUser);
		us.deleteUser(this.selectedUser);
		this.users.remove(this.selectedUser);
		this.selectedUser = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:users");
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedUsers()) {
			int size = this.selectedUsers.size();
			return size > 1 ? size + " users selected" : "1 user selected";
		}

		return "Delete";
	}

	public boolean hasSelectedUsers() {
		return this.selectedUsers != null && !this.selectedUsers.isEmpty();
	}

	public void deleteSelectedUsers() {
		this.selectedUsers.forEach(u -> us.deleteUser(u));
		this.users.removeAll(this.selectedUsers);
		this.selectedUsers = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Users Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:users");
		PrimeFaces.current().executeScript("PF('usersTable').clearFilters()");
	}

	private List<FilterMeta> filterBy;

	@PostConstruct
	public void init() {
		users = us.getAllUsers();

		filterBy = new ArrayList<>();

		/*
		 * filterBy.add(FilterMeta.builder() .field("role") .filterValue(UserStatus.NEW)
		 * .matchMode(MatchMode.EQUALS) .build());
		 */

		filterBy.add(FilterMeta.builder().field("birthdayDate")
				.filterValue(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28)))
				.matchMode(MatchMode.RANGE).build());

		filterBy.add(FilterMeta.builder().field("creationDate")
				.filterValue(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28)))
				.matchMode(MatchMode.RANGE).build());

		createUserStats();
		createRequestStats();
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isValueBlank(filterText)) {
			return true;
		}

		User user = (User) value;
		return user.getId().toLowerCase().contains(filterText) || user.getFirstName().toLowerCase().contains(filterText)
				|| user.getLastName().toLowerCase().contains(filterText)
				|| user.getEmail().toLowerCase().contains(filterText)
				|| user.getAddress().toLowerCase().contains(filterText)
				|| user.getBirthdayDate().toString().toLowerCase().contains(filterText)
				|| user.getRole().name().toLowerCase().contains(filterText)
				|| user.getCreatedDate().toString().toLowerCase().contains(filterText);
	}

	private int newRequests;
	private int newUsers;
	private int newVisitors;
	private int numberOfUsers;

	public int getNewRequests() {
		return rs.NumberOfRecentRequests();
	}

	public int getNewUsers() {
		return us.NumberOfRecentUsers();
	}

	public int getNewVisitors() {
		return us.NumberOfRecentVisitors();
	}

	public int getNumberOfUsers() {
		return us.NumberOfUsers();
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	private BarChartModel userStats;

	public BarChartModel getUserStats() {
		return userStats;
	}

	public void setUserStats(BarChartModel userStats) {
		this.userStats = userStats;
	}

	public void createUserStats() {
		userStats = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet dataSet = new BarChartDataSet();
        dataSet.setLabel("User Statistics");

		List<Number> values = new ArrayList<>();
		values.add(us.Numberof(role.ADMIN));
		values.add(us.Numberof(role.PARENT));
		values.add(us.Numberof(role.VISITOR));
		values.add(us.Numberof(role.DIRECTOR));
		values.add(us.Numberof(role.STUDENT));
		values.add(us.Numberof(role.DOCTOR));
		values.add(us.Numberof(role.TEACHER));
		values.add(us.Numberof(role.DRIVER));
		dataSet.setData(values);

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb(255, 99, 132)");
		bgColors.add("rgb(54, 162, 235)");
		bgColors.add("rgb(255, 205, 86)");
		bgColors.add("rgb(100, 36, 44)");
		bgColors.add("rgb(190, 197, 170)");
		bgColors.add("rgb(84, 165, 147)");
		bgColors.add("rgb(47, 44, 76)");
		bgColors.add("rgb(160,81,149)");
		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		List<String> labels = new ArrayList<>();
		labels.add("ADMIN");
		labels.add("PARENT");
		labels.add("VISITOR");
		labels.add("DIRECTOR");
		labels.add("STUDENT");
		labels.add("DOCTOR");
		labels.add("TEACHER");
		labels.add("DRIVER");
		data.setLabels(labels);

		userStats.setData(data);
		//Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Number of Users By Role");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);

        // disable animation
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);

        userStats.setOptions(options);
	}

	private DonutChartModel requestStats;

	public DonutChartModel getRequestStats() {
		return requestStats;
	}

	public void setRequestStats(DonutChartModel requestStats) {
		this.requestStats = requestStats;
	}

	public void createRequestStats() {
		requestStats = new DonutChartModel();
		ChartData data = new ChartData();

		DonutChartDataSet dataSet = new DonutChartDataSet();

		List<Number> values = new ArrayList<>();
		values.add(rs.Numberof(subject.INFORMATION));
		values.add(rs.Numberof(subject.COMPLAINT));
		dataSet.setData(values);

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb(54, 162, 235)");
		bgColors.add("rgb(255, 99, 132)");
		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		List<String> labels = new ArrayList<>();
		labels.add("INFORMATION");
		labels.add("COMPLAINT");
		data.setLabels(labels);

		requestStats.setData(data);
	}
    private Subject subject;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}
