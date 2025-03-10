package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {

    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache=cache;
    }
    @Override
    public List<Book> findAll() {
        if(cache.hasResult()){
            return cache.load();
        }

        List<Book> books=decoratorBookRepository.findAll();
        cache.save(books);
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if(cache.hasResult()){
            return cache.load().stream()
                    .filter(it->it.getId().equals(id))
                    .findFirst();
        }
        return decoratorBookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratorBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratorBookRepository.delete(book);
    }

    @Override
    public boolean update(Book book, Long stock) {
        cache.invalidateCache();
        return decoratorBookRepository.update(book,stock);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratorBookRepository.removeAll();
    }
}
