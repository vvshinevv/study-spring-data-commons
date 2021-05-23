package study.together.example.jun.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import study.together.example.jun.model.Book;

interface BookRepository extends MongoRepository<Book, String> {

}
