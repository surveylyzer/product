import { Chart } from "react-google-charts";
import React, {useCallback, useEffect, useState} from 'react';
import {
    IonBackButton,
    IonButtons,
    IonContent,
    IonCard,
    IonCardHeader,
    IonCardSubtitle,
    IonCardTitle,
    IonCardContent,
    IonHeader,
    IonPage,
    IonTitle,
    IonToolbar,
    IonButton, IonImg
} from '@ionic/react';
import { RouteComponentProps } from "react-router";

import './Result.css';

interface ResultProps {
    surveyId : string,
    surveyFile : File
}

const Result: React.FC<RouteComponentProps> = (props) => {
    // Init
    const myProps : ResultProps = props.location.state as ResultProps || {surveyId:null, surveyFile :null};
    const [resData, setResData] = useState([]);
    const url = 'http://localhost:8080/resultObject';
    const urlRawData = 'http://localhost:8080/rawResults';

    const fetchResult = useCallback(() => {
        fetch(url)
            .then(response => response.json())
            .then(json => {
                if (json.some((row: string | string[]) => row.includes('$$busy$$'))) {
                    alert('server is still working...');
                    setTimeout(() => { fetchResult(); }, 2000);
                }
                else {
                    console.log('Fetched json: ', json);
                    // make all row-arrays the same length (for google charts):
                    // if json
                    let maxL = json[0].length;
                    let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL-row.length,0)).fill(null)]});
                    console.log('Googel JSON: ', res);
                    // update state
                    setResData(res);
                }
            })
            .catch((err) => { console.log(err); alert('id not found') });
    }, []);


    // Fetch Result Data
    // Similar to componentDidMount and componentDidUpdate:
    useEffect(() => {
        const properties : ResultProps = props.location.state as ResultProps || {surveyId:null, surveyFile :null};
        function submitSurveyPdfAndGetResult(file: any, surveyId: string) {
            let formData = new FormData();
            formData.append('file', file);
            formData.append('surveyId', surveyId);
            fetch(url, {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(json => {
                    if (json.some((row: string | string[]) => row.includes('$$busy$$'))) {
                        alert('server is still working...');
                        setTimeout(() => { fetchResult(); }, 2000);
                    }
                    else {
                        console.log('Fetched json: ', json);
                        // make all row-arrays the same length (for google charts):
                        // if json
                        let maxL = json[0].length;
                        let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL-row.length,0)).fill(null)]});
                        console.log('Googel JSON: ', res);
                        // update state
                        setResData(res);
                    }
                })
        }
        submitSurveyPdfAndGetResult(properties.surveyFile, properties.surveyId);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [fetchResult]); // [] --> only on "Mount and Unmount", pass function avoids missing dependency error

    const surveyName = myProps?.surveyFile?.name.replace(".pdf", "");
    const rawDataUrl = urlRawData + '?' + 'surveyId=' + myProps?.surveyId;

    function renderData(resData: any) {
        if (resData.length === 0) {
            return (
                <div>
                    <IonImg class='processing' src='./assets/img/processing.gif' alt="processing" />
                    <IonCardSubtitle class='info'> We are processing your request... It can take some time </IonCardSubtitle>
                </div>
            )
        } else {
            return (
                <Chart
                    width={'100%'}
                    height={'75vh'}
                    chartType="BarChart"
                    loader={<div>Loading Chart</div>}
                    data={resData}
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
            );
        }
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
                        <IonButton href={"/export-survey-results"}>Export Data as CSV</IonButton>
                        <IonCardTitle class={"title"}>{surveyName.toUpperCase()}</IonCardTitle>
                        <IonCardSubtitle class={"subtitle"}>Not patient enough to wait for the survey result... ? </IonCardSubtitle>
                        <div className="block">
                            <IonCardSubtitle>Copy and paste this link in new browser tab to access the <b>raw data: </b>
                                <span className="url">{rawDataUrl}</span>
                            </IonCardSubtitle>
                            <IonCardSubtitle>Copy and paste this ID to put them in the id field on the home page to access the
                                <b> data visualization: </b> <span className="url">{myProps?.surveyId}</span>
                            </IonCardSubtitle>
                        </div>

                    </IonCardHeader>
                    <IonCardContent>
                        {renderData(resData)}
                    </IonCardContent>
                </IonCard>
            </IonContent>
        </IonPage>
    );
};
export default Result;
