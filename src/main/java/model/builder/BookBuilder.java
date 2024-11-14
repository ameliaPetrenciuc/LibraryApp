package model.builder;

import model.Book;

import java.time.LocalDate;
//Design Pattern Creational

public class BookBuilder {
    private Book book;

    public BookBuilder(){
        book=new Book();
    }

    public BookBuilder setId(Long id){
        book.setId(id);
        return this;
    }

    public BookBuilder setTitle(String title){
        book.setTitle(title);
        return this;
    }

    public BookBuilder setAuthor(String author){
        book.setAuthor(author);
        return this;
    }

    public BookBuilder setPublishedDate(LocalDate publishedDated){
        book.setPublishedDate(publishedDated);
        return this;
    }

    public BookBuilder setStock(Long stock){
        book.setStock(stock);
        return this;
    }

    public BookBuilder setPrice(Float price){
        book.setPrice(price);
        return this;
    }

    public Book build(){
        return book;
    }

}
