package ch.zhaw.results;

import ch.zhaw.db.Storage;
import ch.zhaw.domain.Survey;
import ch.zhaw.surveylyzerbackend.SurveylyzerBackendApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Get result data from database or error messages.
 */
@RestController
@RequestMapping("/pdfResult")
public class ResultController {

    @Autowired
    private Storage dataBase;

    @GetMapping()
    public Object[][] getResult() {
                /*
        Dummy Code:
        Get Result from DB.
        if null --> invalid id / id not found message
        if found but empty result --> still working message
        if found with result --> return result (like getFakeData())

        (Du kannst auch ein anderes Objekt zurückgeben, muss kein Object[][] sein...
        Es gibt für den Controller auch Wrapper-Klassen für HTTP Status Meldungen.
        Z.B. "Spring Boot ResponseEntity" --> Als Inspiration ;-))
        */
        UUID surveyId = SurveylyzerBackendApplication.surveyId;
        String[] header = {"Questions", "1", "2", "3"};
        Object[][] dummyResult = {header};

        if (surveyId != null) {
            // todo: saveResult should be invoked within Application Class instead here
            saveResult(surveyId, SurveylyzerBackendApplication.results);
            Survey survey = dataBase.getSurveyResultById(surveyId);
            if (survey.getResult() != null) {
                return survey.getResult();
            } else {
                return dummyResult;
            }

        } else {
            return dummyResult;
        }
    }

    public void saveResult(UUID surveyId, Object[][] result) {
        Survey survey = dataBase.getSurveyResultById(surveyId);
        survey.setResult(result);
        dataBase.saveOrUpdateSurveyResult(survey);
    }

    private static Object[][] simulateBusy() {
        // return null; // "not found"
        // return simulateBusy(); // "found but empty --> result not yet ready"
        Object[][] busy = {{"$$busy$$"}};
        return busy;
    }

}
