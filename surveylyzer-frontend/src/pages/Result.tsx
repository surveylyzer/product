import { Chart } from "react-google-charts";
import React, { useCallback, useEffect, useState } from 'react';
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
    surveyId: string,
    surveyFile: File
}

const Result: React.FC<RouteComponentProps> = (props) => {
    // --------------------------------------------
    // Init
    // --------------------------------------------
    const [resData, setResData] = useState([]);
    const myProps: ResultProps = props.location.state as ResultProps || { surveyId: null, surveyFile: null };
    const surveyName = myProps?.surveyFile?.name?.replace(".pdf", "");
    const surveyID = String(myProps?.surveyId || "");
    const hostURL = window.location.protocol + '//' + window.location.host;
    const resultURL = hostURL + '/resultObject';
    const csvURL = hostURL + '/get-results-csv';
    const rawDataUrl = hostURL + '/rawResults?surveyId=' + surveyID;

    // --------------------------------------------
    // Submit Functions
    // --------------------------------------------
    const submitSurveyPdfAndGetResult = useCallback((file: any, surveyId: string) => {
        let formData = new FormData();
        formData.append('file', file);
        formData.append('surveyId', surveyId);
        fetch(resultURL, {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(json => {
                console.log('Fetched json: ', json);
                // make all row-arrays the same length (for google charts):
                // if json
                let maxL = json[0].length;
                let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL - row.length, 0)).fill(null)] });
                console.log('Googel JSON: ', res);
                // update state
                setResData(res);
            })
    }, []);

    function submitSurveyId(surveyId: string) {
        let formData = new FormData();
        formData.append('surveyId', surveyId);
        fetch(csvURL, {
            method: 'POST',
            body: formData
        })
            .then(response => response);
        // Todo: response ? - error handling...
    }

    // TODO: Maybe use this for other use case "get result from survey id.."
    // const fetchResult = useCallback(() => {
    //     fetch(resultURL)
    //         .then(response => response.json())
    //         .then(json => {
    //             if (json.some((row: string | string[]) => row.includes('$$busy$$'))) {
    //                 alert('server is still working...');
    //                 setTimeout(() => { fetchResult(); }, 2000);
    //             }
    //             else {
    //                 console.log('Fetched json: ', json);
    //                 // make all row-arrays the same length (for google charts):
    //                 // if json
    //                 let maxL = json[0].length;
    //                 let res = json.map((row: []) => { return [...row, ...Array(Math.max(maxL - row.length, 0)).fill(null)] });
    //                 console.log('Googel JSON: ', res);
    //                 // update state
    //                 setResData(res);
    //             }
    //         })
    //         .catch((err) => { console.log(err); alert('id not found') });
    // }, []);

    // --------------------------------------------
    // Fetch Result Data
    // --------------------------------------------
    // Similar to componentDidMount and componentDidUpdate:
    useEffect(() => {
        // const properties: ResultProps = props.location.state as ResultProps || { surveyId: null, surveyFile: null };
        if (myProps?.surveyId && myProps?.surveyFile) {
            submitSurveyPdfAndGetResult(myProps.surveyFile, myProps.surveyId);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [submitSurveyPdfAndGetResult, myProps]); // [] --> only on "Mount and Unmount", pass function avoids missing dependency error

    // eslint-disable-next-line prefer-template

    // --------------------------------------------
    // Render Functions
    // --------------------------------------------
    function renderData(resData: any) {
        if (!surveyID) { return <IonCardContent>We're sorry because an Error Happend</IonCardContent>; }
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

    function renderLinks() {
        if (!surveyID) { return <IonCardContent>We're sorry because an Error Happend</IonCardContent>; }
        if (resData.length === 0) {
            return (
                <div>
                    <IonCardSubtitle class={"subtitle"}>Not patient enough to wait for the survey result... ?</IonCardSubtitle>
                    <div className="block">
                        <IonCardSubtitle>Copy and save this link to access the <b>raw data: </b>
                            <span className="url">{rawDataUrl}</span>
                        </IonCardSubtitle>
                        <IonCardSubtitle>Copy and save this ID to put them in the "Result ID" field on the home page to access the
                            <b> data visualization: </b> <span className="url">{surveyID}</span>
                        </IonCardSubtitle>
                    </div>
                </div>
            )
        } else {
            submitSurveyId(surveyID);
            return (
                <div>
                    <IonButton class={"button"} href={"/export-survey-results"}>Export Data as CSV</IonButton>
                    <IonCardSubtitle>Click this link to access the <b>raw data: </b>
                        <a href={rawDataUrl} className="url_ready">{rawDataUrl}</a>
                    </IonCardSubtitle>
                    <IonCardSubtitle>Save this ID to access the
                        <b> data visualization: </b> <span className="url">{surveyID}</span>
                    </IonCardSubtitle>
                </div>
            )
        }
    }

    // --------------------------------------------
    // Returning UI-Elements
    // --------------------------------------------
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
                        <IonCardTitle class={"title"}>{surveyName?.toUpperCase()}</IonCardTitle>
                        {renderLinks()}
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
