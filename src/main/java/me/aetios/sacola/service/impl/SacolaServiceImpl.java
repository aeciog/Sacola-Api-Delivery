package me.aetios.sacola.service.impl;
// classe que implmeneta todos os metodos no Sacola Service


import lombok.RequiredArgsConstructor;
import me.aetios.sacola.enumaration.FormaPagamento;
import me.aetios.sacola.model.Item;
import me.aetios.sacola.model.Restaurante;
import me.aetios.sacola.model.Sacola;
import me.aetios.sacola.repository.ItemRepository;
import me.aetios.sacola.repository.ProdutoRepository;
import me.aetios.sacola.repository.SacolaRepository;
import me.aetios.sacola.resource.dto.ItemDto;
import me.aetios.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;
    @Override
    public Item incluirItemSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getIdSacola());

        if (sacola.isFechada()){
            throw  new RuntimeException("Esta sacola está fechada.");
        }
        Item itemInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                ))
                .build();
        List<Item> itensDaSacola = sacola.getItens();
        if(itensDaSacola.isEmpty()){
            itensDaSacola.add(itemInserido);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteItemAdd = itemInserido.getProduto().getRestaurante();
            if(restauranteAtual.equals(restauranteItemAdd)) {
                itensDaSacola.add(itemInserido);
            } else {
                throw new RuntimeException("Não é possível adicionar produtos de outro restaurante. Esvazie seu carrinho ou feche. ");
            }
        }
        List<Double> valorDosItens = new ArrayList<>();
        for(Item itemDaSacola: itensDaSacola){
            double valorTotalItem =
                    itemDaSacola.getProduto().getValorUnitario() * itemDaSacola.getQuantidade();
            valorDosItens.add(valorTotalItem);
        }

        double valorTotalSacola = valorDosItens.stream()
                        .mapToDouble(valorTotalDeCadItem -> valorTotalDeCadItem)
                                .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        //return itemRepository.save(itemInserido);
        return itemInserido;
    }
    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numFormaPagamento) {
        Sacola sacola = verSacola(id);

        if (sacola.getItens().isEmpty()){
            throw new RuntimeException("Inclua itens na sacola!");
        }
        FormaPagamento formaPagamento =
            numFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINA;

        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);

    }
}
