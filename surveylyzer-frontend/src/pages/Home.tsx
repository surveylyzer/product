import React from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonContent, IonPage, IonIcon, IonFab, IonFabButton,
} from '@ionic/react';
import { calculator } from 'ionicons/icons';
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
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick= {()=>GetStatus()
                    }>
                        <IonIcon icon={calculator} />
                    </IonFabButton>
                </IonFab>
                <DropArea />
            </IonContent>
        </IonPage>
    );
};
export default Home;