package service.user.admin;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import model.Order;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.user.AuthentificationService;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static database.Constants.Roles.EMPLOYEE;

public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminServiceImpl(UserRepository userRepository,RightsRolesRepository rightsRolesRepository ){
        this.userRepository=userRepository;
        this.rightsRolesRepository=rightsRolesRepository;
    }

    public Notification<Boolean> register(String username, String password) {

        Role customerRole=rightsRolesRepository.findRoleByTitle(EMPLOYEE);

        User user=new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator=new UserValidator(user);
        boolean userValid=userValidator.validate();
        Notification<Boolean> userRegisterNotification=new Notification<>();

        if(!userValid || userRepository.existsByUsername(username)){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.addError("Username already exists!");
            userRegisterNotification.setResult(Boolean.FALSE);

        }else{
            user.setPassword(hashPassword(password));
            userRegisterNotification=userRepository.save(user);
        }
        return userRegisterNotification;
    }

    public Notification<Boolean> register(String username, String password , String role) {

        Role customerRole = rightsRolesRepository.findRoleByTitle(role);
        if (customerRole == null) {
            throw new IllegalArgumentException("Role not found: " + role);
        }
        User user=new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator=new UserValidator(user);
        boolean userValid=userValidator.validate();
        Notification<Boolean> userRegisterNotification=new Notification<>();

        if(!userValid || userRepository.existsByUsername(username)){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.addError("Username already exists!");
            userRegisterNotification.setResult(Boolean.FALSE);

        }else{
            user.setPassword(hashPassword(password));
            userRegisterNotification=userRepository.save(user);
        }
        return userRegisterNotification;
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean deleteUserById(Long id) {
        return userRepository.deleteUserById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Notification<User> findUser(Long id) {
        return null;
    }

    @Override
    public void removeAll() {
        userRepository.removeAll();

    }

    public boolean generateOrderReport(List<Order> orders){
        try{

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filePath = "D:\\Petrenciuc Amelia\\Documents\\IS\\Library\\report" + timestamp + ".pdf";
            PdfWriter writer=new PdfWriter(filePath);
            PdfDocument pdf=new PdfDocument(writer);
            Document document= new Document(pdf);

            document.add(new Paragraph("Order Report").setBold().setFontSize(20).setMarginBottom(20));

            Map<Long, String> employeeIdToUsername = getEmployeeIdToUsernameMap();

            Map<Long, List<Order>> ordersByEmployee = orders.stream()
                    .filter(order -> order.getEmployeeId() > 0)
                    .collect(Collectors.groupingBy(Order::getEmployeeId));

            for(Map.Entry<Long, List<Order>> entry : ordersByEmployee.entrySet()){
                Long employeeId= entry.getKey();
                List<Order> employeeOrders=entry.getValue();

                String username = employeeIdToUsername.getOrDefault(employeeId, "Unknown Employee");
                document.add(new Paragraph("Employee: " + username + " (ID: " + employeeId + ")")
                        .setBold()
                        .setFontSize(14)
                        .setMarginBottom(10));

                // Create a table for the employee's orders
                float[] columnWidths = {100, 200, 100, 100};
                Table table = new Table(columnWidths);

                table.addHeaderCell(new Cell().add(new Paragraph("Title")));
                table.addHeaderCell(new Cell().add(new Paragraph("Author")));
                table.addHeaderCell(new Cell().add(new Paragraph("Quantity")));
                table.addHeaderCell(new Cell().add(new Paragraph("Date")));

                // Populate table with order data
                for (Order order : employeeOrders) {
                    table.addCell(order.getTitle());
                    table.addCell(order.getAuthor());
                    table.addCell(String.valueOf(order.getQuantity()));
                    table.addCell(order.getSaleDateTime().toString());
                }

                // Add the table to the document
                document.add(table.setMarginBottom(20));
            }
            document.close();
            System.out.println("PDF report generated successfully: " +filePath);
            return true;
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public Map<Long, String> getEmployeeIdToUsernameMap() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));
    }
}
