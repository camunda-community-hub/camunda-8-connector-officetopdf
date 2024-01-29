[![Community badge: Incubating](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)
[![Community extension badge](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)


# camunda-8-connector-officetopdf

This connector creates a PDF from an Microsoft Office or an Open Office document.

## Principle
A Process Variable provides the document, as a File Reference [File Storage](https://github.com/camunda-community-hub/zebee-cherry-filestorage).
or via a Load Connector.

The connector transform the document in PDF. It may be a multi page PDF.

## Inputs
| Name                           | Description                                                                                                         | Class             | Default | Level     |
|--------------------------------|---------------------------------------------------------------------------------------------------------------------|-------------------|---------|-----------|
| sourceFileVariableFileVariable | For the file to convert                                                                                             | java.lang.Object  |         | REQUIRED  |
| destinationFileName            | Destination file name                                                                                               | java.lang.String  |         | REQUIRED  |
| destinationStorageDefinition   | Storage Definition use to describe how to save the file. If not provided, the Source file's storage definition is used. | java.lang.String  | JSON    | OPTIONAL  |


The sourceFileVariable file is accessible via a reference. Visit [File Storage](https://github.com/camunda-community-hub/zebee-cherry-filestorage) library.
The file can be saved as a process variable in JSON, or in an external Folder, a CMIS repository, etc...

Connectors to load and save files are available in the repository [Cherry Framework](https://github.com/camunda-community-hub/zeebe-cherry-framework)
Component [LoadFileFromDisk](https://github.com/camunda-community-hub/zeebe-cherry-framework/tree/main/src/main/java/io/camunda/cherry/files/LoadFileFromDiskWorker.java)
load a file from a disk, and [SaveFileToDisk](https://github.com/camunda-community-hub/zeebe-cherry-framework/tree/main/src/main/java/io/camunda/cherry/files/SaveFileToDiskWorker.java) save the file on a disk.

The destinationStorageDefinition indicates where the PDF file is produced. According to the File Storage library,
If the **destinationStorageDefinition** is not provided, then the storage of the source file is used. 


## Output
| Name             | Description                                         | Class             | Level    |
|------------------|-----------------------------------------------------|-------------------|----------|
| pdfFileVariable  | FileVariable converted (a File Variable Reference)  | java.lang.Object  | REQUIRED |

## BPMN Errors

| Name             | Explanation       |
|------------------|-------------------|
| LOAD_FILE_ERROR  | Load File error   |
| CONVERSION_ERROR | Conversion error  | 


## Manipulating file

Via the **File Storage** library, The process variable contains only a reference. 
The core document, which may be saved in a Folder, Temporary Folder, or CMIS or any repository available via the library.

The result is saved in the File Storage: this is why the connetor ask for a StorageDefinition. 
The result process variable will contains only the reference to the file. If no "destinationStorageDefinition"
is provided, the PDF is saved in the same storage than the source document. 

To get the result of the file on a file system, use any connector or application using the File Storage API.

Find the user documentation in our [Camunda Platform 8 Docs](https://docs.camunda.io/docs/components/integration-framework/connectors/out-of-the-box-connectors/slack/).

# Build

```bash
mvn clean package
```

Two jars are produced. The jar with all dependency can be upload in the [Cherry Framework](https://github.com/camunda-community-hub/zeebe-cherry-framework)

## Element Template

The element templates can be found in the [element-templates/office-to-PDF.json](element-templates/office-to-PDF.json) file.