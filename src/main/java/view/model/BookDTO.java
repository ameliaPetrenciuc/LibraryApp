package view.model;
import javafx.beans.property.*;

public class BookDTO {
    private StringProperty author;
    private StringProperty title;
    private LongProperty stock;
    private FloatProperty price;


    public void setAuthor(String author){
        authorProperty().set(author);
    }

    public String getAuthor(){
        return authorProperty().get();
    }

    public StringProperty authorProperty(){
        if(author==null){
            author=new SimpleStringProperty(this,"author");
        }
        return author;
    }

    public void setTitle(String title){
        titleProperty().set(title);
    }

    public String getTitle(){
        return titleProperty().get();
    }

    public StringProperty titleProperty(){
        if (title==null){
            title=new SimpleStringProperty(this, "title");
        }
        return title;
    }

    public void setStock(Long stock){
        stockProperty().set(stock);
    }

    public Long getStock(){
        return stockProperty().get();
    }

    public LongProperty stockProperty(){
        if (stock==null){
            stock = new SimpleLongProperty(this, "stock", 0L);
        }
        return stock;
    }

    public void setPrice(Float price){
        priceProperty().set(price);
    }

    public Float getPrice(){
        return priceProperty().get();
    }

    public FloatProperty priceProperty(){
        if (price==null){
            price = new SimpleFloatProperty(this, "price", 0.0f);
        }
        return price;
    }
}
