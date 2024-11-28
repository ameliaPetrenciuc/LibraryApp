package model;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishedData;
    private Long stock;
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishedDate() {
        return publishedData;
    }

    public void setPublishedDate(LocalDate publishedData) {
        this.publishedData = publishedData;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return "Book: Id: "+ id +" Title: "+ title+
                "Author: "+ author +" Published Date: "+ publishedData+
                " Stock: "+ stock+ " Price: "+ price;

    }
}
