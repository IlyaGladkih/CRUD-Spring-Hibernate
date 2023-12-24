package ru.test.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.test.entity.Book;
import ru.test.entity.Person;
import ru.test.exceptions.NoSuchBookException;
import ru.test.service.BookService;
import ru.test.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    private PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String getAllBook(Model model, HttpServletRequest request){
        Optional<String> page = Optional.ofNullable(request.getParameter("page"));
        Optional<String> booksPerPage = Optional.ofNullable(request.getParameter("books per page"));
        Boolean val = Boolean.valueOf(Optional.ofNullable(request.getParameter("sort_by_year")).orElse("false"));
        if(page.isPresent()&&booksPerPage.isPresent())
            model.addAttribute("books", bookService.getAllBook(page,booksPerPage,val));
        else
            model.addAttribute("books", bookService.getAllBook(val));
        return "book/all";
    }

    @GetMapping("/search")
    public String searchGet(){
        return "book/search";
    }

    @PostMapping("/search")
    public String searchPost(@ModelAttribute("book") Book book, Model model,HttpServletRequest request){
        Optional<String> found = Optional.ofNullable(request.getParameter("found"));
        model.addAttribute("findingBooks",bookService.getBooksLike(found.orElse(null)));
        return "book/search";
    }

    @GetMapping("/{id}")
    public String bookById(@PathVariable("id") int id, Model model){
        Book book = bookService.getById(id);
        model.addAttribute("book",book);
        model.addAttribute("persons",personService.getAllPersons());
        model.addAttribute("person",new Person());
        model.addAttribute("fresh",bookService.chekDate(book));
        return "book/ids";
    }

    @PostMapping("/{id}")
    public String bookPush(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        bookService.pushBook(person,id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/{id}/release")
    public String releasePush(@PathVariable("id") int id){
        bookService.release(id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/new")
    public String addNewBookForm(Model model){
        model.addAttribute("book", new Book());
        return "book/new";
    }

    @PostMapping("/new")
    public String saveBook(@ModelAttribute("book") Book book){
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editGet(@PathVariable("id")int id, Model model){
        model.addAttribute("book", bookService.getById(id));
        return "book/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editPatch(@PathVariable("id") int id, @ModelAttribute("book") Book book){
        bookService.update(book,id);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/books";
    }


    @ExceptionHandler(NoSuchBookException.class)
    public String noBookHandler(){
        return "book/nobook";
    }
}
