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
import React, {useState} from 'react';
import './DropArea.css';
import { play } from "ionicons/icons";

import { History } from "history";

interface ResultProps {
    history: History;
}

const InputArea: React.FC<ResultProps> = ({ history }) => {

    const [surveyId, setSurveyId] = useState<string>();
    const hostURL = window.location.protocol + '//' + window.location.host;
    const resultsUrl = hostURL + '/visualizeResults';
    //Alerts
    const [showAlertInput, setShowAlertInput] = useState(false);
    const [subtitleInput, setSubtitleInput] = useState("Unknown Error");
    const [messageInput, setMessageInput] = useState("Something went wrong");

    function submitIdAndGetResult(surveyId: any) {
        console.log("response: ", surveyId);
        if(surveyId == null || "") {
            setAlertInput("ID Error", "'Results ID' Input is empty, please enter a valid ID!");
        } else {
            let formData = new FormData();
            formData.append('surveyId', surveyId);
            fetch(resultsUrl, {
                method: 'POST',
                body: formData
            })
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
                    // if json
                    let maxL = json[0].length;
                    let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL-row.length,0)).fill(null)]});
                    console.log('Googel JSON: ', res);
                    // update state
                    goToResult(res);
                    window.location.reload();
                }).catch(error => console.log(error))
        }

    }

    function errorHandling(response: any) {
        // No content or ID is not valid
        if (response.status === 204) {
            setAlertInput("ID Error", "Your ID is not correct! Insert correct ID to visualize your results.");
            console.log("No Content was found: " , response);
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

    function goToResult(res: any) {
        if (!res) { console.error("ID and Survey File mustn't be null!!"); return; }
        history.push('/calculatedResults', { res: res, surveyId: surveyId});
    }

    // testID_1 = "420beefe-4643-4e7d-88ff-171ae1e2e73b";
    // testID_2 = "4ff38e89-fdc1-48db-b8a9-241168341886";

    return (
        <IonCardContent>
            <IonItem>
                <IonLabel position="floating">Results ID</IonLabel>
                <IonInput class={"spacing"}  value={surveyId} onIonChange={event => setSurveyId(event.detail.value || "")} type="text" name="surveyId" />
            </IonItem>
            <IonText>Copy here your 'Result ID' to access visualization of your survey</IonText>
            <IonFab vertical="bottom" horizontal="end" slot="fixed">
                <IonFabButton onClick={() => submitIdAndGetResult(surveyId)}>
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
