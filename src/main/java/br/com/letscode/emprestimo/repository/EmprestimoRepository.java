package br.com.letscode.emprestimo.repository;

import br.com.letscode.emprestimo.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {

    List<Emprestimo> findByValorGreaterThan(Float valor);

    List<Emprestimo> findByNumParcelasLessThanEqual(Integer num);

    List<Emprestimo> findByDataEmprestimoGreaterThan(LocalDateTime data);

    List<Emprestimo> findByDataEmprestimoBetween(LocalDateTime inicio, LocalDateTime fim);
}