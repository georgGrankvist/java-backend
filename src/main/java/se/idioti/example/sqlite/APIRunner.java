package se.idioti.example.sqlite;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;

import static spark.Spark.*;

/**
 * This demonstrates how to expose the storage through a REST API using Spark.
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class APIRunner {

	public static void main(String[] args) throws Exception {
		port(5000);
		
		Storage storage = new Storage();
		storage.setup();

		get("/",(req, res) -> {
			res.type("application/json");
			return new Gson().toJson(new Gson().toJsonTree(storage.fetchUnicorns()));
		});

		post("/", (req, res) -> {
				res.type("application/json");
				Unicorn unicorn = new Gson().fromJson(req.body(), Unicorn.class);
				storage.addUnicorn(unicorn);

				return new Gson().toJson("UNICORN ADDED");
		});

		get("/:id", (request, response) -> {
			response.type("application/json");
			return new Gson().toJson(new Gson().toJsonTree
					(storage.fetchUnicorn(Integer.parseInt(":id"))));
		});

		put("/:id", (request, response) -> {
			response.type("application/json");
			Unicorn toEdit = new Gson().fromJson(request.body(),Unicorn.class);
			storage.updateUnicorn(toEdit);
			return new Gson().toJson(new Gson().toJsonTree(toEdit));

		});

		delete("/:id", (request, response) -> {
			response.type("application/json");
			storage.deleteUnicorn(Integer.parseInt(":id"));
			return new Gson().toJson("user deleted");
		});
	}

}
