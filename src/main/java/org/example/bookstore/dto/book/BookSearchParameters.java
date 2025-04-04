package org.example.bookstore.dto.book;

public record BookSearchParameters(String[] authors, String[] titles, String[] isbns) {
}
