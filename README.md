# camunda-8-connector-officetopdf

This connector creates a PDF from an Office or an Open Office document.

A Process Variable provides the document (See connector camunda-8-LoadFileFromDisk).

The process variable references the core document, which may be saved in a Folder, Temporary Folder, or CMIS.
The result is saved in a process variable using the same File Storage library. If no "destinationStorageDefinition
" is provided, and the PDF is saved in the same storage. New storage can be provided.

To get the result of the file on a file system, use the camunda-8-SaveFileToDisk connector.

