package br.com.api.viacepapi.controller;

import br.com.api.viacepapi.dto.ResponseViaCepDTO;
import br.com.api.viacepapi.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class IndexController {

    @Autowired
    private IndexService service;

    @PostMapping("v1/consulta-endereco")
    public ResponseViaCepDTO buscarFrete(@RequestParam String cep) throws IOException {
        return this.service.buscarFrete(cep);
    }
}
