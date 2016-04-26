package dao;

import entity.Group;
import entity.Student;

import java.util.List;

/**
 * Created by airat on 26.04.16.
 */
public interface DAO {
    List<Group> getAllGroupsWithStudents() throws DAOException;
    void updateStudent(Student student) throws DAOException;
    void updateGroup(Group group) throws DAOException;
    void addStudent(Student student) throws DAOException;
    void addGroup(Group group) throws DAOException;
    void removeStudent(Student student) throws DAOException;
    void removeGroup(Group group) throws DAOException;
}
