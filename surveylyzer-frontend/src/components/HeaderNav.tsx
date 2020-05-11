import React from 'react';
import {
    IonItem, IonHeader, IonToolbar, IonButtons, IonImg, IonTitle
} from '@ionic/react';

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
                </IonButtons>
            </IonToolbar>
        </IonHeader>
    );
}

export default HeaderNav;
