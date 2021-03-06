import java.util.List;
import org.sql2o.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Store {
  private int id;
  private String name;
  private String city;
  private String state;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public Store(String name, String city, String state) {
    this.name = name;
    this.city = city;
    this.state = state;
  }

  @Override
  public boolean equals(Object otherStore) {
    if(!(otherStore instanceof Store)) {
      return false;
    } else {
      Store newStore = (Store) otherStore;
      return this.getName().equals(newStore.getName()) &&
             this.getId() == newStore.getId();
    }
  }

  public static List<Store> all() {
    String sql = "SELECT * FROM stores ORDER BY name;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

   public static List<Store> allByCity() {
    String sql = "SELECT * FROM stores ORDER BY city;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

   public static List<Store> allByState() {
    String sql = "SELECT * FROM stores ORDER BY state;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

   public static List<Store> allById() {
    String sql = "SELECT * FROM stores ORDER BY id;";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores (name, city, state) VALUES (:name, :city, :state);";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("city", this.city)
      .addParameter("state", state)
      .executeUpdate()
      .getKey();
    }
  }

   public static Store find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stores WHERE id=:id";
      Store store = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Store.class);
      return store;
    }
  }

  public void update(String name, String city, String state) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stores SET name = :name, city = :city, state = :state WHERE id = :id";
      con.createQuery(sql)
      .addParameter("name", name)
      .addParameter("city", city)
      .addParameter("state", state)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void delete() {
  try(Connection con = DB.sql2o.open()) {
    String deleteQuery = "DELETE FROM stores WHERE id=:id";
    con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();
    String joinDeleteQuery = "DELETE FROM brands_stores WHERE store_id=:storeId";
    con.createQuery(joinDeleteQuery)
      .addParameter("storeId", this.getId())
      .executeUpdate();
  }
}

  public void addBrand(Brand brand) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands_stores (brand_id, store_id) VALUES (:brand_id, :store_id)";
      con.createQuery(sql)
        .addParameter("brand_id", brand.getId())
        .addParameter("store_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Brand> getBrands() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT brand_id FROM brands_stores WHERE store_id = :store_id";
      List<Integer> brandIds = con.createQuery(sql)
        .addParameter("store_id", this.getId())
        .executeAndFetch(Integer.class);
      ArrayList<Brand> brands = new ArrayList<Brand>();
      for (Integer brandId : brandIds) {
        String brandQuery = "SELECT * FROM brands WHERE id = :brandId";
        Brand brand = con.createQuery(brandQuery)
          .addParameter("brandId", brandId)
          .executeAndFetchFirst(Brand.class);
        brands.add(brand);
      }
      return brands;
    }
  }

  public static List<Store> searchByName(String name) {
    String lowerCaseSearch = name.toLowerCase();
    String sql = "SELECT * FROM stores WHERE LOWER (stores.name) LIKE '%" + lowerCaseSearch + "%' ORDER BY name";
    List<Store> storeResults;
    try (Connection con = DB.sql2o.open()) {
      storeResults = con.createQuery(sql)
        .executeAndFetch(Store.class);
    }
    return storeResults;
  }

  public static List<Store> searchByState(String state) {
    String lowerCaseSearch = state.toLowerCase();
    String sql = "SELECT * FROM stores WHERE LOWER (stores.state) LIKE '%" + lowerCaseSearch + "%' ORDER BY name";
    List<Store> storeResults;
    try (Connection con = DB.sql2o.open()) {
      storeResults = con.createQuery(sql)
        .executeAndFetch(Store.class);
    }
    return storeResults;
  }


    public void removeBrand(int brand_id) {
      try (Connection con = DB.sql2o.open()) {
        String removeBrand = "DELETE FROM brands_stores WHERE brand_id = :brand_id AND store_id = :id";
        con.createQuery(removeBrand)
          .addParameter("brand_id", brand_id)
          .addParameter("id", this.getId())
          .executeUpdate();
         }
    }

}
