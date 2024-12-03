package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Order;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import service.order.OrderService;
import service.user.admin.AdminService;
import view.AdminView;

import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static database.Constants.Roles.ROLES;
//import static launcher.ComponentFactory.componentFactory;

public class AdminController {
    private final AdminView adminView;
    private final AdminService adminService;
    private final OrderService orderService;
    private Notification<User> user;


//    public AdminController(AdminView adminView, ComponentFactory componentFactory, Notification<User> user) {
//        this.adminView = adminView;
//        this.componentFactory = componentFactory;
//        this.user = user;
//
//        List<User> users = componentFactory.getAdministratorService().findAll();
//        //ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
//        adminView.setListOfUsers(users);
//
//        this.adminView.addCreateUserButtonListener(new AdminController.CreateUserButtonListener());
//        this.adminView.addDeleteUserButtonListener(new AdminController.DeleteUserButtonListener());
//        this.adminView.addCreateUserButtonListener(new AdminController.PDFButtonListener());
//
//    }
public AdminController(AdminView adminView, AdminService adminService, Notification<User> user, OrderService orderService) {
        this.adminView = adminView;
        this.adminService=adminService;
        this.user = user;
        this.orderService=orderService;

        List<User> users = adminService.findAll();
        adminView.setListOfUsers(users);

        this.adminView.addCreateUserButtonListener(new AdminController.CreateUserButtonListener());
        this.adminView.addDeleteUserButtonListener(new AdminController.DeleteUserButtonListener());
       this.adminView.addPdfButtonListener(new AdminController.PDFButtonListener());

    }


    private class CreateUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            User newEmployee = adminView.getNewEmployee();
            Notification<Boolean> registerEmployeeNotification=adminService.register(newEmployee.getUsername(), newEmployee.getPassword(),EMPLOYEE);
            if ((registerEmployeeNotification.hasErrors())){
                adminView.addDisplayAlertMessage("Create Error", "Problem of Username or Password field", "Try again!");
            }else{
                adminView.addDisplayAlertMessage("Create Successful", "User Added", "The user was successfully added!");
            }

            List<User> users = adminService.findAll(); // Recuperează utilizatorii actualizați
            adminView.setListOfUsers(users); // Actualizează tabelul
        }

    }


    private class DeleteUserButtonListener implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            User selectedUser = adminView.getUserTableView().getSelectionModel().getSelectedItem();

            if (selectedUser == null) {
                adminView.addDisplayAlertMessage(
                        "Delete Error",
                        "No User Selected",
                        "Please select a user from the table to delete."
                );
                return;
            }

            boolean deleteSuccess = adminService.deleteUserById(selectedUser.getId());

            if (deleteSuccess) {
                adminView.addDisplayAlertMessage(
                        "Delete Successful",
                        "User Deleted",
                        "The user was successfully deleted!"
                );
            } else {
                adminView.addDisplayAlertMessage(
                        "Delete Error",
                        "Unable to Delete User",
                        "An error occurred while deleting the user. Please try again."
                );
            }

            List<User> users = adminService.findAll();
            adminView.setListOfUsers(users);
        }

    }

   private class PDFButtonListener implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event) {
            List<Order> orders = orderService.findAllOrders();
            adminService.generateOrderReport(orders);
        }
    }

}
