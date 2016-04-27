package service;

import dao.DAO;
import dao.DAOException;
import dao.HSQLDBDAO;
import entity.Group;
import entity.Student;

import java.util.*;

/**
 * Created by airat on 26.04.16.
 */
public class Service {
    private static Service service;

    private DAO dao;
    private HashMap<Long, Group> allGroups = new HashMap<Long, Group>();

    private Service() throws DAOException {
        dao = new HSQLDBDAO();
        List<Group> allGroupsWithStudents = dao.getAllGroupsWithStudents();
        for (Group currentGroup : allGroupsWithStudents) {
            allGroups.put(currentGroup.getId(), currentGroup);
        }
    }

    public synchronized static Service getInstance(){
        if(service == null)
            try {
                service = new Service();
            } catch (DAOException e) {
                e.printStackTrace();
            }

        return service;
    }

    public Collection<Group> getAllGroupsWithStudent(){
        return allGroups.values();
    }

    public long addGroup(String departmentName, int groupNumber){
        if(departmentName == null || departmentName.trim().equals("") || groupNumber <= 0)
            return -1;
        Group group = new Group(departmentName.trim(), groupNumber);
        try {
            dao.addGroup(group);
            allGroups.put(group.getId(), group);
            return group.getId();
        } catch (DAOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean updateGroup(long groupID, String departmentName, int groupNumber){
        Group group = allGroups.get(groupID);
        if(departmentName == null || departmentName.trim().equals("") || groupNumber <= 0 || group == null)
            return false;
        int oldGroupNumber = group.getGroupNumber();
        String oldDepartmentName = group.getDepartmentName();
        group.setDepartmentName(departmentName.trim());
        group.setGroupNumber(groupNumber);
        try {
            dao.updateGroup(group);
            return true;
        } catch (DAOException e) {
            e.printStackTrace();
            group.setDepartmentName(oldDepartmentName);
            group.setGroupNumber(oldGroupNumber);
            return false;
        }

    }

    public boolean removeGroup(long groupID){
        try {
            Group group = allGroups.get(groupID);
            if(group == null)
                return false;
            dao.removeGroup(group);
            allGroups.remove(groupID);
            return true;
        } catch (DAOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long addStudent(String name, String lastName, String patronymic, Date bornDate, long groupID){
        Group group = allGroups.get(groupID);
        if(group == null || name == null || lastName == null || patronymic == null || bornDate == null
                || name.trim().equals("") || lastName.trim().equals("") || patronymic.trim().equals(""))
            return -1;
        Student student = new Student(name.trim(), lastName.trim(), patronymic.trim(), bornDate);
        group.addStudent(student);
        try {
            dao.addStudent(student);
            return student.getId();
        } catch (DAOException e) {
            e.printStackTrace();
            group.removeStudent(student);
            return -1;
        }
    }

    public boolean updateStudent(long studentID, String name, String lastName, String patronymic, String bornDate, long oldGroupID, long newGroupID){
        Group oldGroup = allGroups.get(oldGroupID);
        Group newGroup = allGroups.get(newGroupID);

        if(oldGroup == null || newGroup == null || name == null || lastName == null || patronymic == null || bornDate == null
                || name.trim().equals("") || lastName.trim().equals("") || patronymic.trim().equals(""))
            return false;
        Student student = oldGroup.getStudent(studentID);
        if(student == null)
            return false;
        String oldName = student.getName();
        String oldLastName = student.getLastName();
        String oldPatronymic = student.getPatronymic();
        String oldBornDate = student.getBornDate();
        student.setName(name);
        student.setLastName(lastName);
        student.setPatronymic(patronymic);
        student.setBornDate(bornDate);
        oldGroup.removeStudent(student);
        newGroup.addStudent(student);
        try {
            dao.updateStudent(student);
            return true;
        } catch (DAOException e) {
            e.printStackTrace();
            student.setName(oldName);
            student.setLastName(oldLastName);
            student.setPatronymic(oldPatronymic);
            student.setBornDate(oldBornDate);
            newGroup.removeStudent(student);
            oldGroup.addStudent(student);
            return false;
        }
    }

    public boolean removeStudent(long groupID, long studentID){
        Group group = allGroups.get(groupID);
        if(group == null)
            return false;
        Student student = group.getStudent(studentID);
        if(student == null)
            return false;
        try {
            dao.removeStudent(student);
            group.removeStudent(student);
            return true;
        } catch (DAOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws DAOException {
        Service service = new Service();
    }



}
