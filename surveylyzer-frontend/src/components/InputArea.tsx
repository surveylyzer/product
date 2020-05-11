import { IonFab, IonFabButton, IonIcon, IonItem, IonLabel, IonInput, IonCardContent, IonText } from "@ionic/react";
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

    function submitIdAndGetResult(surveyId: any) {
        let formData = new FormData();
        formData.append('surveyId', surveyId);
        fetch(resultsUrl, {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(json => {
                console.log('Fetched json: ', json);
                // make all row-arrays the same length (for google charts):
                // if json
                let maxL = json[0].length;
                let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL-row.length,0)).fill(null)]});
                console.log('Googel JSON: ', res);
                // update state
                goToResult(res);
            })
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

        </IonCardContent>
    );
};
export default InputArea;
