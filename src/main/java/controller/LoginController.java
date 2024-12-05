package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.AdminComponentFactory;
import launcher.CustomerComponentFactory;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import model.Role;
import model.User;
import model.validation.Notification;
import service.user.AuthentificationService;
import view.LoginView;
import static database.Constants.Roles.*;

public class LoginController {

    private final LoginView loginView;
    private final AuthentificationService authentificationService;


    public LoginController(LoginView loginView, AuthentificationService authentificationService) {
        this.loginView = loginView;
        this.authentificationService = authentificationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authentificationService.login(username, password);
            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                User loggedInUser = loginNotification.getResult(); // Obține utilizatorul logat
                loginView.setActionTargetText("LogIn Successfull");
                // Verifică rolul utilizatorului
                for (Role r : loginNotification.getResult().getRoles()) {
                    if (r.getRole().equals(ADMINISTRATOR)) {
                        AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loginNotification);
                    } else if (r.getRole().equals(EMPLOYEE)) {
                        EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loggedInUser, loginNotification);
                    } else if (r.getRole().equals(CUSTOMER)) {
                        CustomerComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loggedInUser, loginNotification);
                    }
                }
            }
        }
    }

        private class RegisterButtonListener implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent event) {
                String username = loginView.getUsername();
                String password = loginView.getPassword();

                Notification<Boolean> regiterNotification = authentificationService.register(username, password);

                if (regiterNotification.hasErrors()) {
                    loginView.setActionTargetText(regiterNotification.getFormattedErrors());
                } else {
                    loginView.setActionTargetText("Register successful");
                }
            }
        }
    }

