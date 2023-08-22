package ru.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.test.entity.Person;
import ru.test.service.PersonService;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String getAllPersons(Model model){
        model.addAttribute("persons", personService.getAllPersons());
        return "person/all";
    }

    @GetMapping("/{id}")
    public String personById(@PathVariable("id") int id, Model model){
        model.addAttribute("person",personService.getById(id));
        return "person/ids";
    }

    @GetMapping("/new")
    public String addNewPersonForm(Model model){
        model.addAttribute("person", new Person());
        return "person/new";
    }

    @PostMapping("/new")
    public String savePerson(@ModelAttribute("person") Person person){
        personService.save(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}/edit")
    public String editGet(@PathVariable("id")int id, Model model){
        model.addAttribute("person", personService.getById(id));
        return "person/edit";
    }

    @PatchMapping("/{id}/edit")
    public String editPatch(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        personService.update(person, id);
        return "redirect:/persons/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/persons";
    }


}
