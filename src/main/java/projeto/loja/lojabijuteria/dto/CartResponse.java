package projeto.loja.lojabijuteria.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartResponse {

    private Long id;
    private List<CartItemResponse> items;
    private BigDecimal total;
    private Integer itemCount;
}
