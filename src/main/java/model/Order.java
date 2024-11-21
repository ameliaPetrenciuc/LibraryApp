package model;
//pt a crea un nou tabel
public class Order {
    private Long id;
//    private Long bookId;
    private Long stock;
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(Long bookId) {
//        this.bookId = bookId;
//    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long quantity) {
        this.stock = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String toString(){
        return "Order: Id: "+ id +
                "Quantity: "+ stock +" Price: "+ price;
    }
}
