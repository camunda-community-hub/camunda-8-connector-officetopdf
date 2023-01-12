[![Community badge: Incubating](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)
[![Community extension badge](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)


# camunda-8-connector-officetopdf

This connector creates a PDF from an Office or an Open Office document.

A Process Variable provides the document (See connector camunda-8-LoadFileFromDisk).

The process variable references the core document, which may be saved in a Folder, Temporary Folder, or CMIS.
The result is saved in a process variable using the same File Storage library. If no "destinationStorageDefinition
" is provided, and the PDF is saved in the same storage. New storage can be provided.

To get the result of the file on a file system, use the camunda-8-SaveFileToDisk connector.

Find the user documentation in our [Camunda Platform 8 Docs](https://docs.camunda.io/docs/components/integration-framework/connectors/out-of-the-box-connectors/slack/).

# Build

```bash
mvn clean package
```

## API

### Input

```json
{
  "sourceFileVariable": ".....",
  "destinationFileName": "MyPdf.pdf",
  "destinationStorageDefinition" : "JSON"
}
```

The sourceFileVariable file is accessible via a reference. Visit [File Storage](https://github.com/camunda-community-hub/zebee-cherry-filestorage) library.
The file can be saved as a process variable in JSON, or in an external Folder, a CMIS repository, etc...

Connectors to load and save files are available in the repository [Cherry Framework](https://github.com/camunda-community-hub/zeebe-cherry-framework)
Component [LoadFileFromDisk](https://github.com/camunda-community-hub/zeebe-cherry-framework/tree/main/src/main/java/io/camunda/cherry/files/LoadFileFromDiskWorker.java) 
load a file from a disk, and [SaveFileToDisk](https://github.com/camunda-community-hub/zeebe-cherry-framework/tree/main/src/main/java/io/camunda/cherry/files/SaveFileToDiskWorker.java) save the file on a disk.

The destinationStorageDefinition indicates where the PDF file is produced. According to the File Storage library, 
it can be on a disk, an external Folder, a CMIS repository, etc...



### Output

The response will contain the reference where the file is saved, according the storageDefinition

```json
{
  "result": {
    "pdfFileVariable": "..."
  }
}

```

## Element Template

The element templates can be found in the [element-templates/office-to-PDF.json](element-templates/office-to-PDF.json) file.