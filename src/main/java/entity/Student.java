package entity;

import dao.DAO;

import java.util.Date;

/**
 * Created by airat on 26.04.16.
 */
public class Student {
    private long id = -1;
    private String name;
    private String lastName;
    private String patronymic;
    private Date bornDate;
    private long groupID;

    public Student(String name, String lastName, String patronymic, Date bornDate) {
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.bornDate = bornDate;
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
        return "" + bornDate.getDay() + "/" + (bornDate.getMonth() + 1) + "/" + (bornDate.getYear() + 1900);
    }

    public void setBornDate(String bornDate) {
        String[] split = bornDate.split("/");
        int year = Integer.parseInt(split[2]) - 1900;
        int month = Integer.parseInt(split[1]) - 1;
        int day = Integer.parseInt(split[0]);
        Date date = new Date(year, month, day);
        this.bornDate = date;
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
