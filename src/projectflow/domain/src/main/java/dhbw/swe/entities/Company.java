package dhbw.swe.entities;

import dhbw.swe.valueobjects.Money;
import dhbw.swe.valueobjects.TimeRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Company {

    private final UUID id;
    private String name;
    private Money budget;
    private final List<Project> projects;
    private final List<Resource> resources;

    public Company(UUID id, String name, Money budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.projects = new ArrayList<>();
        this.resources = new ArrayList<>();
    }

    public Project createProject(String name, Money projectMoney, TimeRange duration) {
        if (budget.compareTo(projectMoney) < 0) {
            throw new IllegalArgumentException("project budget exceeds company budget");
        }
        Project project = new Project(UUID.randomUUID(), name, projectMoney, duration);
        projects.add(project);
        return project;
    }

    public void removeProject(Project project) {
        projects.remove(project);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getMoney() {
        return budget;
    }

    public List<Project> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }
}
