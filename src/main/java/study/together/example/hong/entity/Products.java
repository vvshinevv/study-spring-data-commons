package study.together.example.hong.entity;

import org.springframework.data.util.Streamable;

import java.util.Iterator;

public class Products implements Streamable<Product> {

    private Streamable<Product> streamable;

    public int getTotal() {
        return streamable.stream()
                .map(Product::getPrice)
                .reduce(0, Integer::sum);
    }

    @Override
    public Iterator<Product> iterator() {
        return streamable.iterator();
    }
}
