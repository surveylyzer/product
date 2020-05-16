import React from 'react';
import {
    IonBackButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonPage,
    IonTitle,
    IonToolbar,
    IonSlides, IonImg
} from '@ionic/react';
import HelpSlide from '../components/HelpSlide';

const Help: React.FC = () => {
    // --------------------------------------------
    // Slide data
    // --------------------------------------------
    const itemsS1 = [
        "Format: PDF",
        "Maximal Anzahl Seiten: 1",
        "Markierungsfarbe: Gelb = rgb(255,255,0)",
        "Optionsfelder müssen Rechtecke sein",
        "Quer- Hochformat muss mit der Umfrage übereinstimmen",
        "Empfohlen wird Arial 11 und eine Mindestgrösse der Rechtecke von 20x20px"
    ];
    const itemsS2 = [
        "Format: PDF",
        "Muss zwingend dem Layout der Vorlage entsprechen." +
        " Rotationsabweichungen (z.B. bei schrägem Einscannen) werden bis zu 20° tolleriert.",
        "Pro Seite eine ausgefüllte Umfrage",
        "Es werden zuerzeit nur Rechtecke, die farblich stärker" +
        " von den restlichen abweichen, erkannt.",
        "Durchgestrichene Rechtecke sind farblich dünkler und" +
        " werden somit als angekreuzt betrachtet!"
    ];
    const itemsS3 = [
        "Nach dem Upload erhalten Sie eine ID der Form 'XXXXXXXX-XXXX-XXXXXXXXX-XXXXXXXXXXXX'.",
        "Mit dieser ID können Sie die Ergebnisse der Umfrage jederzeit abfragen.",
        "Sie müssen nicht warten, bis die Umfrage fertig ausgewertet ist." +
        " Kommen Sie einfach später mit Ihrer ID wieder.",
        "API Abfragen sind auf folgender URL möglich:" +
        "'surveylyzermaster.herokuapp.com/rawResults?surveyId={ID}'."
    ];
    const itemsS4 = [
        "Mit der Nutzung von Surveylyzer erklären Sie sich damit einverstanden, dass:",
        "...das Template und das Resultat der Auswertung gespeichert werden.",
        "...nicht zwingend alle Auswertungen korrekt sein müssen.",
        "Sie können ihre Daten anhand der SurveyID löschen lassen."
    ];

    // --------------------------------------------
    // Returning UI-Elements
    // --------------------------------------------
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <a href='/'>
                            <IonImg className="logo" src='./assets/icon/surveylyzer_icon.png' alt="Logo" />
                        </a>
                        <IonBackButton defaultHref="/home" />
                    </IonButtons>
                    <IonTitle>Surveylyzer - Help</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonSlides pager={true} options={slideOpts}>
                    <HelpSlide
                        title="Template (Vorlage)"
                        subtitle="Vorgaben"
                        imgPath="./assets/img/template_example.JPG"
                        items={itemsS1 as []} />
                    <HelpSlide
                        title="Survey (Umfrage)"
                        subtitle="Vorgaben"
                        imgPath="./assets/img/survey_example.JPG"
                        items={itemsS2 as []} />
                    <HelpSlide
                        title="SurveyID"
                        subtitle="Info"
                        imgPath=""
                        items={itemsS3 as []} />
                    <HelpSlide
                        title="Nutzungshinweise"
                        subtitle="Info"
                        imgPath=""
                        items={itemsS4 as []} />
                </IonSlides>
            </IonContent>
        </IonPage>
    );
};
export default Help;

const slideOpts = {
    initialSlide: 0,
    // autoHeight: true,
    speed: 500,
    breakpoints: {
        // when window width is >= 720px
        720: {
            slidesPerView: 1,
            spaceBetween: 20
        },
        // when window width is >= 2080px
        2080: {
            slidesPerView: 2,
            spaceBetween: 30
        },
        // when window width is >= 8K
        7680: {
            slidesPerView: 4,
            spaceBetween: 40
        }
    }
};
