package study.together.example.jun.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import study.together.example.jun.model.Book;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class DataTest {

    @Autowired
    private MongoRepository<Book, String> mongoRepository;

    @Test
    public void CRUD_TEST() {
        Book book = new Book(null, "TEST", 500);

        // save
        Book savedBook = mongoRepository.save(book);

        Book book1 = new Book(null, "TEST1", 1000);
        Book book2 = new Book(null, "TEST2", 5000);
        Book book3 = new Book(null, "TEST3", 10000);

        // saveAll
        List<Book> savedBooks = (List<Book>) mongoRepository.saveAll(Stream.of(book1, book2, book3).collect(Collectors.toList()));
        assertSame(3, savedBooks.size());

        // findById
        Book findBook = mongoRepository.findById(savedBook.getId()).orElse(null);
        assertEquals(book, findBook);

        // existsById
        boolean isExist = mongoRepository.existsById(savedBooks.get(0).getId());
        assertTrue(isExist);

        // findAll
        List<Book> allBooks = (List<Book>) mongoRepository.findAll();
        assertSame(4, allBooks.size());

        Iterable<String> bookIds = savedBooks.stream().map(Book::getId).collect(Collectors.toList());
        // findAllById
        List<Book> findBooks = (List<Book>) mongoRepository.findAllById(bookIds);
        assertSame(3, findBooks.size());

        // count
        long allCount = mongoRepository.count();
        assertSame(4L, allCount);

        // deleteById
        mongoRepository.deleteById(findBook.getId());
        assertSame(3L, mongoRepository.count());

        // delete
        mongoRepository.delete(findBooks.get(1));
        assertSame(2L, mongoRepository.count());

        // deleteAll
        mongoRepository.deleteAll(findBooks);
        assertSame(0L, mongoRepository.count());

        mongoRepository.save(book);
        mongoRepository.saveAll(Stream.of(book1, book2, book3).collect(Collectors.toList()));

        assertSame(4L, mongoRepository.count());
        // deleteAll
        mongoRepository.deleteAll();
        assertSame(0L, mongoRepository.count());
    }

    @Test
    public void SortNPage_TEST() {
        Book book = new Book(null, "TEST", 500);

        // save
        Book savedBook = mongoRepository.save(book);

        Book book1 = new Book(null, "TEST1", 1000);
        Book book2 = new Book(null, "TEST2", 5000);
        Book book3 = new Book(null, "TEST3", 10000);

        // saveAll
        List<Book> savedBooks = (List<Book>) mongoRepository.saveAll(Stream.of(book1, book2, book3).collect(Collectors.toList()));

        List<Book> priceOrderBooks = mongoRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        assertEquals(10000, priceOrderBooks.get(0).getPrice());
        assertEquals(500, priceOrderBooks.get(3).getPrice());

        Page<Book> book1Page = mongoRepository.findAll(PageRequest.of(0, 4));
        Page<Book> book2Page = mongoRepository.findAll(PageRequest.of(1, 2));

        assertEquals(4, book1Page.getTotalElements());
        assertEquals(1, book1Page.getTotalPages());
        assertEquals(4, book2Page.getTotalElements());
        assertEquals(2, book2Page.getTotalPages());
        assertEquals(4L, book1Page.stream().count());
        assertEquals(2L, book2Page.stream().count());
    }

}
