package com.macsupport.books.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.macsupport.books.models.Book;
import com.macsupport.books.services.BookService;

@Controller
public class BooksApi {
	private final BookService bookService;
    public BooksApi(BookService bookService){
        this.bookService = bookService;
    }
    @RequestMapping("/books")
    public String index(Model model) {
        List<Book> allBooks = bookService.allBooks();
        model.addAttribute("books", allBooks);
        return "index.jsp";
    }
    
    @RequestMapping(value="/api/books", method=RequestMethod.POST)
    public Book create(@RequestParam(value="title") String title, @RequestParam(value="description") String desc, @RequestParam(value="language") String lang, @RequestParam(value="pages") Integer numOfPages) {
        Book book = new Book(title, desc, lang, numOfPages);
        return bookService.createBook(book);
    }
    
    @RequestMapping(value="/books", method=RequestMethod.POST)
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "new.jsp";
        } else {
            bookService.createBook(book);
            return "redirect:/books";
        }
    }
    
    @RequestMapping("/api/books/{id}")
    public Book show(@PathVariable("id") Long id) {
        Book book = bookService.findBook(id);
        return book;
    }
    
    @RequestMapping("/books/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "new.jsp";
    }
    @RequestMapping(value="/books/{id}", method=RequestMethod.DELETE)
    public String destroy(@PathVariable("id") Long id) {
    	bookService.deleteBook(id);
    	return "redirect:/books";
    }
    @RequestMapping("/books/{id}")
    public String display(@PathVariable("id") Long id, Model model) {
			model.addAttribute("singleBook", bookService.findBook(id));
    		return "show.jsp";
    		
    }
    @PostMapping("/books/{id}/update")
    public String updateBook(@Valid @PathVariable("id") Long id, @ModelAttribute("singleBook") Book singleBook, BindingResult result){
    	if(result.hasErrors()) {
    		return "show.jsp";
    	} else {
    		bookService.update(singleBook, id);
    	return "redirect:/books/" + id;
    	}	
   }
}
