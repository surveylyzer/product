import React from 'react';
import {
    IonBackButton,
    IonButton,
    IonButtons,
    IonCard,
    IonCardContent,
    IonCardHeader,
    IonContent,
    IonHeader,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import { CSVLink } from "react-csv";

const TestReactCSV: React.FC = () => {
    let headers = [
        { label: "Frage", key: "question" },
        { label: "Wert 1", key: "one" },
        { label: "Wert 2", key: "two" },
        { label: "Wert 3", key: "three" },
        { label: "Wert 4", key: "four" }
    ];
    let dataArray = [
        ['City', '1', '2', '3', '4'],
        ['Question 1', 23, 47, 2, 5],
        ['Question 2', 24, 10, 40, 3],
        ['Question 3', 3, 57, 15, 1],
        ['Question 4', 67, 5, 3, 2],
        ['Question 5', 2, 5, 1, 69]
    ];
    let dataLiteralObjArray = [
        { question: "City", one: 1, two: 2, three: 3, four: 4 },
        { question: "Q1", one: 23, two: 47, three: 2, four: 5 },
        { question: "Q2", one: 24, two: 10, three: 40, four: 3 },
        { question: "Q3", one: 3, two: 57, three: 15, four: 1 }
    ];
    let dataString = `City; 1; 2; 3; 4
Question 1; 23; 47; 2; 5
Question 2; 24; 10; 40; 3
Question 3; 3; 57; 15; 1
Question 4; 67; 5; 3; 2
Question 5; 2; 5; 1; 69
        `;
    let dataJson = [
        {
            "question": "City",
            "one": 1,
            "two": 2,
            "three": 3,
            "four": 4
        },
        {
            "question": "Q1",
            "one": 23,
            "two": 47,
            "three": 2,
            "four": 5
        },
        {
            "question": "Q2",
            "one": 24,
            "two": 10,
            "three": 40,
            "four": 3
        },
        {
            "question": "Q3",
            "one": 3,
            "two": 57,
            "three": 15,
            "four": 1
        }
    ];

    const ShowData = ({ data }) => {
        // (destructured) data could be a prop for example
        return (<div><pre>{JSON.stringify(data, null, 2)}</pre></div>);
    };
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton defaultHref="/home" />
                    </IonButtons>
                    <IonTitle>Testing Page for exporting Survey Result as CSV</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <section>
                    <h1>Export as CSV</h1>
                    <CSVLink
                        data={dataArray}
                        separator={";"}
                        filename={"fromArrayOfArrays.csv"}
                        target="_blank">
                        <IonButton expand="block" color="primary" fill="outline">Data as Array of Arrays</IonButton>
                    </CSVLink>
                    <CSVLink
                        data={dataLiteralObjArray}
                        headers={headers}
                        separator={";"}
                        filename={"fromArrayOfLiteralObj.csv"}
                        target="_blank">
                        <IonButton expand="block" color="secondary" fill="outline">Data as Array of Literal Objects</IonButton>
                    </CSVLink>
                    <CSVLink
                        data={dataString}
                        separator={";"}
                        filename={"fromString.csv"}
                        target="_blank">
                        <IonButton expand="block" color="tertiary" fill="outline">Data as String</IonButton>
                    </CSVLink>
                    <CSVLink
                        data={dataJson}
                        separator={";"}
                        enclosingCharacter={`'`}
                        filename={"fromJSON.csv"}
                        target="_blank">
                        <IonButton expand="block" color="success" fill="outline">Data as JSON</IonButton>
                    </CSVLink>
                </section>

                <section>
                    <h1>RAW Data</h1>
                    <IonCard>
                        <IonCardHeader><h2>Array of Arrays</h2></IonCardHeader>
                        <IonCardContent>{ShowData({ data: dataArray })}</IonCardContent>
                    </IonCard>
                    <IonCard>
                        <IonCardHeader><h2>Literal Object Array</h2></IonCardHeader>
                        <IonCardContent>{ShowData({ data: dataLiteralObjArray })}</IonCardContent>
                    </IonCard>
                    <IonCard>
                        <IonCardHeader><h2>String</h2></IonCardHeader>
                        <IonCardContent>{ShowData({ data: dataString })}</IonCardContent>
                    </IonCard>
                    <IonCard>
                        <IonCardHeader><h2>JSON</h2></IonCardHeader>
                        <IonCardContent>{ShowData({ data: dataJson })}</IonCardContent>
                    </IonCard>
                </section>
            </IonContent>
        </IonPage >
    );
};
export default TestReactCSV;
