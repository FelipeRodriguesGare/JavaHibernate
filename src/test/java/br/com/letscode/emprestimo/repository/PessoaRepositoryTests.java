package br.com.letscode.emprestimo.repository;

import br.com.letscode.emprestimo.model.Pessoa;
import br.com.letscode.emprestimo.model.PessoaSalario;
import br.com.letscode.emprestimo.repository.projection.PessoaParcelaProjection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PessoaRepositoryTests {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testFindCpfNotFound() {
        Optional<Pessoa> pessoa = pessoaRepository.findByCpf("123");
        Assertions.assertFalse(pessoa.isPresent());
    }

    @Test
    public void testFindCpf() {
        Optional<Pessoa> pessoa = pessoaRepository.findByCpf("321");
        Assertions.assertTrue(pessoa.isPresent());
    }

    @Test
    public void testSumSalarios() {
        Double salario = pessoaRepository.sumSalarios();
        Assertions.assertEquals(15000.0, salario);
    }

    @Test
    public void testGetMediaSalarios() {
        List<PessoaSalario> pessoaSalarios = pessoaRepository.getMediaSalario();
        Assertions.assertEquals(2, pessoaSalarios.size());
        Assertions.assertEquals("Rua Abcd", pessoaSalarios.get(0).getEndereco());
        Assertions.assertEquals(5000, pessoaSalarios.get(0).getMedia());
    }

    @Test
    public void testFindByEnderecoPageable() {
        Pageable page = PageRequest.of(0,1, Sort.by("nome"));
        List<Pessoa> pessoas = pessoaRepository.findByEndereco("Rua Abcd", page);

        Assertions.assertEquals(1,pessoas.size());
        Assertions.assertEquals("Eduardo", pessoas.get(0).getNome());
    }

    @Test
    public void testListValorParcela() {
        List<PessoaParcelaProjection> parcelas = pessoaRepository.listPessoaValorParcela("Eduardo");

        Assertions.assertEquals(5, parcelas.size());
        Assertions.assertEquals(2200, parcelas.get(0).getValorParcela());
        Assertions.assertEquals("Eduardo", parcelas.get(0).getNomePessoa());
    }

}