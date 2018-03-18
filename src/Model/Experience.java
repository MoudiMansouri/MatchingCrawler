package Model;

import java.util.Date;

public class Experience {
    private Date startDate;
    private Date endDate;
    private String role;
    private String description;
    private String institution;

    public Experience() {
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", institution='" + institution + '\'' +
                '}';
    }
}
