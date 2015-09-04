import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;
import java.util.Arrays;


public class App {
    public static void main(String[] args) {
    	staticFileLocation("/public");
    	String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("brands", Brand.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("stores", Store.all());
      model.put("template", "templates/stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/add-store", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String city = request.queryParams("city");
      String state = request.queryParams("state");
      Store newStore = new Store(name, city, state);
      newStore.save();
      response.redirect("/stores");
      return null;
    });


    }
}
