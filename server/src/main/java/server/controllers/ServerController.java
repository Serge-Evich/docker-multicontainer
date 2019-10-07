package server.controllers;

import server.forms.IndexForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.PgClientService;
import server.services.RedisClientService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class ServerController {

    @Resource
    private PgClientService pgClientService;
    @Resource
    private RedisClientService redisClientService;

    @GetMapping("/")
    public String sayHi() {
        return "Hi";
    }

    @GetMapping("/values/all")
    public List<Integer> getAllValues() {
        return pgClientService.getAll();
    }

    @GetMapping("/values/current")
    public Map<String, String> getAllCurrentValues() {
        return redisClientService.getAll();
    }

    @PostMapping("/values")
    public ResponseEntity<String> postIndex(@ModelAttribute IndexForm indexForm) {
        int index;
        try {
            index = Integer.parseInt(indexForm.getIndex());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("It's not a number");
        }
        if (index > 40) {
            return ResponseEntity.badRequest().body("Index too high");
        }
        redisClientService.publish("insert", String.valueOf(index));
        pgClientService.insertValue(index);
        return ResponseEntity.ok("OK");
    }

}
