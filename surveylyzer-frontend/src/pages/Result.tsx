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

declare let google: any;

const Result: React.FC = () => {

    fetch('http://localhost:8080/pdfResult')
        .then(function(response){ return response.json(); })
        .then(function(data) {

            const table: any[] = [];
            table.push(['question', 'item 1', 'item 2', 'item 3']);
            for (var i = 0; i < data.length; i++) {
                let val = 0;
                if (data[i].eval[2] != null) {
                    val = data[i].eval[2];
                }
                let text: String = "'" + data[i].questionText + "'";
                const question = [text, data[i].eval[0], data[i].eval[1], val];
                table.push(question);
            }
            google.charts.load('current', {'packages':['bar']});
            google.charts.setOnLoadCallback(drawStuff);

            function drawStuff() {
                var data = new google.visualization.arrayToDataTable(table, false);

                var options={
                    title: 'Answers',
                        chartArea: { width: '50%' },
                    colors: ['#124868', '#259BDE', '#7BC8F4'],
                        hAxis: {
                        title: 'Total',
                            minValue: 0,
                    },
                    vAxis: {
                        title: 'Survey',
                    },
                };

                var chart = new google.charts.Bar(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
            return table;
        });


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
                    <IonCardContent id={'chart_div'}/>
                </IonCard>
            </IonContent>
        </IonPage>
    );
};
export default Result;
