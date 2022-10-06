package me.aetios.sacola.resource;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.aetios.sacola.model.Item;
import me.aetios.sacola.model.Sacola;
import me.aetios.sacola.resource.dto.ItemDto;
import me.aetios.sacola.service.SacolaService;
import org.springframework.web.bind.annotation.*;

@Api(value="/projeto-aetios/sacolas")
@RestController
@RequestMapping("/projeto-aetios/sacolas") //definir os endpoints
@RequiredArgsConstructor

public class SacolaResource {
    private final SacolaService sacolaService;

    @PostMapping
    public Item incluirItemSacola(@RequestBody ItemDto itemDto){
        return sacolaService.incluirItemSacola(itemDto);
    }

    @GetMapping("/{id}")
    public Sacola verSacola(@PathVariable("id") Long id ){
        return sacolaService.verSacola(id);
    }
    @PatchMapping("/fecharSacola/{sacolaId}") //out PutMapping
    public Sacola fecharSacola(@PathVariable("sacolaId") Long sacolaId, @RequestParam("formaPagamento") int formaPagamento){
        return sacolaService.fecharSacola(sacolaId, formaPagamento);
    }


}
