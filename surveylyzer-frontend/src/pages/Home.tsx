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

    return (
        <IonPage>
            <HeaderNav />
            <IonContent>
                <DropArea history={props.history} />
            </IonContent>
        </IonPage>
    );
};
export default Home;