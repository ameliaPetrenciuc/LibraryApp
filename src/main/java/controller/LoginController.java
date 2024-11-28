package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactory;
import launcher.EmployeeComponentFactory;
import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import service.user.AuthentificationService;
import view.LoginView;

import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthentificationService authenticationService;



    public LoginController(LoginView loginView, AuthentificationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasError()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successfull");
                EmployeeComponentFactory.getInstance(ComponentFactory.getComponentsForTests(),ComponentFactory.getStage());
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> regiterNotification= authenticationService.register(username,password);

            if (regiterNotification.hasError()) {
                loginView.setActionTargetText(regiterNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("Register successful");
            }
        }
    }
}