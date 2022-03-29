package br.com.letscode.emprestimo.controller;

import br.com.letscode.emprestimo.dto.PessoaBuscaDTO;
import br.com.letscode.emprestimo.dto.PessoaDTO;
import br.com.letscode.emprestimo.model.Pessoa;
import br.com.letscode.emprestimo.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    PessoaService pessoaService;

    @GetMapping()
    @ResponseBody
    public Page<PessoaDTO> listPessoas(
            @RequestParam(name = "nome") List<String> nome,
            Pageable pageable
    ){
        return pessoaService.listAll(nome, pageable);
    }

    @GetMapping("/page")
    @ResponseBody
    public Page<PessoaDTO> listPessoas(
            @PageableDefault(value = 5, page = 0) Pageable pageable
    ) {
        return pessoaService.listAll(pageable);
    }

    @PostMapping()
    @ResponseBody
    public PessoaDTO criaPessoa(@RequestBody PessoaDTO pessoa) {
        return pessoaService.criarPessoa(pessoa);
    }

    @DeleteMapping("/{cpf}")
    public void deletePessoa(@PathVariable String cpf) {
        pessoaService.deletePessoa(cpf);
    }

    @GetMapping("/busca")
    @ResponseBody
    public List<PessoaDTO> buscaPessoa(PessoaBuscaDTO pessoaBuscaDTO) {
        return pessoaService.queryPessoaSpecification(pessoaBuscaDTO);
    }

}

