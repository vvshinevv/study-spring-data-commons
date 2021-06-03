package study.together.example.hong.repository;

import org.springframework.data.repository.Repository;
import study.together.example.hong.entity.Product;
import study.together.example.hong.entity.Products;

public interface ProductRepository extends Repository<Product, Long> {
    Products findAllByDescriptionContaining(String text);
}
