package com.monitoratec.victor.services;

import com.monitoratec.victor.models.Book;
import com.monitoratec.victor.models.dto.BookDTO;
import com.monitoratec.victor.services.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService extends AService<Book, BookRepository> {
    @Autowired
    private BookRepository repository;

    @Override
    protected BookRepository repository() {
        return repository;
    }

    public Book save(BookDTO bookDto, int id) {
        Book book = BookDTO.toBook(bookDto, true);
        // ensure the id is correct to avoid creation on update and vice-versa
        book.setId(id);

        return this.repository().save(book);
    }
}
