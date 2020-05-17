import {
    IonFab,
    IonFabButton,
    IonIcon,
    IonItem,
    IonLabel,
    IonInput,
    IonCardContent,
    IonText,
    IonAlert
} from "@ionic/react";
import React, { useState } from 'react';
import './DropArea.css';
import { play } from "ionicons/icons";
import { History } from "history";

interface ResultProps {
    history: History;
}

/**
 * Get survey results by survey ID.
 * Then move to calculated results for displaying them.
 * @param history
 */
const InputArea: React.FC<ResultProps> = ({ history }) => {
    // --------------------------------------------
    // Init
    // --------------------------------------------
    var surveyRes: any = null;
    var surveyTitle: string | null = null;
    const [surveyId, setSurveyId] = useState<string>();
    const hostURL = window.location.protocol + '//' + window.location.host;
    const resultsUrl = hostURL + '/visualizeResults/?surveyId=' + surveyId;
    const resultsTitleUrl = hostURL + '/resultTitle?surveyId=' + surveyId;
    //Alerts
    const [showAlertInput, setShowAlertInput] = useState(false);
    const [subtitleInput, setSubtitleInput] = useState("Unknown Error");
    const [messageInput, setMessageInput] = useState("Something went wrong");

    // --------------------------------------------
    // Functions
    // --------------------------------------------
    function submitIdAndGetResult(surveyId: any) {
        console.log("response: ", surveyId);
        if (surveyId == null || "") {
            setAlertInput("ID Error", "'Results ID' Input is empty, please enter a valid ID!");
        } else {
            console.log("fetch res");
            fetchRes();
            fetchResTitle();
        }
    }

    function fetchRes() {
        fetch(resultsUrl)
            .then(response => errorHandling(response))
            .then(response => {
                const contentType = response.headers.get('content-type');
                if (!contentType || !contentType.includes('application/json')) {
                    throw new TypeError("Not correct or empty ID, there is no JSON!");
                }
                return response.json();
            })
            .then(json => {
                console.log('Fetched json: ', json);
                // make all row-arrays the same length (for google charts):
                let maxL = json[0].length;
                let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL - row.length, 0)).fill(null)] });
                console.log('Googel JSON: ', res);
                // save result
                surveyRes = res;
                console.log("res after set: ", surveyRes);
                tryGoToResult();
            }).catch(error => console.log(error))
    }

    function fetchResTitle() {
        console.log("fetch title");
        fetch(resultsTitleUrl)
            .then(response => response.text())
            .then(data => {
                console.log("fetched title data: ", data);
                // setSurveyTitle(data);
                surveyTitle = data;
                tryGoToResult();
            })
            .catch(error => console.log(error));
    }

    function errorHandling(response: any) {
        // No content or ID is not valid
        if (response.status === 204) {
            setAlertInput("ID Error", "Your ID is not correct! Insert correct ID to visualize your results.");
            console.log("No Content was found: ", response);
        }
        // Precondition failed, ID was null or empty
        else if (response.status === 412) {
            setAlertInput("ID Error", "'Results ID' - Input can not be empty!");
        }
        else if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
    }

    function setAlertInput(subtitle: string, message: string) {
        setShowAlertInput(true);
        setSubtitleInput(subtitle);
        setMessageInput(message);
    }

    function tryGoToResult() {
        if (surveyRes && surveyTitle) {
            history.push('/calculatedResults', { res: surveyRes, surveyId: surveyId, surveyTitle: surveyTitle });
            window.location.reload();
        }
    }

    // --------------------------------------------
    // Returning UI-Elements
    // --------------------------------------------
    return (
        <IonCardContent>
            <IonItem>
                <IonLabel position="floating">Results ID</IonLabel>
                <IonInput id={'input_id'} class={"spacing"} value={surveyId} onIonChange={event => setSurveyId(event.detail.value || "")} type="text" name="surveyId" />
            </IonItem>
            <IonText>Copy here your 'Result ID' to access visualization of your survey</IonText>
            <IonFab vertical="bottom" horizontal="end" slot="fixed">
                <IonFabButton id={'upload_id'} onClick={() => submitIdAndGetResult(surveyId)}>
                    <IonIcon icon={play} />
                </IonFabButton>
            </IonFab>

            <IonAlert
                isOpen={showAlertInput}
                onDidDismiss={() => setShowAlertInput(false)}
                header={"Alert"}
                subHeader={subtitleInput}
                message={messageInput}
                buttons={['OK']}
            />
        </IonCardContent>
    );
};
export default InputArea;
