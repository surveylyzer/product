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
  return (
    <IonPage>
      <HeaderNav />

      <IonContent>

        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => props.history.push('/result')}>
            <IonIcon icon={calculator} />
          </IonFabButton>
        </IonFab>

        <DropArea />
      </IonContent>
    </IonPage>
  );
};

export default Home;
