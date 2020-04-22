import { IonContent, IonIcon } from "@ionic/react";
import React from "react";
import Dropzone from "react-dropzone";
import './DropArea.css';
import { cloudUploadOutline, cloudUpload } from "ionicons/icons";

const DropArea: React.FC = () => {
    let dragIsActive = false;


    //TODO Code duplication of Inputhandling and Status updating
    function handleTemplateFileInput(fileIn: any[]) {
        dragIsActive = false;
        let file = fileIn[0];
        let arr = file?.name?.split('.');
        let  pdfType = "templateFile";
        if (arr && arr[arr?.length - 1].toLowerCase() === 'pdf') {
            console.log(file);
        }
        let formData = new FormData();
        formData.append('file1',file);
        formData.append('pdfType', pdfType);
        //Post Template
        fetch('http://localhost:8080/pdf', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log("Template File "+file.name+" has been uploaded");
            updateStatusTemplateInput(file.name)
        })
    }

    //TODO Code duplication of Inputhandling and Status updating
    function handleDataFileInput(fileIn: any[]) {
        dragIsActive = false;
        let file = fileIn[0];
        let arr = file?.name?.split('.');
        let  pdfType = "dataFile";
        if (arr && arr[arr?.length - 1].toLowerCase() === 'pdf') {
            console.log(file);
        }
        let formData = new FormData();
        formData.append('file1',file);
        formData.append('pdfType', pdfType);
        // Post Data
        fetch('http://localhost:8080/pdf', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log("Data File "+file.name+" has been uploaded");
            updateStatusDataInput(file.name);
        })
    }


    //TODO Code duplication of Inputhandling and Status updating
    function updateStatusTemplateInput(templateName : any){
        fetch('/workflow',{
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body:  JSON.stringify({"templateReceived":true, "templateName":templateName})
        }).then(response => {
            console.log("Status of "+templateName+" has been updated")
        })
    }


    //TODO Code duplication of Inputhandling and Status updating
    function updateStatusDataInput(surveyName : any){
        fetch('/workflow',{
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body:  JSON.stringify({"surveyReceived":true, "surveyName":surveyName})
        }).then(response => {
            console.log("Status of "+surveyName+" has been updated")
        })
    }


    return (
        <IonContent>

            <Dropzone onDrop={acceptedFiles => handleTemplateFileInput(acceptedFiles)}
                      onDragEnter={() => dragIsActive = true}
                      onDragLeave={() => dragIsActive = false}>
                {({ getRootProps, getInputProps }) => (
                    <section className="dropzone">
                        <div className="content" {...getRootProps()}>
                            <input pattern=".+\.pdf$" {...getInputProps()} />
                            <span className="icon"><IonIcon icon={cloudUpload} /></span>
                            {dragIsActive ?
                                <p>Drop here ...</p> :
                                <p>Drag 'n' drop your <strong>PDF Template</strong> here, <br />
                                    or click to select files</p>
                            }
                        </div>
                    </section>
                )}
            </Dropzone>
            <Dropzone onDrop={acceptedFiles => handleDataFileInput(acceptedFiles)}
                      onDragEnter={() => dragIsActive = true}
                      onDragLeave={() => dragIsActive = false}>
                {({ getRootProps, getInputProps }) => (
                    <section className="dropzone">
                        <div className="content" {...getRootProps()}>
                            <input pattern=".+\.pdf$" {...getInputProps()} />
                            <span className="icon"><IonIcon icon={cloudUploadOutline} /></span>
                            {dragIsActive ?
                                <p>Drop here ...</p> :
                                <p>Drag 'n' drop your <strong>PDF DATA</strong> here, <br />
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
