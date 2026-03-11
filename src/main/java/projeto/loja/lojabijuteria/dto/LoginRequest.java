package projeto.loja.lojabijuteria.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email deve ser valido")
    private String email;

    @NotBlank(message = "Senha e obrigatorio")
    private String password;

}
