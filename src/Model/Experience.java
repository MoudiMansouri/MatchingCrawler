package Model;

import java.util.Date;

public class Experience {
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
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", institution='" + institution + '\'' +
                '}';
    }
}
