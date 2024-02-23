package com.zelda.model.entity;


import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private Long countPost = 0L;

    public Category() {
    }

    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Category(Long id, String name, String description, Role role, Long countPost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.role = role;
        this.countPost = countPost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getCountPost() {
        return countPost;
    }

    public void setCountPost(Long countPost) {
        this.countPost = countPost;
    }
}
