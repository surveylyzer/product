package ch.zhaw.workflow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    private static Workflow worworkflow = new Workflow();

    @GetMapping
    public ResponseEntity<Workflow> getWorkflow() {
        return new ResponseEntity<>(worworkflow, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Workflow> updateWorkflow(@RequestBody Workflow workflow) {
        worworkflow.updateWorkflow(workflow);
        return new ResponseEntity<>(workflow, HttpStatus.CREATED);
    }

    public static Workflow getWorkflowStatus(){
        return worworkflow;
    }
}
