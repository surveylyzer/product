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
    IonButton
} from '@ionic/react';
import { RouteComponentProps } from "react-router";

interface ResultProps {
    surveyId : String,
    surveyFile : File
}

const Result: React.FC<RouteComponentProps> = (props) => {
    // Init
    const myProps : ResultProps = props.location.state as ResultProps || {surveyId:null, surveyFile :null};
    const [resData, setResData] = useState([]);
    const url = 'http://localhost:8080/pdfResult';
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
        console.log(";-)");
        console.log("ERHALTENE Props auf Result-Page:", myProps);
        fetchResult();
    }, [fetchResult]); // [] --> only on "Mount and Unmount", pass function avoids missing dependency error


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
                        <IonCardTitle>Survey Results</IonCardTitle>
                        <IonCardSubtitle>Bar Chart</IonCardSubtitle>
                        <IonCardSubtitle>{"ID: " + myProps?.surveyId}</IonCardSubtitle>
                        <IonCardSubtitle>{"File: " + myProps?.surveyFile?.name}</IonCardSubtitle>
                    </IonCardHeader>
                    <IonCardContent>
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
                    </IonCardContent>

                    <IonCardHeader>
                        <IonCardSubtitle>CandleStick Chart</IonCardSubtitle>
                    </IonCardHeader>
                    <IonCardContent>
                        <Chart
                            width={'100%'}
                            height={'75vh'}
                            chartType="CandlestickChart"
                            loader={<div>Loading Chart</div>}
                            data={resData}
                            options={{
                                legend: 'none',
                                bar: { groupWidth: '80%' },
                                candlestick: {
                                    fallingColor: { strokeWidth: 0, fill: '#a52714' }, // red
                                    risingColor: { strokeWidth: 0, fill: '#0f9d58' }, // green
                                },
                            }}
                        />
                    </IonCardContent>
                </IonCard>
            </IonContent>
        </IonPage>
    );
};
export default Result;
