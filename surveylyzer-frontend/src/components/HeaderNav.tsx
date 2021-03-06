import React from 'react';
import {
    IonItem, IonHeader, IonToolbar, IonButtons, IonImg, IonTitle, IonIcon,
} from '@ionic/react';
import { helpCircleOutline } from 'ionicons/icons';

/**
 * Header navigation elements
 */
const HeaderNav: React.FC = () => {
    return (
        <IonHeader>
            <IonToolbar>
                <IonButtons slot="start">
                    <a href='/'>
                        <IonImg className="logo" src='./assets/icon/surveylyzer_icon.png' alt="Logo" />
                    </a>
                    <IonItem routerLink="/home">
                        <IonTitle>Surveylyzer</IonTitle>
                    </IonItem>
                    <IonItem routerLink="/help" id={"help_icon"}>
                        <IonIcon icon={helpCircleOutline} color="warning"></IonIcon>
                    </IonItem>
                </IonButtons>
            </IonToolbar>
        </IonHeader>
    );
}

export default HeaderNav;
