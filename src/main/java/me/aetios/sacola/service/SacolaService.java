package me.aetios.sacola.service;

import me.aetios.sacola.model.Item;
import me.aetios.sacola.model.Sacola;
import me.aetios.sacola.resource.dto.ItemDto;

public interface SacolaService {
    Item incluirItemSacola(ItemDto itemDto);
    Sacola verSacola(Long id);
    Sacola fecharSacola(Long id, int formaPagamento);

}
