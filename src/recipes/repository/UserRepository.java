package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.common.User;

public interface UserRepository  extends CrudRepository<User,String> {


}
