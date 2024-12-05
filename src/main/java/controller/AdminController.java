package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.AdminComponentFactory;
import launcher.LoginComponentFactory;
import model.Order;
import model.User;
import model.validation.Notification;
import service.order.OrderService;
import service.user.admin.AdminService;
import view.AdminView;
import java.util.List;

public class AdminController {
    private final AdminView adminView;
    private final AdminService adminService;
    private final OrderService orderService;
    private final Stage primaryStage;
    private Notification<User> user;

public AdminController(AdminView adminView, AdminService adminService, Notification<User> user, OrderService orderService,Stage primaryStage) {
        this.adminView = adminView;
        this.adminService=adminService;
        this.user = user;
        this.orderService=orderService;
        this.primaryStage = primaryStage;

        List<User> users = adminService.findAll();
        adminView.setListOfUsers(users);

        this.adminView.addCreateUserButtonListener(new AdminController.CreateUserButtonListener());
        this.adminView.addDeleteUserButtonListener(new AdminController.DeleteUserButtonListener());
        this.adminView.addPdfButtonListener(new AdminController.PDFButtonListener());
        this.adminView.addBackButtonListener(new AdminController.BackButtonListener());
}


    private class CreateUserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            User newEmployee = adminView.getNewEmployee();

            if (newEmployee.getRoles().isEmpty()) {
                adminView.addDisplayAlertMessage(
                        "Create Error",
                        "Role Not Selected",
                        "Please select a role for the new user."
                );
                return;
            }
            String selectedRole = newEmployee.getRoles().get(0).getRole();
            Notification<Boolean> registerEmployeeNotification=adminService.register(newEmployee.getUsername(), newEmployee.getPassword(),selectedRole);
            if ((registerEmployeeNotification.hasErrors())){
                adminView.addDisplayAlertMessage("Create Error", "Problem of Username or Password field", "Try again!");
            }else{
                adminView.addDisplayAlertMessage("Create Successful", "User Added", "The user was successfully added!");
            }

            List<User> users = adminService.findAll();
            adminView.setListOfUsers(users);
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

   private class PDFButtonListener implements EventHandler<ActionEvent> {
       public void handle(ActionEvent event) {
           List<Order> orders = orderService.findAllOrders();

           boolean reportGenerated = adminService.generateOrderReport(orders);

           if (reportGenerated) {
               adminView.addDisplayAlertMessage(
                       "Report Generated",
                       "PDF Report Successful",
                       "The PDF report was successfully generated!"
               );
           } else {
               adminView.addDisplayAlertMessage(
                       "Report Generation Failed",
                       "PDF Report Error",
                       "There was an error generating the PDF report. Please try again."
               );
               adminService.generateOrderReport(orders);
           }
       }
   }

    private class BackButtonListener implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event) {
            LoginComponentFactory.getInstance(AdminComponentFactory.getComponentsForTest(),AdminComponentFactory.getStage());
        }
    }

}
