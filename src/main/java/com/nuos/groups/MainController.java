package com.nuos.groups;

import com.nuos.groups.domain.Group;
import com.nuos.groups.domain.Student;
import com.nuos.groups.repos.GroupRepo;
import com.nuos.groups.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Objects;

@Controller
public class MainController {
    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("main")
    public String main(Map<String, Object> model) {
        showAll(model);
        return "main";
    }

    @PostMapping("addGroup")
    public String addGroup(@RequestParam String universityGroups, Map<String, Object> model) {
        if ( !universityGroups.isEmpty() ) {
            if ( groupRepo.findByName(universityGroups) == null ) { // проверка на повтор имён
                Group group = new Group(universityGroups);
                groupRepo.save(group);
                printMessage(group+" is successfully added");
            } else {
                printMessage(universityGroups+" already exists, can't add");
            }
        } else {
            printMessage("Please, enter the name of group to add");
        }

        showAll(model);
        return "main";
    }

    @PostMapping("renameGroup")
    public String renameGroup(@RequestParam String oldName, @RequestParam String newName, Map<String, Object> model) {
        if ( (!oldName.isEmpty()) && (!newName.isEmpty()) ) { // новое и старое имя не пустые

            Group groupToRename = groupRepo.findByName(oldName);
            if ( groupToRename != null ) {
                Iterable<Group> groups = groupRepo.findAll();
                boolean exists = false; // проверка на повтор имён
                for (Group group: groups) {
                    if ( group.getName().equals(newName) ) {
                        exists = true;
                        break;
                    }
                }

                if ( !exists ) {
                    groupToRename.setName(newName);
                    groupRepo.save(groupToRename);
                    printMessage(groupToRename.getId()+" is successfully renamed from "+oldName+" to "+newName);
                } else {
                    printMessage(newName+" already exists, can't rename");
                }
            } else {
                printMessage(oldName+" doesn't exist, can't rename");
            }

        } else {
            printMessage("Please, enter all fields to rename group");
        }

        showAll(model);
        return "main";
    }

    @PostMapping("deleteGroup")
    public String deleteGroup(@RequestParam String groupName, Map<String, Object> model) {
        if ( !groupName.isEmpty() ) {

            Group group = groupRepo.findByName(groupName);
            if ( group != null ) {

                if ( isEmptyGroup(groupName) ) {
                    groupRepo.delete(group);
                    printMessage(group+" is successfully deleted");
                } else {
                    printMessage(group+" is not empty, can't delete");
                }

            } else {
                printMessage(groupName+" doesn't exist, can't delete");
            }

        } else {
            printMessage("Please, enter the name of group to delete");
        }

        showAll(model);
        return "main";
    }

    @PostMapping("deleteGroupById")
    public String deleteGroupById(@RequestParam Integer id, Map<String, Object> model) {
        if ( id != null ) {

            if ( groupRepo.findById(id).isPresent() ) { // если существует такая группа

                if ( isEmptyGroup(id) ) { // удаляем только пустые группы
                    groupRepo.deleteById(id);
                    printMessage(id+" is successfully deleted");
                } else {
                    printMessage(id+" is not empty, can't delete");
                }

            } else {
                printMessage(id+" doesn't exist, can't delete");
            }

        } else {
            printMessage("Please, enter id of the group to delete");
        }

        showAll(model);
        return "main";
    }


    //TODO ПРОВЕРКА ПУСТЫХ ГРУПП И ИМЁН ""
    @PostMapping("addStudent")
    public String addStudent(@RequestParam String studentName, @RequestParam String groupName, Map<String, Object> model) {
        if ( (!studentName.isEmpty()) && (!groupName.isEmpty()) ) {
            Group group = groupRepo.findByName(groupName);
            if ( group == null ) { // созд группу если еще не существует
                group = new Group(groupName);
                groupRepo.save(group);
                printMessage(group+" is created");
            }
            studentRepo.save(new Student(studentName, group));
            printMessage(studentName+" is successfully added");
        } else {
            printMessage("Please, enter all fields to add student");
        }

        showAll(model);
        return "main";
    }

    @PostMapping("renameStudent")
    public String renameStudent(@RequestParam Integer id, @RequestParam String newName, Map<String, Object> model) {
        if ( (id!=null) && (!newName.isEmpty()) ) {

            if ( studentRepo.findById(id).isPresent() ) {
                Iterable<Student> students = studentRepo.findAll();
                Student studentToRename;
                for (Student student : students) {
                    if ( student.getId().equals(id) ) { // существует
                        studentToRename = student;
                        String oldName = studentToRename.getName();
                        studentToRename.setName(newName);
                        studentRepo.save(studentToRename);
                        printMessage(studentToRename.getId()+" is successfully renamed from "+oldName+" to "+newName);
                        break;
                    }
                }
            } else {
                printMessage(id+" doesn't exist, can't rename");
            }

        } else {
            printMessage("Please, enter all fields to rename student");
        }

        showAll(model);
        return "main";
    }

/*    @PostMapping("deleteStudent")
    public String deleteStudent(@RequestParam String studentName, Map<String, Object> model) {
        Student student = studentRepo.findByName(studentName);
        if ( student != null ) {
            studentRepo.delete(student);
        }
        showAll(model);
        return "main";
    }*/

// БЫВАЮТ ОДНОФАМИЛЬЦЫ, ПОЭТОМУ УДАЛЯТЬ ТОЛЬКО ПО id

    @PostMapping("deleteStudentById")
    public String deleteStudentById(@RequestParam Integer id, Map<String, Object> model) {
        if ( id != null ) {
            if ( studentRepo.findById(id).isPresent() ) { // если существует
                studentRepo.deleteById(id);
                printMessage(id+" is successfully deleted");
            } else {
                printMessage(id+" doesn't exist, can't delete");
            }
        } else {
            printMessage("Please, enter id of the student to delete");
        }

        showAll(model);
        return "main";
    }



    private void showAll(Map<String, Object> model) {
        Iterable<Group> groups = groupRepo.findAll();
        model.put("universityGroups", groups);

        Iterable<Student> students = studentRepo.findAll();
        model.put("students", students);
    }

    private void printMessage(String message) {
        System.out.println("=================================");
        System.out.println(message);
        System.out.println("=================================");
    }

    private boolean isEmptyOldName(String name) {
        if ( name.isEmpty() ) {
            printMessage("Empty names are not allowed, can't rename");
            return true;
        }
        return false;
    }

    private boolean isEmptyNewName(String name) {
        if ( name.isEmpty() ) {
            printMessage("Can't rename to empty name");
            return true;
        }
        return false;
    }

    private boolean isEmptyGroup(Integer id) {
        Iterable<Student> students = studentRepo.findAll();
        for (Student student : students) {
            if ( Objects.equals(student.getGroup().getId(), id) ) { // нашли студента с такой группой
                return false;
            }
        }
        return true;
    }

    private boolean isEmptyGroup(String name) {
        Iterable<Student> students = studentRepo.findAll();
        for (Student student : students) {
            if ( Objects.equals(student.getGroup().getName(), name) ) { // нашли студента с такой группой
                return false;
            }
        }
        return true;
    }

}