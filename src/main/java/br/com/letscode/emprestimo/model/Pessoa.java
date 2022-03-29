package br.com.letscode.emprestimo.model;

import br.com.letscode.emprestimo.dto.PessoaDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "salario")
    private Float salario;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "endereco")
    private String endereco;

    @ManyToMany(mappedBy = "pessoas", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Emprestimo> emprestimos;

    public static Pessoa convert(PessoaDTO pessoaDTO){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDTO.getNome());
        pessoa.setSalario(pessoaDTO.getSalario());
        pessoa.setCpf(pessoaDTO.getCpf());
        pessoa.setEndereco(pessoaDTO.getEndereco());
        return pessoa;
    }
}