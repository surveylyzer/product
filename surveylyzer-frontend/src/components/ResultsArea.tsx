import { IonFab, IonFabButton, IonIcon, IonItem, IonLabel, IonInput, IonCardContent, IonText } from "@ionic/react";
import React from 'react';
import './DropArea.css';
import { play } from "ionicons/icons";

import { History } from "history";

interface ResultProps {
    history: History;
}

const ResultsArea: React.FC<ResultProps> = ({ history }) => {

    // const [surveyId, setSurveyId] = useState(null);

    function submitIdAndGetResult(surveyId: any) {
        let formData = new FormData();
        formData.append('surveyId', surveyId);
        fetch('http://localhost:8080/visualizeResults', {
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
        history.push('/calculatedResults', { res: res});
    }

    const text = "09591764-05a6-41c3-b499-4e936471a991";
    // const textLang = "b8718b71-48f9-4a4f-b964-99e201b60560";



    return (
        <IonCardContent>
            <IonItem>
                <IonLabel position="floating">Results ID</IonLabel>
                <IonInput class={"spacing"}></IonInput>
                {/*<IonInput class={"spacing"}  value={surveyId}  onChange={event => setSurveyId(event.target.value)} type="text" name="surveyId" />*/}
            </IonItem>
            <IonText>Copy here your 'Results ID' to access visualization of your survey</IonText>


            <IonFab vertical="bottom" horizontal="end" slot="fixed">
                <IonFabButton onClick={() => submitIdAndGetResult(text)}>
                    <IonIcon icon={play} />
                </IonFabButton>
            </IonFab>

        </IonCardContent>
    );
};
export default ResultsArea;
