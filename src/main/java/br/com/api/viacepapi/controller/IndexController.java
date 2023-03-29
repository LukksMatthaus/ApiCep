package br.com.api.viacepapi.controller;

import br.com.api.viacepapi.dto.ResponseViaCepDTO;
import br.com.api.viacepapi.exception.CepNaoEncontradoException;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class IndexController {

    @PostMapping("v1/consulta-endereco")
    public ResponseViaCepDTO buscarFrete(@RequestParam String cep) throws IOException, CepNaoEncontradoException {
        String cepFormatado = cep.replaceAll("\\W", "");
        URL urlViaCep =  new URL("https://viacep.com.br/ws/"+cepFormatado+"/json/");
        HttpURLConnection connection = (HttpURLConnection) urlViaCep.openConnection();
        connection.setRequestMethod("GET");
        Integer responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String cepAux = "";
            StringBuilder jsonCep = new StringBuilder();
            while((cepAux = br.readLine()) != null){
                jsonCep.append(cepAux);
            }

            ResponseViaCepDTO response = new Gson().fromJson(jsonCep.toString(), ResponseViaCepDTO.class);
            if(response.getLocalidade() == null || response.getUf() == null){
                throw new CepNaoEncontradoException("CEP não encontrado.");
            } else{
                response.setFrete(calculcarFrete(response.getUf()));
                return response;
            }
        } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
            throw new CepNaoEncontradoException("CEP Inválido.");
        }

        return null;

    }

    private String calculcarFrete(String uf){
        String[] regiaoNorte =  new String[]{"AM", "RR", "AP", "PA", "TO", "RO", "AC"};
        String[] regiaoNordeste = new String[]{"MA", "PI", "CE", "RN", "PE", "PB", "SE", "AL", "BA"};
        String[] regiaoCentroOeste = new String[]{"MT", "MS", "GO"};
        String[] regiaoSudeste = new String[]{"SP", "RJ", "ES", "MG"};
        String[] regiaoSul = new String[]{"PR", "RS", "SC"};

        if(Arrays.asList(regiaoNorte).contains(uf)){
            return "20,83";
        } else if(Arrays.asList(regiaoNordeste).contains(uf)){
            return "15,98";
        } else if(Arrays.asList(regiaoCentroOeste).contains(uf)){
            return "12,50";
        } else if(Arrays.asList(regiaoSudeste).contains(uf)){
            return "7,85";
        }else if(Arrays.asList(regiaoSul).contains(uf)){
            return "17,30";
        } else{
            return "Região não encontrada para cálculo do frete.";
        }
    }
}
