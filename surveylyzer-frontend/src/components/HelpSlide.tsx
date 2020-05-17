import React from 'react';
import {
    IonSlide,
    IonCard,
    IonCardSubtitle,
    IonCardTitle,
    IonCardContent,
    IonCardHeader,
    IonIcon,
    IonImg,
    IonItem,
    IonList
} from '@ionic/react';
import { pin } from 'ionicons/icons';

interface SlideProps {
    title: string
    subtitle: string
    imgPath: string
    items: []
}

/**
 * Display a slide help page slide.
 * @param param0
 */
const HelpSlide: React.FC<SlideProps> = ({ title, subtitle, imgPath, items }) => {
    const regexSplit = new RegExp('[/.]', 'g');
    const regexRepl = new RegExp('[-+*_.]', 'g');
    let SlideImage = null;
    if (imgPath) {
        let imgPathArr = imgPath.split(regexSplit);
        let imgName = imgPathArr[imgPathArr.length - 2].replace(regexRepl, ' ');
        SlideImage = (<IonImg alt={imgName} src={imgPath} />)
    }
    return (
        <IonSlide>
            <IonCard>
                {SlideImage}
                <IonCardHeader>
                    <IonCardSubtitle>{subtitle}</IonCardSubtitle>
                    <IonCardTitle>{title}</IonCardTitle>
                </IonCardHeader>
                <IonCardContent>
                    <IonIcon icon={pin} slot="start" color="warning" />
                    <IonList>
                        {items.map((item, i) =>
                            <IonItem key={i}>{item}</IonItem>
                        )}
                    </IonList>
                </IonCardContent>
            </IonCard>
        </IonSlide>
    );
};
export default HelpSlide;
