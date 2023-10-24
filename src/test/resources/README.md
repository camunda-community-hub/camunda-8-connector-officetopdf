# Run the test


To test it:
1/ deploy the officeToPdf.bpm process
2/ Deploy the connector on a runtime 
3/ Create a process instance

The connector load a WORD and a OPENOFFICE file from Internet

The Source File is :
`````json
{
  "storageDefinition": "URL", 
  "content": "https://github.com/pierre-yves-monnet/camunda-8-connector-officetopdf/raw/a51fc1b29add729087936eb0460b028ba8b5e977/src/test/resources/OfficeToPdfExample.docx"
}
`````

A PDF is created and saved in the TEMP Folder on the runtime (so not very easy to access). 
Check if you see any errors.
