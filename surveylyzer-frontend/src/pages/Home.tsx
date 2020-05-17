import React from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonCard, IonCardHeader, IonCardTitle, IonPage
} from '@ionic/react';

// Custom Components
import DropArea from '../components/DropArea';
import HeaderNav from '../components/HeaderNav';
import InputArea from '../components/InputArea';
// Our CSS
import './Home.css';

const Home: React.FC<RouteComponentProps> = (props) => {
    return (
        <IonPage>
            <HeaderNav />
            <IonCard>
                <IonCardHeader>
                    <IonCardTitle>{"Calculate new result"}</IonCardTitle>
                </IonCardHeader>
                <DropArea history={props.history} />
            </IonCard>
            <IonCard>
                <IonCardHeader>
                    <IonCardTitle>{"Visualize your calculated result"}</IonCardTitle>
                </IonCardHeader>
                <InputArea history={props.history} />
            </IonCard>
        </IonPage>
    );
};
export default Home;