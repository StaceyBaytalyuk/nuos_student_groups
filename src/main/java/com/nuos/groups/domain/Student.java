package com.nuos.groups.domain;

import javax.persistence.*;

@Entity // сущность, которая будет храниться в БД
@Table(name = "Students")
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 20)
    private String name;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    public Student(String name) {
        this.name = name;
    }

    public Student() {}

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group.getName() +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
