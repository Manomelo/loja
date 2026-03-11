package projeto.loja.lojabijuteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {

    private long id;

    @NotBlank(message = "Nome e obrigatorio")
    private String name;

    private String description;

    @NotNull(message = "Preco e obrigatorio")
    @Positive(message = "Preco deve ser maior que zero")
    private BigDecimal price;

    @NotNull(message = "Estoque e obrigatorio")
    @PositiveOrZero(message = "Estoque nao pode ser negativo")
    private Integer stock;

    private String imageUrl;

    private Boolean active;

    @NotNull(message = "Categoria e obrigatoria")
    private Long categoryId;

    private String categoryName;
}
