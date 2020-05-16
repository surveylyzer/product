import {IonAlert, IonCardContent, IonFab, IonFabButton, IonIcon} from "@ionic/react";
import React, { useState} from "react";
import Dropzone from "react-dropzone";
import './DropArea.css';
import { cloudUploadOutline, cloudUpload, play } from "ionicons/icons";

import { History } from "history";

interface DropAreaProps {
    history: History;
}

const DropArea: React.FC<DropAreaProps> = ({ history }) => {
    //Init
    const [templateText, setTemplateText] = useState("Drag 'n' drop your Template here");
    const [surveyText, setSurveyText] = useState("Drag 'n' drop your Survey here");
    const [templateFile, setTemplateFile] = useState(null);
    //Alerts
    const [showAlert, setShowAlert] = useState(false);
    const [subtitle, setSubtitle] = useState("Unknown Error");
    const [message, setMessage] = useState("Something went wrong");
    // Values to be passed to result
    const [surveyFile, setSurveyFile] = useState(null);
    const hostURL = window.location.protocol + '//' + window.location.host;
    const templateUrl = hostURL + '/template';

    let dragIsActive = false;

    function uploadFile(fileIn: any[], inputType: string) {
        dragIsActive = false;
        let file = fileIn[0];
        let arr = file?.name?.split('.');
        let fileName = file.path;
        if (arr && arr[arr?.length - 1].toLowerCase() === 'pdf') {
            console.log("uploadFile -> ", file);
        }
        if (!fileName.toString().endsWith(".pdf")) {
            setAlert("File Extension Error", "Surveylyzer can proceed ONLY documents in PDF - Format!");
        } else {
            if (inputType === "templateFile") {
                setTemplateFile(file);
                setTemplateText("Uploaded TEMPLATE: " + file.name + "   (Mistake? Just reupload correct file)")
            } else if (inputType === "dataFile") {
                setSurveyFile(file);
                setSurveyText("Uploaded SURVEY: " + file.name + "   (Mistake? Just reupload correct file)")
            }
        }
    }

    function submitAllFiles() {
        if (templateFile === null) {
            setAlert("Template Error", "Template file has not been uploaded!");
        } else if (surveyFile === null) {
            setAlert("Survey Error", "Survey file has not been uploaded!");
        }
        else {
            submitTemplate(templateFile, "templateFile");
            console.log("submitAllFiles -> SUCCESS - Template has been submitted");
        }
    }

    function setAlert(subtitle: string, message: string) {
        setShowAlert(true);
        setSubtitle(subtitle);
        setMessage(message);
    }

    function submitTemplate(file: any, inputType: string) {
        let formData = new FormData();
        formData.append('file1', file);
        formData.append('pdfType', inputType);
        fetch(templateUrl, {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(json => {
                console.log("submitTemplate -> Template: ", file.name + " has been submitted");
                console.log("submitTemplate -> Survey ID: ", json.toString());
                let fetchedSurveyId = json.toString();
                // setSurveyId(fetchedSurveyId);
                goToResult(fetchedSurveyId, surveyFile != null ? surveyFile : null);
            })
    }

    function goToResult(id: String, file: File | null) {
        if (!id || !file) { console.error("ID and Survey File mustn't be null!!"); return; }
        history.push('/result', { surveyId: id, surveyFile: file });
        window.location.reload();
    }

    return (
        <IonCardContent>
            <Dropzone onDrop={acceptedFiles => uploadFile(acceptedFiles, "templateFile")}
                onDragEnter={() => dragIsActive = true}
                onDragLeave={() => dragIsActive = false}>
                {({ getRootProps, getInputProps }) => (
                    <section className="dropzone">
                        <div className="content" {...getRootProps()}>
                            <input pattern=".+\.pdf$" {...getInputProps()} />
                            <span className="icon"><IonIcon icon={cloudUpload} /></span>
                            {dragIsActive ?
                                <p>Drop here ...</p> :
                                <p>{templateText}</p>
                            }
                        </div>
                    </section>
                )}
            </Dropzone>

            <Dropzone onDrop={acceptedFiles => uploadFile(acceptedFiles, "dataFile")}
                onDragEnter={() => dragIsActive = true}
                onDragLeave={() => dragIsActive = false}>
                {({ getRootProps, getInputProps }) => (
                    <section className="dropzone">
                        <div className="content" {...getRootProps()}>
                            <input pattern=".+\.pdf$" {...getInputProps()} />
                            <span className="icon"><IonIcon icon={cloudUploadOutline} /></span>
                            {dragIsActive ?
                                <p>Drop here ...</p> :
                                <p>{surveyText}</p>
                            }
                        </div>
                    </section>
                )}
            </Dropzone>

            <IonFab vertical="bottom" horizontal="end" slot="fixed">
                <IonFabButton id={'submit'} onClick={() => submitAllFiles()}>
                    <IonIcon icon={play} />
                </IonFabButton>
            </IonFab>

            <IonAlert
                isOpen={showAlert}
                onDidDismiss={() => setShowAlert(false)}
                header={"Alert"}
                subHeader={subtitle}
                message={message}
                buttons={['OK']}
            />

        </IonCardContent>
    );
}
export default DropArea;
