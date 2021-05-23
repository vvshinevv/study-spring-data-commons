package study.together.example.jun.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import study.together.example.jun.model.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByName(String name);

    List<Book> findByNameAndPrice(String name, int price);

    List<Book> findDistinctBooksByNameAndPrice(String name, int price);

    List<Book> findByNameIgnoreCase(String name);

    List<Book> findByNameAndDesc(String name, String desc);

    List<Book> findByNameAndDescAllIgnoreCase(String name, String desc);

    List<Book> findAllByOrderByPriceAsc();

    List<Book> findAllByOrderByPriceDesc();
}
