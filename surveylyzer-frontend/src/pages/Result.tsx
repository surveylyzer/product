import { Chart } from "react-google-charts";

import React from 'react';
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


async function HandleDataInput() {
    var fetchedData;
    await fetch("http://localhost:8080/pdfResult")
        .then((response)=>{
            return response.body;
        })
        .then((data)=>{
            console.log("FetchedData: "+ JSON.stringify(data))
            fetchedData = data;
        })

    return JSON.stringify(fetchedData);
}



async function GetDataInput() {
    const res = await fetch("http://localhost:8080/pdfResult");
    const json = await res.json().then();
    console.log("Fetched JSON stringify:"+JSON.stringify(json));
    return JSON.stringify(json);
}





const Result: React.FC = () => {
    HandleDataInput();
    GetDataInput();


    var dataInput =JSON.parse("[[\"City\",\"1\",\"2\",\"3\",\"4\"],[\"Question 1\",23,47,2,5]]");


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
                        <IonCardSubtitle>Simple Bar Chart / It is a hardcoded example</IonCardSubtitle>
                        <IonCardTitle>Survey Results</IonCardTitle>
                    </IonCardHeader>
                    <Chart
                        width={'700px'}
                        height={'500px'}
                        chartType="BarChart"
                        loader={<div>Loading Chart</div>}
                        data = {dataInput}
                        /*data={[
                            ['City', '1', '2', '3', '4'],
                            ['Question 1', 23, 47, 2, 5],
                            ['Question 2', 24, 10, 40, 3],
                            ['Question 3', 3, 57, 15, 1],
                            ['Question 4', 67, 5, 3, 2],
                            ['Question 5', 2, 5, 1, 69],
                        ]}*/
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
                    <IonCardHeader>
                        <IonCardSubtitle>Simple Pie Chart / It is a hardcoded example</IonCardSubtitle>
                        <IonCardTitle>Daily Activities</IonCardTitle>
                    </IonCardHeader>
                    <IonCardContent>
                        <Chart
                            width={'700px'}
                            height={'500px'}
                            chartType="PieChart"
                            loader={<div>Loading Chart</div>}
                            data={[
                                ['Task', 'Hours per Day'],
                                ['Work', 11],
                                ['Eat', 2],
                                ['Commute', 2],
                                ['Watch TV', 2],
                                ['Sleep', 7],
                            ]}
                            options={{
                                title: 'My Daily Activities',
                            }}
                            rootProps={{ 'data-testid': '1' }}
                        />
                    </IonCardContent>
                </IonCard>
            </IonContent>
        </IonPage>
    );
};
export default Result;
