package servlets;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import entity.Group;
import entity.Student;
import service.Service;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private Window errorWindow;
    @Override
    protected void init(VaadinRequest request) {
        initErrorWindow();
        VerticalLayout groupLayout = new VerticalLayout();
        VerticalLayout studentLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        groupLayout.setMargin(true);
        studentLayout.setMargin(true);
        addGroups(groupLayout, request);
        addStudents(studentLayout);
        horizontalLayout.addComponent(groupLayout);
        horizontalLayout.addComponent(studentLayout);
        setContent(horizontalLayout);

    }
    private void initErrorWindow(){
        errorWindow = new Window("Ошибка");
        errorWindow.center();
        errorWindow.setModal(true);
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(new Label("Ой что то пошло не так :("));
        layout.addComponent(new Label(" "));
        layout.addComponent(new Label("Возможные проблемы:"));
        layout.addComponent(new Label("    - Вы пытаетесь ввести некорректные данные"));
        layout.addComponent(new Label("    - Вы пытаетесь удалить или редактировать не выбрав объект"));
        layout.addComponent(new Label("    - Вы пытаетесь удалить группу в которой есть студенты"));
        layout.addComponent(new Label("    - Что то не так с базой данных"));
        errorWindow.setContent(layout);
    }

    private void addStudents(Layout studentLayout) {
        final Table table = new Table("Студенты");
        table.addContainerProperty("Фамилия", String.class, null);
        table.addContainerProperty("Имя", String.class, null);
        table.addContainerProperty("Отчество", String.class, null);
        table.addContainerProperty("Дата рождения", String.class, null);
        for (Group group : Service.getInstance().getAllGroupsWithStudent()) {
            for (Student student : group.getStudents()) {
                table.addItem(new Object[]{student.getLastName(), student.getName(), student.getPatronymic(),
                student.getBornDate()},"" + group.getId() + " " + student.getId());
            }
        }
        table.setPageLength(10);
        table.setSelectable(true);
        studentLayout.addComponent(table);
        Button addButton = new Button("Добавить");
        Button updateButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(addButton);
        horizontalLayout.addComponent(updateButton);
        horizontalLayout.addComponent(deleteButton);
        studentLayout.addComponent(horizontalLayout);
        final Window window = new Window();


        deleteButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    String id = (String) table.getValue();
                    String[] arr = id.split(" ");
                    long groupID = Long.parseLong(arr[0]);
                    long studentID = Long.parseLong(arr[1]);
                    if (Service.getInstance().removeStudent(groupID,studentID)) {
                        table.removeItem(id);
                    } else {
                        addWindow(errorWindow);
                    }
                } catch (NullPointerException e) {
                    addWindow(errorWindow);
                }

            }
        });

        VerticalLayout verticalLayout = new VerticalLayout();
         horizontalLayout = new HorizontalLayout();
        verticalLayout.setMargin(true);
        final TextField nameTextField = new TextField();
        final TextField lastNameTextField = new TextField();
        final TextField patronymicTextField = new TextField();
        final TextField bornDateTextField = new TextField();
        final ComboBox comboBox = new ComboBox();
        verticalLayout.addComponent(new Label("Фамилия:"));
        verticalLayout.addComponent(lastNameTextField);
        verticalLayout.addComponent(new Label("Имя:"));
        verticalLayout.addComponent(nameTextField);
        verticalLayout.addComponent(new Label("Отчество:"));
        verticalLayout.addComponent(patronymicTextField);
        verticalLayout.addComponent(new Label("Дата рождения(дд/мм/гггг):"));
        verticalLayout.addComponent(bornDateTextField);
        verticalLayout.addComponent(new Label("Группа:"));
        verticalLayout.addComponent(comboBox);
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");
        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        window.setContent(verticalLayout);
        window.setModal(true);
        window.center();
        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.setCaption("Добавление студента");
                initComboBox(comboBox);
                addWindow(window);
            }
        });
        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    initComboBox(comboBox);
                    window.setCaption("Редактирование студента");
                    String id = (String) table.getValue();
                    String[] arr = id.split(" ");
                    long groupID = Long.parseLong(arr[0]);
                    Item item = table.getItem(id);
                    nameTextField.setValue((String) item.getItemProperty("Имя").getValue());
                    lastNameTextField.setValue((String)item.getItemProperty("Фамилия").getValue());
                    patronymicTextField.setValue((String)item.getItemProperty("Отчество").getValue());
                    bornDateTextField.setValue((String)item.getItemProperty("Дата рождения").getValue());
                    comboBox.setValue(Service.getInstance().getGroupByID(groupID));

                    addWindow(window);
                } catch (NullPointerException e) {
                    addWindow(errorWindow);
                }

            }
        });
        cancelButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.close();
                nameTextField.clear();
                lastNameTextField.clear();
                patronymicTextField.clear();
                bornDateTextField.clear();
                comboBox.clear();
            }
        });

        okButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                String name = nameTextField.getValue();
                String lastName = lastNameTextField.getValue();
                String patronymic = patronymicTextField.getValue();
                String bornDate = bornDateTextField.getValue();
                long groupID = ((Group) comboBox.getValue()).getId();
                nameTextField.clear();
                lastNameTextField.clear();
                patronymicTextField.clear();
                bornDateTextField.clear();
                comboBox.clear();
                if("Добавление студента".equals(window.getCaption())) {
                    long id = Service.getInstance().addStudent(name, lastName, patronymic, bornDate, groupID);
                    window.close();
                    if (id == -1) {
                        addWindow(errorWindow);
                    } else {
                        table.addItem(new Object[]{lastName, name, patronymic, bornDate}, groupID + " " + id);
                    }
                }else{
                    String id = (String)table.getValue();
                    String[] arr = id.split(" ");
                    long oldGroupID = Long.parseLong(arr[0]);
                    long studentID = Long.parseLong(arr[1]);
                    window.close();
                    if (Service.getInstance().updateStudent(studentID, name, lastName, patronymic, bornDate, oldGroupID, groupID )) {
                        table.removeItem(id);
                        table.addItem(new Object[]{lastName, name, patronymic, bornDate}, groupID + " " + studentID);
                    } else {
                        addWindow(errorWindow);
                    }
                }
            }
        });

    }

    private void initComboBox(ComboBox comboBox){
        comboBox.removeAllItems();
        for (Group group : Service.getInstance().getAllGroupsWithStudent()) {
            comboBox.addItem(group);
        }
    }

    private void addGroups(Layout groupLayout, final VaadinRequest request) {
        final Table table = new Table("Группы");
        table.addContainerProperty("Название факультета", String.class, null);
        table.addContainerProperty("Номер группы", Integer.class, null);
        for (Group group : Service.getInstance().getAllGroupsWithStudent()) {
            table.addItem(new Object[]{group.getDepartmentName(), group.getGroupNumber()}, group.getId());
        }
        table.setPageLength(10);
        table.setSelectable(true);
        groupLayout.addComponent(table);
        Button addButton = new Button("Добавить");
        Button updateButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(addButton);
        horizontalLayout.addComponent(updateButton);
        horizontalLayout.addComponent(deleteButton);
        groupLayout.addComponent(horizontalLayout);
        final Window window = new Window();
        initGroupWindow(window, table, updateButton);

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.setCaption("Добавление группы");
                addWindow(window);
            }
        });
        deleteButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                try{
                    long id = (Long)table.getValue();
                    if(Service.getInstance().removeGroup(id)){
                        table.removeItem(id);
                    }else{
                        addWindow(errorWindow);
                    }
                }catch (NullPointerException e){
                    addWindow(errorWindow);
                }

            }
        });
    }

    private void initGroupWindow(final Window window, final Table table, Button updateButton) {
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        verticalLayout.setMargin(true);
        final TextField departmentTextField = new TextField();
        final TextField groupNumberTextField = new TextField();
        verticalLayout.addComponent(new Label("Название факультета:"));
        verticalLayout.addComponent(departmentTextField);
        verticalLayout.addComponent(new Label("Номер группы:"));
        verticalLayout.addComponent(groupNumberTextField);
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");
        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        window.setContent(verticalLayout);
        window.setModal(true);
        window.center();

        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    window.setCaption("Редактирование группы");

                    long id = (Long) table.getValue();
                    Item item = table.getItem(id);
                    departmentTextField.setValue((String) item.getItemProperty("Название факультета").getValue());
                    groupNumberTextField.setValue(String.valueOf(item.getItemProperty("Номер группы").getValue()));
                    addWindow(window);
                } catch (NullPointerException e) {
                    addWindow(errorWindow);
                }

            }
        });
        cancelButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.close();
                departmentTextField.clear();
                groupNumberTextField.clear();
            }
        });

        okButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                String department = departmentTextField.getValue();
                int groupNumber = Integer.parseInt(groupNumberTextField.getValue());
                departmentTextField.clear();
                groupNumberTextField.clear();
                if("Добавление группы".equals(window.getCaption())) {
                    long id = Service.getInstance().addGroup(department, groupNumber);
                    window.close();
                    if (id == -1) {
                        addWindow(errorWindow);
                    } else {
                        table.addItem(new Object[]{department, groupNumber}, id);
                    }
                }else{
                    long id = (Long)table.getValue();
                    window.close();
                    if (Service.getInstance().updateGroup(id, department, groupNumber)) {
                        table.removeItem(id);
                        table.addItem(new Object[]{department, groupNumber}, id);
                    } else {
                        addWindow(errorWindow);
                    }
                }
            }
        });


    }
}