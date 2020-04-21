import { IonContent, IonIcon } from "@ionic/react";
import React from "react";
import Dropzone from "react-dropzone";
import './DropArea.css';
import { cloudUploadOutline, cloudUpload } from "ionicons/icons";

const DropArea: React.FC = () => {
    let dragIsActive = false;

    function handleInput(fileIn: any[], inputType:string) {
        dragIsActive = false;
        let file = fileIn[0];
        let arr = file?.name?.split('.');
        if (arr && arr[arr?.length - 1].toLowerCase() === 'pdf') {
            console.log(file);
        }
        let formData = new FormData();
        formData.append('file1',file);
        formData.append('pdfType', inputType);
        //Post Template
        fetch('http://localhost:8080/pdf', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log(inputType+" "+file.name+" has been uploaded now");
        })
    }

    return (
        <IonContent>

            <Dropzone onDrop={acceptedFiles => handleInput(acceptedFiles,"templateFile")}
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
            <Dropzone onDrop={acceptedFiles => handleInput(acceptedFiles,"dataFile")}
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
