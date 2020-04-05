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
                    <IonItem routerLink="/result">
                        View Result
                    </IonItem>
                    <IonItem routerLink="/result-test">
                        TESTING Export
                    </IonItem>
                </IonButtons>
            </IonToolbar>
        </IonHeader>
    );
}

export default HeaderNav;
