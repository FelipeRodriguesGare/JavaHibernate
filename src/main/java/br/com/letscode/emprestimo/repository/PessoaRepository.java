package br.com.letscode.emprestimo.repository;

import br.com.letscode.emprestimo.model.Pessoa;
import br.com.letscode.emprestimo.model.PessoaSalario;
import br.com.letscode.emprestimo.repository.projection.PessoaEmprestimoProjection;
import br.com.letscode.emprestimo.repository.projection.PessoaParcelaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer>, JpaSpecificationExecutor<Pessoa> {

    Optional<Pessoa> findByCpf(String cpf);

    Page<Pessoa> findByNome(String nome, Pageable pageable);

    List<Pessoa> findByNomeContaining(String nome);

    List<Pessoa> findByNomeStartsWith(String nome);

    List<Pessoa> findByNomeEndsWith(String nome);

    @Query("Select sum(salario) from pessoa")
    public Double sumSalarios();

    @Query("Select sum(salario) from pessoa where nome = :nome")
    public Double sumSalarios(String nome);

    @Query("SELECT avg(salario), endereco from pessoa group by endereco")
    public List<Object[]> getMediaSalarioObject();

    @Query("SELECT new br.com.letscode.emprestimo.model.PessoaSalario(avg(salario), endereco) from pessoa group by endereco")
    public List<PessoaSalario> getMediaSalario();


    // AULA 04 -------------

    List<Pessoa> findByEnderecoOrderByNomeAsc(String endereco);

    List<Pessoa> findByEndereco(String endereco, Sort sort);

    List<Pessoa> findByEndereco(String endereco, Pageable pageable);

    @Query(value = "SELECT p.nome AS nomePessoa, pa.valor AS valorParcela " +
            "FROM pessoa p " +
            "JOIN pessoa_emprestimo pe on p.id = pe.id_pessoa " +
            "JOIN emprestimo e on e.id = pe.id_emprestimo " +
            "JOIN parcela pa on e.id = pa.id_emprestimo "+
            "where nome = :nome", nativeQuery = true
    )
    public List<PessoaParcelaProjection> listPessoaValorParcela(String nome);

    @Query(
            value = "SELECT p.nome AS nomePessoa, p.salario AS salarioPessoa, e.valor AS valorEmprestimo, " +
                    "e.num_parcelas AS numParcelas, e.status AS status " +
                    "FROM pessoa p " +
                    "JOIN pessoa_emprestimo pe on p.id = pe.id_pessoa " +
                    "JOIN emprestimo e on e.id = pe.id_emprestimo ", nativeQuery = true
    )
    public List<PessoaEmprestimoProjection> listPessoaEmprestimo();


}
