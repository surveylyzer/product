import { IonContent, IonIcon } from "@ionic/react";
import React from "react";
import Dropzone from "react-dropzone";
import './DropArea.css';
import { cloudUploadOutline, cloudUpload } from "ionicons/icons";

const DropArea: React.FC = () => {
    let dragIsActive = false;
    function handleDataFile(fileIn: any[]) {
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
        fetch('http://localhost:8080/pdf', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log("File has been uploaded");
        })
    }

    //TODO Code duplication, will be corrected as soon as parameter passing is known
    function handleTemplateFile(fileIn: any[]) {
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
        fetch('http://localhost:8080/pdf', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log("File has been uploaded");
        })
    }

    return (
        <IonContent>

            <Dropzone onDrop={acceptedFiles => handleTemplateFile(acceptedFiles)}
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
            <Dropzone onDrop={acceptedFiles => handleDataFile(acceptedFiles)}
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
