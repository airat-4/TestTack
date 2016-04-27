package entity;

import dao.DAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by airat on 26.04.16.
 */
public class Student {
    private long id = -1;
    private String name;
    private String lastName;
    private String patronymic;
    private String bornDate;
    private long groupID;

    public Student(String name, String lastName, String patronymic, String bornDate) {
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.bornDate = bornDate;
    }

    public Student(String name, String lastName, String patronymic, Date bornDate) {
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        this.bornDate = simpleDateFormat.format(bornDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public long getGroupID() {
        return groupID;
    }

    void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (groupID != student.groupID) return false;
        if (!name.equals(student.name)) return false;
        if (!lastName.equals(student.lastName)) return false;
        if (!patronymic.equals(student.patronymic)) return false;
        return bornDate.equals(student.bornDate);

    }

}
