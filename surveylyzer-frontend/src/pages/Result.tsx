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
    IonToolbar
} from '@ionic/react';

function HandleDataInput() {
    fetch("http://localhost:8080/pdfResult")
        .then((response)=>{
            return response.json();
        })

}

const Result: React.FC = () => {
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
                            <IonCardSubtitle>Simple Bar Chart / It is a hardcoded example</IonCardSubtitle>
                            <IonCardTitle>Survey Results</IonCardTitle>
                        </IonCardHeader>
                        <Chart
                            width={'700px'}
                            height={'500px'}
                            chartType="BarChart"
                            loader={<div>Loading Chart</div>}
                            data={()=> HandleDataInput()}
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
