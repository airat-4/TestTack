package servlets;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import entity.Group;
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
        addGStudents(studentLayout);
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
        errorWindow.setContent(layout);
    }

    private void addGStudents(Layout studentLayout) {
    }

    private void addGroups(Layout groupLayout, final VaadinRequest request) {
        Table table = new Table("Группы");
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
        initGroupWindow(window, table);

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.setCaption("Добавить группу");
                addWindow(window);
            }
        });
        updateButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.setCaption("Редактировать группу");
                addWindow(window);
            }
        });
    }

    private void initGroupWindow(final Window window, final  Table table) {
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
        cancelButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.close();
            }
        });

        okButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                String department = departmentTextField.getValue();
                int groupNumber = Integer.parseInt(groupNumberTextField.getValue());
                departmentTextField.clear();
                groupNumberTextField.clear();
                if("Добавить группу".equals(window.getCaption())) {
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