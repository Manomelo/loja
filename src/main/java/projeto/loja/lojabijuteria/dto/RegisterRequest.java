package projeto.loja.lojabijuteria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Nome e obrigatório")
    private String name;

    @NotBlank(message =  "Email e obrigatório")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "Senha e obrigatório")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;
}
