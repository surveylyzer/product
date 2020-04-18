import React from 'react';
import { RouteComponentProps } from 'react-router';
import {
  IonContent, IonPage, IonIcon, IonFab, IonFabButton,
} from '@ionic/react';
import { calculator, construct } from 'ionicons/icons';
// Custom Components
import DropArea from '../components/DropArea';
import HeaderNav from '../components/HeaderNav';
// Our CSS
import './Home.css';

async function CheckStatus() {
    let templateReceived;
    let surveyReceived;
    let passable;
    await fetch("http://localhost:8080/workflow")
        .then(response =>{
            return response.json();
        })
        .then((data)=>{
        templateReceived = data.templateReceived;
        surveyReceived = data.surveyReceived;
        alert("Template here? "+templateReceived+" SurveyReceived? "+surveyReceived);
            if(templateReceived&&surveyReceived){
                console.log("Template and Survey are here ");
                passable = true;
                return passable
            } else {
                console.log("Something missing")
                passable =  false;
                return passable
            }
    })


}


//   <IonFabButton onClick= {() => {props.history.push('/result')}

const Home: React.FC<RouteComponentProps> = (props) => {


    function GoToResult(){
        //CheckStatus(templateReceived,surveyReceived)
        let state = CheckStatus();
        console.log("State: "+ state);
        if(state){
            props.history.push('/result')
        } else {
            alert("Template or Survey is missing");
        }

    }


    return (
    <IonPage>
      <HeaderNav />

      <IonContent>

        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick= {()=>GoToResult()
          }>
            <IonIcon icon={calculator} />
          </IonFabButton>

            <IonFabButton onClick={()=> CheckStatus()}>
                <IonIcon icon={construct} />
            </IonFabButton>
        </IonFab>

        <DropArea />
      </IonContent>
    </IonPage>
  );
};

export default Home;
