import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  protected void before() {
    DB.sql2o = new
    Sql2o("jdbc:postgresql://localhost:5432/shoe_stores_test", "postgres", "welcome");
  }

  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteBrandsQuery = "DELETE FROM brands *;";
      String deleteStoresQuery = "DELETE FROM stores *;";
      String deleteBrandsStoresQuery = "DELETE FROM brands_stores *;";
      con.createQuery(deleteBrandsQuery).executeUpdate();
      con.createQuery(deleteStoresQuery).executeUpdate();
      con.createQuery(deleteBrandsStoresQuery).executeUpdate();
    }
  }
}
