package dao;

import entity.Group;
import entity.Student;

import java.sql.*;
import java.util.LinkedList;
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
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
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
            getMaxID();
        } catch (SQLException e) {
            try {
                createTables();
                getMaxID();
            } catch (SQLException e1) {
                throw new DAOException(e1.getMessage());
            }

        }

    }

    private void createTables() throws SQLException {
        statement.execute("create table learn_group(\n" +
                "    id bigint primary key,\n" +
                "    group_number integer not null,\n" +
                "    dept_name varchar(100) not null\n" +
                ")");
        statement.execute("create table student(\n" +
                "    id bigint primary key,\n" +
                "    name varchar(50) not null,\n" +
                "    last_name varchar(50) not null,\n" +
                "    patronymic varchar(50) not null,\n" +
                "    born_date date not null,\n" +
                "    group_id bigint,\n" +
                "    foreign key (group_id) references learn_group(id)\n" +
                ")");
    }

    private void getMaxID() throws SQLException {
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select max(id) as max_id from learn_group");
        if(resultSet.next()){
            maxGroupID = resultSet.getLong("max_id");
        }
        resultSet.close();
        resultSet = statement.executeQuery("select max(id) as max_id from student");
        if(resultSet.next()){
            maxStudentID = resultSet.getLong("max_id");
        }
        resultSet.close();
    }


    public List<Group> getAllGroupsWithStudents() throws DAOException {
        LinkedList<Group> groups = new LinkedList<Group>();
        try {
            ResultSet resultSet = statement.executeQuery("select * from learn_group");
            while(resultSet.next()){
                Group group = new Group(resultSet.getString("dept_name"), resultSet.getInt("group_number"));
                group.setId(resultSet.getLong("id"));
                groups.add(group);
                ResultSet studentResultSet = statement.executeQuery("select * from student where group_id = " + group.getId());
                while (studentResultSet.next()){
                    Student student =
                            new Student(studentResultSet.getString("name"), studentResultSet.getString("last_name"),
                                    studentResultSet.getString("patronymic"), studentResultSet.getDate("born_date"));
                    student.setId(studentResultSet.getLong("id"));
                    group.addStudent(student);
                }
                studentResultSet.close();
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return groups;
    }

    public void updateStudent(Student student) throws DAOException {
        try {
            statement.execute("update student set "
                                + " name = '" + student.getName()
                                + "', last_name = '" + student.getLastName()
                                + "', patronymic = '" + student.getPatronymic()
                                + "', born_date = to_date('" + student.getBornDate() +"', 'DD/MM/YYYY')"
                                + ", group_id = " + student.getGroupID()
                                + " where id = " +student.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public void updateGroup(Group group) throws DAOException {
        try {
            statement.execute("update learn_group set "
                    + " dept_name = '" + group.getDepartmentName()
                    + "', group_number = " + group.getGroupNumber()
                    + " where id = " + group.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public synchronized void addStudent(Student student) throws DAOException {
        try {
            statement.execute("insert into student (id, name, last_name, patronymic, born_date, group_id) values " +
                    "(" + ++maxStudentID + ",'" + student.getName() + "','" + student.getLastName() + "','" + student.getPatronymic()
                    + "', to_date('" + student.getBornDate() +"', 'DD/MM/YYYY')" + "," + student.getGroupID() + ")");
            student.setId(maxStudentID);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public synchronized void addGroup(Group group) throws DAOException {
        try {
            statement.execute("insert into learn_group (id, group_number, dept_name) values("+
                    + ++maxGroupID + ", " + group.getGroupNumber() + ", '" + group.getDepartmentName() + "')");
            group.setId(maxGroupID);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public void removeStudent(Student student) throws DAOException {
        try {
            statement.execute("delete from student where id = " + student.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public void removeGroup(Group group) throws DAOException {
        try {
            statement.execute("delete from learn_group where id = " + group.getId());
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
}
