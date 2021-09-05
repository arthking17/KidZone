package tn.kidzone.spring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import tn.kidzone.spring.entity.Request;
import tn.kidzone.spring.entity.Request.Subject;
import tn.kidzone.spring.service.IRequestService;

@Scope(value = "session")
@Controller(value = "requestController")
@ELBeanName(value = "requestController")
@Join(path = "/", to = "/front/index.jsf")
public class RequestController {

    @Autowired
    IRequestService rs;

    private String id;
    private Date creationDate;
    private String requestEmail;
    private String name;
    private String question;
    private Subject subject;
    private Subject[] subjects;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestEmail() {
        return requestEmail;
    }

    public void setRequestEmail(String requestEmail) {
        this.requestEmail = requestEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Subject[] getSubjects() {
        // Enum roles = ['PARENT', 'STUDENT'];
        return Request.Subject.values();
    }

    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void sendRequest() {
        Request request = new Request(email, name, question, subject, new Date());
        rs.addRequest(request);
        FacesMessage facesMessage = new FacesMessage("Request Sent with success.");
        FacesContext.getCurrentInstance().addMessage("form:btn", facesMessage);
        email = null;
        name = null;
        question = null;
        subject = null;
    }

    private List<Request> requests;

    public List<Request> getRequests() {
        requests = rs.getAllRequests();
        return requests;
    }

    private Request selectedRequest;
    private List<Request> selectedRequests;

    public Request getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(Request selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public List<Request> getSelectedRequests() {
        return selectedRequests;
    }

    public void setSelectedRequests(List<Request> selectedRequests) {
        this.selectedRequests = selectedRequests;
    }

    public void deleteSelectedRequest() {
        System.out.println("ddddddddddddddddddd" + this.selectedRequest);
        rs.deleteRequest(this.selectedRequest);
        this.requests.remove(this.selectedRequest);
        this.selectedRequest = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Request Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:requests");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedRequests()) {
            int size = this.selectedRequests.size();
            return size > 1 ? size + " requests selected" : "1 request selected";
        }

        return "Delete";
    }

    public boolean hasSelectedRequests() {
        return this.selectedRequests != null && !this.selectedRequests.isEmpty();
    }

    public void deleteSelectedRequests() {
        this.selectedRequests.forEach(u -> rs.deleteRequest(u));
        this.requests.removeAll(this.selectedRequests);
        this.selectedRequests = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Requests Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:requests");
        PrimeFaces.current().executeScript("PF('requestsTable').clearFilters()");
    }

    private List<FilterMeta> filterBy;

    @PostConstruct
    public void init() {
        requests = rs.getAllRequests();

        filterBy = new ArrayList<>();

        /*
         * filterBy.add(FilterMeta.builder() .field("role")
         * .filterValue(RequestStatus.NEW) .matchMode(MatchMode.EQUALS) .build());
         */

        filterBy.add(FilterMeta.builder().field("birthdayDate")
                .filterValue(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28)))
                .matchMode(MatchMode.RANGE).build());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }

        Request request = (Request) value;
        return request.getName().toLowerCase().contains(filterText)
                || request.getQuestion().toLowerCase().contains(filterText)
                || request.getEmail().toLowerCase().contains(filterText)
                || request.getId().toLowerCase().contains(filterText)
                || request.getCreatedDate().toString().toLowerCase().contains(filterText)
                || request.getSubject().name().toLowerCase().contains(filterText);
    }

    private List<Request> filteredRequests;

    public List<Request> getFilteredRequests() {
        return filteredRequests;
    }

    public void setFilteredRequests(List<Request> filteredRequests) {
        this.filteredRequests = filteredRequests;
    }
}
