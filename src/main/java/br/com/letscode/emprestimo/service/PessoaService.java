package br.com.letscode.emprestimo.service;


import br.com.letscode.emprestimo.dto.PessoaBuscaDTO;
import br.com.letscode.emprestimo.dto.PessoaDTO;
import br.com.letscode.emprestimo.model.Pessoa;
import br.com.letscode.emprestimo.repository.PessoaRepository;
import br.com.letscode.emprestimo.repository.projection.PessoaEmprestimoProjection;
import br.com.letscode.emprestimo.repository.projection.PessoaParcelaProjection;
import br.com.letscode.emprestimo.repository.specification.PessoaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public Page<PessoaDTO> listAll(List<String> nome, Pageable pageable) {
        return pessoaRepository
                .findByNome(nome.get(0), pageable)
                .map(PessoaDTO::convert);
    }

    public Page<PessoaDTO> listAll(Pageable pageable) {
        return pessoaRepository
                .findAll(pageable)
                .map(PessoaDTO::convert);
    }

    public PessoaDTO criarPessoa(PessoaDTO pessoa) {
        Pessoa pessoaBD = pessoaRepository.save(Pessoa.convert(pessoa));
        return PessoaDTO.convert(pessoaBD);
    }

    public void deletePessoa(String cpf) {
        Pessoa pessoa = pessoaRepository.findByCpf(cpf)
                .orElseThrow(RuntimeException::new);
        pessoaRepository.delete(pessoa);
    }

    public void ordenacao() {
        List<Pessoa> pessoas = pessoaRepository.findByEnderecoOrderByNomeAsc("sp");
        pessoas.forEach(p -> System.out.println(p.getNome()));

        System.out.println("----------");
        pessoas = pessoaRepository.findByEndereco(
                "sp",
                Sort.by(Sort.Direction.ASC, "nome")
        );
        pessoas.forEach(p -> System.out.println(p.getNome()));

    }

    public void paginacao() {
        List<Pessoa> pessoas = pessoaRepository.findByEndereco(
                "sp",
                PageRequest.of(2, 1, Sort.by(Sort.Direction.ASC, "endereco"))
        );
        pessoas.forEach(p -> System.out.println(p.getNome()));

        Page<Pessoa> pessoasPage = pessoaRepository.findAll(
                PageRequest.of(2, 1, Sort.by(Sort.Direction.ASC, "endereco"))
        );
        pessoas.forEach(p -> System.out.println(p.getNome()));
        System.out.println(pessoasPage.getTotalElements() + " " + pessoasPage.getTotalPages());

    }

    public void projection() {
        List<PessoaParcelaProjection> pessoas = pessoaRepository.listPessoaValorParcela("Joao");
        pessoas.forEach(p -> System.out.println(p.getNomePessoa() + " " + p.getValorParcela()));

        System.out.println("----------");
        List<PessoaEmprestimoProjection> pessoasEmprestimo = pessoaRepository.listPessoaEmprestimo();
        pessoasEmprestimo.forEach(x ->
                System.out.println(x.getNomePessoa()
                + " " + x.getSalarioPessoa()
                + " " + x.getValorEmprestimo()
                + " " + x.getNumParcelas()
                + " " + x.getStatus())
        );
    }

    public void especification() {
        List<Pessoa> pessoas = pessoaRepository.findAll(
                PessoaSpecification.filterByName("Joao")
                .and(PessoaSpecification.filterBySalarioGreaterThan(500.0))
        );
        pessoas.forEach(p -> System.out.println(p.getNome() + " " + p.getEndereco()));
    }


    public List<PessoaDTO> queryPessoaSpecification(PessoaBuscaDTO pessoaBuscaDTO) {

        Specification<Pessoa> specification = Specification.where(null);

        if (pessoaBuscaDTO.getNome() != null) {
            specification.and(PessoaSpecification.filterByName(pessoaBuscaDTO.getNome()));
        }

        if(pessoaBuscaDTO.getEndereco() != null) {
                specification.and(PessoaSpecification.filterByEndereco(pessoaBuscaDTO.getEndereco()));
        }

        if (pessoaBuscaDTO.getSalario() != null) {
            specification.and(PessoaSpecification.filterBySalarioGreaterThan(pessoaBuscaDTO.getSalario()));
        }

        return pessoaRepository
                .findAll(specification)
                .stream()
                .map(PessoaDTO::convert)
                .collect(Collectors.toList());
    }
}