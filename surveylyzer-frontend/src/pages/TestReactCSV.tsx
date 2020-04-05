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
import HorizontalLine from "../components/HorizontalLine";
import TestData from "../data/TestData"

const TestReactCSV: React.FC = () => {
    const ShowData = ({ data }) => {
        return (<div><pre>{JSON.stringify(data, null, 2)}</pre></div>);
    };

    let headers = TestData.getHeaders();
    let dataArray = TestData.getDataArray();
    let dataLiteralObjArray = TestData.getDataLiteralObjArray();
    let dataString = TestData.getDataString();
    let dataJson = TestData.getDataJson();

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonBackButton defaultHref="/home" />
                    </IonButtons>
                    <IonTitle>Test-Page for the export of survey results as CSV</IonTitle>
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
                <br />

                <HorizontalLine color="red" bHeight={2} bStyle="dashed" />

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
