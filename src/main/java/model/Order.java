package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

//pt a crea un nou tabel
public class Order {
    private Long id;
    private String author;
    private String title;
    private LocalDateTime saleDateTime;
    private Long customerId;
    private Long employeeId;
    private int quantity;
//    private Long stock;
//    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public void setSaleDateTime(LocalDateTime saleDateTime) {
        this.saleDateTime = saleDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public Long getEmployeeId(){
        return this.employeeId;
    }
    public void setEmployeeId(Long employeeId){
        this.employeeId = employeeId;
    }
}

//    public String toString(){
//        return "Order: Id: "+ id +
//                "Quantity: "+ stock +" Price: "+ price;
//    }
//}
