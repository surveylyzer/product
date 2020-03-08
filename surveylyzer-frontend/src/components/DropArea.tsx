import { IonContent, IonIcon } from "@ionic/react";
import React from "react";
import Dropzone from "react-dropzone";
import './DropArea.css';
import { cloudUploadOutline } from "ionicons/icons";

const DropArea: React.FC = () => {
    let dragIsActive = false;
    function handleFileInput(fileIn: any[]) {
        dragIsActive = false;
        let file = fileIn[0];
        let arr = file?.name?.split('.');
        if (arr && arr[arr?.length - 1].toLowerCase() === 'pdf') {
            console.log(file);
        }
    }

    return (
        <IonContent>
            <Dropzone onDrop={acceptedFiles => handleFileInput(acceptedFiles)}
                onDragEnter={() => dragIsActive = true} 
                onDragLeave={() => dragIsActive = false}>
                {({ getRootProps, getInputProps }) => (
                    <section className="dropzone">
                        <div className="content" {...getRootProps()}>
                            <input pattern=".+\.pdf$" {...getInputProps()} />
                            <span className="icon"><IonIcon icon={cloudUploadOutline} /></span>
                            {dragIsActive ?
                                <p>Drop here ...</p> :
                                <p>Drag 'n' drop your <strong>surveys</strong> here, <br />
                                    or click to select files</p>
                            }

                        </div>
                    </section>
                )}
            </Dropzone>
        </IonContent>
    );
}
export default DropArea;
