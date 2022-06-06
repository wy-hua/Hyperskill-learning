package recipes.common;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name="recipes")
public class Recipe {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long recipeId;

    @NotBlank(message = "name/category can't be blank")
    private String name ,category;


    private LocalDateTime date;

    @NotBlank(message = "description can't be blank")
    private String description;

    @NotNull
    @NotEmpty
//    Stage3 #13 failed(supposed to be 400 rather than200
//    ) but until add @NotEmpty
    @ElementCollection
    private List<String> ingredients, directions;

    @JsonIgnore
    private String author;



}

