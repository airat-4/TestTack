package entity;

import dao.DAO;

import java.util.ArrayList;

/**
 * Created by airat on 26.04.16.
 */
public class Group {
    private long id = -1;
    private int groupNumber;
    private String departmentName;
    private ArrayList<Student> students = new ArrayList<Student>();

    public Group(String departmentName, int groupNumber) {
        this.groupNumber = groupNumber;
        this.departmentName = departmentName;
    }

    public void addStudent(Student student){
        student.setGroupID(id);
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public Student getStudent(long studentID){
        for (Student student : students) {
            if (student.getId() == studentID)
                return student;
        }
        return null;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return getDepartmentName() + " " + getGroupNumber() + "гр.";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != group.id) return false;
        if (groupNumber != group.groupNumber) return false;
        return departmentName.equals(group.departmentName);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + groupNumber;
        result = 31 * result + departmentName.hashCode();
        return result;
    }
}
