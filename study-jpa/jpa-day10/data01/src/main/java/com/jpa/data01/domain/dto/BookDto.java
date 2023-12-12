package com.jpa.data01.domain.dto;

import com.jpa.data01.domain.type.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
public class BookDto {
    private Long id;
    private BookCategory category;
    private String name;
    private Integer price;
    private LocalDate releaseDate;

    public BookDto(Long id, BookCategory category, String name, Integer price, LocalDate releaseDate) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.releaseDate = releaseDate;
    }
}
