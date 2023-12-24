package testController;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.test.controllers.PersonController;
import ru.test.service.PersonService;

@ExtendWith(MockitoExtension.class)
public class TestPersonController {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
            mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    @SneakyThrows
    void handleGetAllPersonsMethod_validResultIsOk(){
        mockMvc.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("person/all"));
    }


    @Test
    @SneakyThrows
    void handlePersonByIdGetMethod_validResultIsOk(){
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("person/ids"));
    }

    @Test
    @SneakyThrows
    void handleAddNewPersonFormMethod_ValidResultIsOk(){
        mockMvc.perform(MockMvcRequestBuilders.get("/persons/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("person/new"));
    }

    @Test
    @SneakyThrows
    void handleSavePersonMethod_ValidResultRedirect(){
        mockMvc.perform(MockMvcRequestBuilders.post("/persons/new"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons"));
    }


    @Test
    @SneakyThrows
    void handleEditGetMethod_ValidResultIsOk(){
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/{id}/edit",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("person/edit"));
    }

    @Test
    @SneakyThrows
    void handleEditPatchMethod_ValidResultIsRedirect(){
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/persons/{id}/edit",id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons/"+id));
    }

    @Test
    @SneakyThrows
    void handleDeleteBookMethod_ValidResultIsRedirect(){
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/persons/{id}",id))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/persons"));
    }

}
