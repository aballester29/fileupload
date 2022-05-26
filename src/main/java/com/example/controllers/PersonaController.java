package com.example.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

import com.example.entities.Persona;
import com.example.services.PaisService;
import com.example.services.PersonaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @Autowired
    private PaisService paisService;

    @GetMapping("/listar")
    public String listar(Model model) {
        List<Persona> personas = personaService.findAll();
        List<Persona> personasDetalles = new ArrayList<>();
        for (Persona p : personas) {
            personasDetalles.add(personaService.findById(p.getId()));
        }
        model.addAttribute("personas", personasDetalles);
        return "listar";
    }

    // Método muestra el fomrulario y pasa un empleado vacio para rellenar
    @GetMapping("/alta/{idpersona}")
    public String altaModificar(@PathVariable(name = "idpersona") Long idpersona, Model model) {

        // Pasar un empleado vacio
        model.addAttribute("persona", idpersona != 0 ? personaService.findById(idpersona) : new Persona());

        // Pasar la lista de departamentos
        model.addAttribute("paises", paisService.findAll());

        return "alta";
    }

    @GetMapping("/borrar/{idpersona}")
    @Transactional
    public String borrarPersona(@PathVariable(name = "idpersona") Long idpersona) {
        Persona persona = personaService.findById(idpersona);

        Path imageFolder = Paths.get("src/main/resources/static/images");

        String rutaAbsolutaInterna = imageFolder.toFile().getAbsolutePath();

        String rutaAbsolutaExterna = "/home/azahara/pruebas/recursos/";

        try {
            String nombre = persona.getFoto();

            Path rutacompletaInterna = Paths.get(rutaAbsolutaInterna + "//" + nombre);
            Path rutacompletaExterna = Paths.get(rutaAbsolutaExterna + "//" + nombre);

            Files.deleteIfExists(rutacompletaInterna);
            Files.deleteIfExists(rutacompletaExterna);

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        personaService.delete(persona);
        return "redirect:/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute(name = "persona") Persona persona,
            @RequestParam(name = "file") MultipartFile file) {
        if (!file.isEmpty()) {
            // Ruta a la carpeta static/images donde almacenamos los archivos

            Path imageFolder = Paths.get("src/main/resources/static/images");

            String rutaAbsolutaInterna = imageFolder.toFile().getAbsolutePath();

            String rutaAbsolutaExterna = "/home/azahara/pruebas/recursos/";

            try {
                byte[] imageBytes = file.getBytes();
                String nombre = file.getOriginalFilename();

                Path rutacompletaInterna = Paths.get(rutaAbsolutaInterna + "//" + nombre);
                Path rutacompletaExterna = Paths.get(rutaAbsolutaExterna + "//" + nombre);

                Files.write(rutacompletaInterna, imageBytes);
                Files.write(rutacompletaExterna, imageBytes);

                persona.setFoto(nombre);

                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        personaService.save(persona);

        return "redirect:/listar";
    }

    // @GetMapping("/detalle/{idpersona}")
    // public String detalleEmpleado(@PathVariable(name = "idpersona") Long
    // idpersona, Model model){

    // //Método que añade los datos al modelo pidiendolos al servicio
    // model.addAttribute("persona", personaService.findById(idpersona));

    // //Manda a la vista detalle.html los datos
    // return "detalle";
    // }
    // Método muestra el fomrulario y pasa un empleado vacio para rellenar
    // @GetMapping("/alta")
    // public String mostrarFormulario(Model model){

    // //Pasar un empleado vacio
    // model.addAttribute("persona", new Persona());

    // //Pasar la lista de departamentos
    // model.addAttribute("paises", paisService.findAll());

    // return "alta";
    // }
}
