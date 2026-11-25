package br.com.baozi.store.controller;
import br.com.baozi.store.model.*;
import br.com.baozi.store.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository repository;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    public record PedidoInput(@NotNull Long clienteId, @NotNull Long produtoId, @NotNull @Positive Integer quantidade) {}
    public PedidoController(PedidoRepository repository, ClienteRepository clientes, ProdutoRepository produtos) { this.repository = repository; this.clientes = clientes; this.produtos = produtos; }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido criar(@Valid @RequestBody PedidoInput input) {
        Pedido entity = new Pedido();
        entity.setCliente(clientes.findById(input.clienteId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")));
        entity.setProduto(produtos.findById(input.produtoId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado")));
        if (!Boolean.TRUE.equals(entity.getProduto().getEstoque())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponível");
        entity.setQuantidade(input.quantidade());
        return repository.save(entity);
    }
    @GetMapping
    public List<Pedido> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Pedido consultar(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado")); }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        consultar(id);
        try { repository.deleteById(id); } catch (DataIntegrityViolationException ex) { throw new ResponseStatusException(HttpStatus.CONFLICT, "Registro vinculado a um pedido"); }
    }
}
