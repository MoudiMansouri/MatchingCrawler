package Model;

import java.util.ArrayList;

public class Candidate {
    private String firstName;
    private String lastName;
    private ArrayList<Experience> experiences;
    private ArrayList<Education> educations;
    private ArrayList<Skill> skills;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public ArrayList<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(ArrayList<Experience> experiences) {
        this.experiences = experiences;
    }

    public ArrayList<Education> getEducations() {
        return educations;
    }

    public void setEducations(ArrayList<Education> educations) {
        this.educations = educations;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", experiences=" + experiences +
              //  ", educations=" + educations +
              //  ", skills=" + skills +
                '}';
    }
}
