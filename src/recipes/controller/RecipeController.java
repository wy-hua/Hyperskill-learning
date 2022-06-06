package recipes.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import recipes.common.Recipe;
import recipes.repository.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(path="/api/recipe", produces="application/json")
public class RecipeController {
    private final RecipeRepository repository;

    public RecipeController(@Autowired RecipeRepository repository){
        this.repository=repository;
    }

    @GetMapping("/count")
    public long getCount(){
        return  repository.count();
    }

    @PostMapping("/new")
    public  String CreatRecipe(@Valid @RequestBody Recipe newRecipe, Authentication auth){
        HashMap res=new HashMap<String,Long>();
        newRecipe.setAuthor(auth.getName());
        newRecipe.setDate(LocalDateTime.now());
        Recipe recipe=repository.save(newRecipe);
//       My Version
//        res.put("id",recipe.getRecipeId());
//        return res;
        return "{\"id\": "+ recipe.getRecipeId() +"}";
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> UpdateRecipe(@Valid @RequestBody Recipe newRecipe,@PathVariable long id,Authentication auth){
        Optional<Recipe> target=repository.findById(id);
        if(target.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        == is diffent from String.equals()   the former  compares the addresses of string objects
//        while the latter compares the values
        if(!auth.getName().equals(target.get().getAuthor()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        newRecipe.setDate(LocalDateTime.now());
        newRecipe.setRecipeId(target.get().getRecipeId());
        repository.save(newRecipe);
        return  new ResponseEntity<String>("Updating successfully",HttpStatus.NO_CONTENT);
    }


    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable long id){
        Recipe res=repository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        return  res;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteRecipe(@PathVariable long id,Authentication auth){
        Optional<Recipe> target=repository.findById(id);
        if(target.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//    #18    DELETE /api/recipe/1 should respond with status code 204, responded: 403,
//        solution: add condition:target.get().getAuthor()!=null
//        but why?!!
        if(target.get().getAuthor()!=null&&!auth.getName().equals(target.get().getAuthor()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        repository.deleteById(id);
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam Map<String,String> params){
        if(params.size()!=1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(params.containsKey("category")){
            return repository.findRecipesByCategoryIgnoreCaseOrderByDateDesc(params.get("category"));
        }
        if(params.containsKey("name"))
            return  repository.findRecipesByNameIgnoreCaseContainingOrderByDateDesc(params.get("name"));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


}