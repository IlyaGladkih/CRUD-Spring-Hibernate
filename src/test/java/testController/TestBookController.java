package testController;

import io.restassured.module.mockmvc.response.MockMvcResponse;
import lombok.SneakyThrows;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.ViewResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.test.controllers.BookController;
import ru.test.entity.Book;
import ru.test.service.BookService;
import ru.test.service.PersonService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestBookController {

    @Mock
    private BookService bookService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private BookController bookController;


    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @SneakyThrows
    void handleGetAllBook_validReturnsAllBookView(){
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/all"));
    }

    @Test
    @SneakyThrows
    void handleSearchGet_validReturnSearchView(){
        mockMvc.perform(MockMvcRequestBuilders.get("/books/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/search"));
    }

    @Test
    @SneakyThrows
    void handleSearchPost_validReturnSearchView(){
        mockMvc.perform(MockMvcRequestBuilders.post("/books/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/search"));
    }

    @Test
    @SneakyThrows
    void handleSearchPostWithParamFoundIsNull_validReturnSearchView(){
        mockMvc.perform(MockMvcRequestBuilders.post("/books/search").param("found",""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/search"));
    }

    @Test
    @SneakyThrows
    void handleBookByIdMethod_validReturnBook(){

        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/ids"));
    }

    @Test
    @SneakyThrows
    void handlePushBookMethod_validReturnRedirect(){
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.post("/books/{id}",id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/"+id));
    }

    @Test
    @SneakyThrows
    void handleRealeseBookMethod_ValidResiltRedirect(){
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}/release",id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/"+id));
    }

    @Test
    @SneakyThrows
    void handleAddNewBookFormMethod_ValidResultIsOk(){
        mockMvc.perform(MockMvcRequestBuilders.get("/books/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/new"));
    }

    @Test
    @SneakyThrows
    void handleSaveBookMethod_validResultRedirect(){
        mockMvc.perform(MockMvcRequestBuilders.post("/books/new"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
    }

    @Test
    @SneakyThrows
    void handleEditGetMethod_validResultIsOk(){

        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}/edit", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("book/edit"));
    }

    @Test
    @SneakyThrows
    void handleEditPatchMethod_validResultRedirect(){

        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/books/{id}/edit",id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books/"+id));
    }

    @Test
    @SneakyThrows
    void handleDeleteMethod_validResultRedirect(){

        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}",id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
    }
}
