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

