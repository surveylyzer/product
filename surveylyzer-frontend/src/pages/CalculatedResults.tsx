import React from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonBackButton, IonButton,
    IonButtons, IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle,
    IonContent, IonHeader, IonImg, IonPage, IonTitle, IonToolbar
} from '@ionic/react';
import './Result.css';
import { Chart } from "react-google-charts";

interface ResultProps {
    res: any
    surveyId: string
    surveyTitle: string
}

/**
 * Displays already calculated results.
 * @param props
 */
const CalculatedResults: React.FC<RouteComponentProps> = (props) => {
    // --------------------------------------------
    // Init
    // --------------------------------------------
    const myProps: ResultProps = props.location.state as ResultProps || { res: null, surveyId: null };
    const hostURL = window.location.protocol + '//' + window.location.host;
    const csvURL = hostURL + '/get-results-csv';
    const rawDataUrl = hostURL + '/rawResults?surveyId=' + myProps?.surveyId;

    // --------------------------------------------
    // Functions
    // --------------------------------------------
    function submitSurveyId(surveyId: string) {
        let formData = new FormData();
        formData.append('surveyId', surveyId);
        fetch(csvURL, {
            method: 'POST',
            body: formData
        })
            .then(response => response)
            .catch(err => console.log("Error: ", err));
    }

    // --------------------------------------------
    // Returning UI-Elements
    // --------------------------------------------
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <a href='/'>
                            <IonImg className="logo" src='./assets/icon/surveylyzer_icon.png' alt="Logo" />
                        </a>
                        <IonBackButton defaultHref="/home" />
                    </IonButtons>
                    <IonTitle>Survey Calculation Results</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonCard class="welcome-card">
                    <IonCardHeader>
                        {submitSurveyId(myProps?.surveyId)}
                        <IonCardTitle class={"title"}>{myProps?.surveyTitle?.toUpperCase() ?? "Your Survey Result"}</IonCardTitle>
                        <IonButton class={"button"} href={"/export-survey-results"}>Export Data as CSV</IonButton>
                        <IonCardSubtitle>Click this link to access the <b>raw data: </b>
                            <a href={rawDataUrl} className="url_ready" id={"raw_data_link"}>{rawDataUrl}</a>
                        </IonCardSubtitle>
                        <IonCardSubtitle>Save this ID to access the
                            <b> data visualization: </b> <span className="url">{myProps?.surveyId}</span>
                        </IonCardSubtitle>
                    </IonCardHeader>
                    <IonCardContent>
                        <Chart
                            width={'100%'}
                            height={'75vh'}
                            chartType="BarChart"
                            loader={<div>Loading Chart</div>}
                            data={myProps?.res}
                            options={{
                                title: 'Answers',
                                chartArea: { width: '50%' },
                                colors: ['#124868', '#259BDE', '#7BC8F4', '#D3ECFB'],
                                hAxis: {
                                    title: 'Total',
                                    minValue: 0,
                                },
                                vAxis: {
                                    title: 'Survey',
                                },
                            }}
                        />
                    </IonCardContent>
                </IonCard>
            </IonContent>
        </IonPage>
    );
};
export default CalculatedResults;