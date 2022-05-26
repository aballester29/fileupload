package com.example.controllers;

import com.example.entities.Persona;
import com.example.services.PaisService;
import com.example.services.PersonaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @Autowired
    private PaisService paisService;

    @GetMapping("/listar")
    public String listar(Model model){
        model.addAttribute("personas", personaService.findAll());
        return "listar";
    }

    @GetMapping("/detalle/{idpersona}")
    public String detalleEmpleado(@PathVariable(name = "idpersona") Long idpersona, Model model){

        //Método que añade los datos al modelo pidiendolos al servicio
        model.addAttribute("persona", personaService.findById(idpersona));

        //Manda a la vista detalle.html los datos
        return "detalle";
    }
    //Método muestra el fomrulario y pasa un empleado vacio para rellenar
    @GetMapping("/alta")
    public String mostrarFormulario(Model model){

        //Pasar un empleado vacio
        model.addAttribute("persona", new Persona());

        //Pasar la lista de departamentos
        model.addAttribute("paises", paisService.findAll());

        return "alta";
    }

    //Método muestra el fomrulario y pasa un empleado vacio para rellenar
    @GetMapping("/alta/{idpersona}")
    public String modificarUsuario(@PathVariable(name = "idpersona") Long idpersona, Model model){

        //Pasar un empleado vacio
        model.addAttribute("persona",  personaService.findById(idpersona));

        //Pasar la lista de departamentos
        model.addAttribute("paises", paisService.findAll());

        return "alta";
    }
}
