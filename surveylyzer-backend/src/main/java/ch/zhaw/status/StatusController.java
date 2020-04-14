package ch.zhaw.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status")
public class StatusController {

    private static Status worfklowStatus = new Status();

    @GetMapping
    public ResponseEntity<Status> getStatus() {
        return new ResponseEntity<>(worfklowStatus, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Status> updateStatus(@RequestBody Status status) {
        worfklowStatus.updateStatus(status);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

}
