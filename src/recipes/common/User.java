package recipes.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
//    Wrong answer in test #5
//    POST /api/register should respond with status code 400, responded: 200...
//    Solution: add regexp
    @Email(regexp = ".+@.+\\..+",message = "Email format is wrong")
    private  String email;
    @NotBlank
    @Size(min=8,message = "password at least contains 8 char")
    private  String password;

}
