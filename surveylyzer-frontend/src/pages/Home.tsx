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


function HandleDataInput() {
    fetch("http://localhost:8080/pdfResult")
        .then((response)=>{
            return response.json();
        })
        .then((data)=>{
            console.log(data);
            alert("See fetched results at http://localhost:8080/pdfResult");
        })
}

const Home: React.FC<RouteComponentProps> = (props) => {
  return (
    <IonPage>
      <HeaderNav />

      <IonContent>

        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => props.history.push('/result')}>
            <IonIcon icon={calculator} />
          </IonFabButton>

            <IonFabButton onClick={()=> HandleDataInput()}>
                <IonIcon icon={construct} />
            </IonFabButton>
        </IonFab>

        <DropArea />
      </IonContent>
    </IonPage>
  );
};

export default Home;
