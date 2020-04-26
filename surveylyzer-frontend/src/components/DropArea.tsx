import {IonContent, IonFab, IonFabButton, IonIcon} from "@ionic/react";
import React, { useState} from "react";
import Dropzone from "react-dropzone";
import './DropArea.css';
import {cloudUploadOutline, cloudUpload, play} from "ionicons/icons";

import {History} from "history";

interface DropAreaProps {
    history : History;
}

const DropArea: React.FC<DropAreaProps> = ({history}) => {
    //Init
    const [templateText, setTemplateText] = useState("Drag 'n' drop your Template here");
    const [surveyText, setSurveyText] = useState("Drag 'n' drop your Survey here");
    const [templateFile, setTemplateFile] = useState(null);
    // Values to be passed to result
    const [surveyFile, setSurveyFile] = useState(null);
    // const [surveyId, setSurveyId] = useState(""); // wird nicht gebraucht, denn wir gehen zur nächsten Seite, 
                                                     // sobald wir sie haben...

    let dragIsActive = false;

    function uploadFile(fileIn: any[], inputType:string) {
        dragIsActive = false;
        let file = fileIn[0];
        let arr = file?.name?.split('.');
        if (arr && arr[arr?.length - 1].toLowerCase() === 'pdf') {
            console.log("uploadFile -> ", file);
        }
        if(inputType === "templateFile"){
            setTemplateFile(file);
            setTemplateText("Uploaded TEMPLATE: "+ file.name +"   (Mistake? Just reupload correct file)")
        } else if(inputType === "dataFile"){
            setSurveyFile(file);
            setSurveyText("Uploaded SURVEY: "+ file.name +"   (Mistake? Just reupload correct file)" )
        }
    }

    function submitAllFiles(){
        if(templateFile===null){
            alert("FAIL -> Template file has not been uploaded!");
        } else if (surveyFile===null){
            alert("FAIL -> Survey file has not been uploaded!");
        } else {
            submitTemplate(templateFile,"templateFile");
            console.log("submitAllFiles -> SUCCESS - Template has been submitted");
            // while(!readyToPass){
            //     alert("Please wait a while");
            //     readyToPass();
            // }
            // goToResult(); --> sollte aufgerufen werden, sobald du die Antwort erhälst, 
            //                   also in der fetch methode, siehe unten...
            }
    }

    function submitTemplate(file: any, inputType: string) {
        let formData = new FormData();
        formData.append('file1', file);
        formData.append('pdfType', inputType);
        fetch('http://localhost:8080/template', {
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

    // function readyToPass(){
    //     if (surveyId === ""){
    //         return false;
    //     } else {
    //         return true;
    //     }
    // }

    function goToResult(id: String, file: File | null) {
        if (!file) { console.error("Survey File mustn't be null!!"); return; }
        history.push('/result', { surveyId: id, surveyFile: file });
    }

    return (
        <IonContent>
            <Dropzone onDrop={acceptedFiles => uploadFile(acceptedFiles,"templateFile")}
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

            <Dropzone onDrop={acceptedFiles => uploadFile(acceptedFiles,"dataFile")}
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
                <IonFabButton onClick= {()=>submitAllFiles()
                }>
                    <IonIcon icon={play} />
                </IonFabButton>
            </IonFab>

        </IonContent>
    );
}
export default DropArea;
