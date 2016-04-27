package dao;

import entity.Group;
import entity.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by airat on 27.04.16.
 */
public class HSQLDBDAO implements DAO{
    private Connection connection;
    private Statement statement;
    private long maxGroupID;
    private long maxStudentID;

    public HSQLDBDAO() throws DAOException {
        try {
            Class.forName("org.hsqldb.JDBCDriver");
        } catch (ClassNotFoundException e) {
            throw new DAOException("Неудадось загрузить драйвер. " + e.getMessage());
        }
        try {
            String path = "db/";
            String dbName = "test_task";
            String connectionString = "jdbc:hsqldb:file:"+path+dbName;
            String login = "SA";
            String password = "";
            connection = DriverManager.getConnection(connectionString, login, password);

        } catch (SQLException e) {
            throw new DAOException("Неудадось подключится к БД. " + e.getMessage());
        }
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

    }


    public List<Group> getAllGroupsWithStudents() throws DAOException {
        return null;
    }

    public void updateStudent(Student student) throws DAOException {

    }

    public void updateGroup(Group group) throws DAOException {

    }

    public void addStudent(Student student) throws DAOException {

    }

    public void addGroup(Group group) throws DAOException {

    }

    public void removeStudent(Student student) throws DAOException {

    }

    public void removeGroup(Group group) throws DAOException {

    }
}
