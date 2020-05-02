import React from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonBackButton, IonButton,
    IonButtons, IonCard, IonCardContent, IonCardHeader, IonCardTitle,
    IonContent, IonHeader, IonPage, IonTitle, IonToolbar
} from '@ionic/react';

import './Result.css';
import {Chart} from "react-google-charts";

interface ResultProps {
    res : any
}

const CalculatedResults: React.FC<RouteComponentProps> = (props) => {

    const myProps : ResultProps = props.location.state as ResultProps || {res:null};
    const textLang = "09591764-05a6-41c3-b499-4e936471a991";
    const urlCsv = 'http://localhost:8080/get-results-csv';

    function submitSurveyId(surveyId: string) {
        let formData = new FormData();
        formData.append('surveyId', surveyId);
        fetch(urlCsv, {
            method: 'POST',
            body: formData
        })
            .then(response => response);
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton defaultHref="/home" />
                    </IonButtons>
                    <IonTitle>Survey Calculation Results</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonCard class="welcome-card">
                    <IonCardHeader>
                        {submitSurveyId(textLang)}
                        <IonCardTitle class={"title"}>{"Your Survey Result"}</IonCardTitle>
                        <IonButton class={"button"} href={"/export-survey-results"}>Export Data as CSV</IonButton>
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