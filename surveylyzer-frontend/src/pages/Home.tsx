import React from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonContent, IonPage
} from '@ionic/react';

// Custom Components
import DropArea from '../components/DropArea';
import HeaderNav from '../components/HeaderNav';
// Our CSS
import './Home.css';

const Home: React.FC<RouteComponentProps> = (props) => {
    async function GetStatus(){
        await fetch("http://localhost:8080/workflow")
            .then(response => response.json())
            .then(json => {
                console.log("ConsoleLog JSON:",json);
                console.log("ConsoleLog Longing Value:",json.pdfAnalyzerFinished);
                if(json.pdfAnalyzerFinished === true){
                    props.history.push('/result');
                } else {
                    alert("PDF Analyzer has not yet finished");
                }
            });
    }
    return (
        <IonPage>
            <HeaderNav />
            <IonContent>
                <DropArea />
            </IonContent>
        </IonPage>
    );
};
export default Home;