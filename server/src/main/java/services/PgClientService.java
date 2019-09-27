package services;

import io.reactiverse.pgclient.PgClient;
import io.reactiverse.pgclient.PgPool;
import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.pgclient.PgRowSet;
import io.reactiverse.pgclient.Row;
import io.reactiverse.pgclient.Tuple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PgClientService {

    private PgPool pgClient;

    public PgClientService() {
        PgPoolOptions options = new PgPoolOptions()
                .setPort(Integer.parseInt(System.getProperty("PGPORT")))
                .setHost(System.getProperty("PGHOST"))
                .setDatabase(System.getProperty("PGDB"))
                .setUser(System.getProperty("PGUSER"))
                .setPassword(System.getProperty("PGPASSWORD"))
                .setMaxSize(10);
        pgClient = PgClient.pool(options);
    }

    public List<Integer> getAll() {
        List<Row> rows = new ArrayList<>();
        pgClient.query("SELECT * FROM values", pgRowSetAsyncResult -> {
            if (pgRowSetAsyncResult.succeeded()) {
                PgRowSet pgRowSet = pgRowSetAsyncResult.result();
                pgRowSet.forEach(row -> rows.add(row));
            } else {
                System.out.println("pgClient error: " + pgRowSetAsyncResult.cause());
            }
        });
        return rows.stream().map(row -> row.getInteger("values")).collect(Collectors.toList());
    }

    public void insertValue(int index) {
        pgClient.preparedQuery("INSERT INTO VALUES(number) VALUES($1)", Tuple.of(index), pgRowSetAsyncResult -> {});
    }
}
