package controller;


import model.Odontologo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.OdontologoService;

@Controller
@RequestMapping("/odontologo")
public class OdontologoController {
    private OdontologoService odontologoService;

    public OdontologoController() {
        odontologoService = new OdontologoService();
    }

    @GetMapping
    public String buscarPacientePorCorreo(Model model, @RequestParam("id") Integer id){
        //vamos a pasar la solicitud atraves del http, osea va a ir en la url
        Odontologo odontologo= odontologoService.buscarOdontologo(id);
        model.addAttribute("matricula",odontologo.getNumeroMatricula());
        model.addAttribute("nombre",odontologo.getNombre());
        return "index";
        //return pacienteService.buscarPorCorreo(email);
    }

}
