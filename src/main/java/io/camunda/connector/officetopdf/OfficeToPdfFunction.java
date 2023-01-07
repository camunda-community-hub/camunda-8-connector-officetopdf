package io.camunda.connector.officetopdf;

import io.camunda.file.storage.FileVariable;
import io.camunda.file.storage.FileRepoFactory;
import io.camunda.file.storage.FileVariableReference;
import io.camunda.file.storage.StorageDefinition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.error.ConnectorException;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OutboundConnector(name = "OfficeToPdf", inputVariables = {
    OfficeToPdfInput.INPUT_SOURCE_FILE_VARIABLE,
    OfficeToPdfInput.INPUT_DESTINATION_FILE_NAME,
    OfficeToPdfInput.INPUT_DESTINATION_STORAGEDEFINITION },
    type = OfficeToPdfFunction.TYPE_PDF_CONVERT_TO)
public class OfficeToPdfFunction implements OutboundConnectorFunction {
  Logger logger = LoggerFactory.getLogger(OfficeToPdfFunction.class.getName());
  /**
   * Different BPMN Errors this connector can throw
   */
  public static final String BPMERROR_CONVERSION_ERROR = "CONVERSION_ERROR";
  public static final String BPMERROR_LOAD_FILE_ERROR = "LOAD_FILE_ERROR";

  /**
   * Topic for this connector
   */
  public static final String TYPE_PDF_CONVERT_TO = "c-pdf-convert-to";

  @Override
  public OfficeToPdfOutput execute(OutboundConnectorContext context) throws Exception {
    OfficeToPdfInput officeInput = context.getVariablesAsType(OfficeToPdfInput.class);

    if (officeInput.sourceFileVariable == null) {
      throw new ConnectorException(BPMERROR_LOAD_FILE_ERROR,
          "Connector [" + getName() + "] cannot read file[" + OfficeToPdfInput.INPUT_SOURCE_FILE_VARIABLE + "]");
    }
    FileRepoFactory fileVariableFactory = FileRepoFactory.getInstance();
    FileVariableReference docSourceReference = FileVariableReference.fromJson(officeInput.getSourceFileVariable());
    FileVariable docSource = fileVariableFactory.loadFileVariable(docSourceReference);
    byte[] docContent = docSource.getValue();

    try {

      // Load the source document
      docSourceReference = FileVariableReference.fromJson(officeInput.getSourceFileVariable());
      FileVariable docSource2 = fileVariableFactory.loadFileVariable(docSourceReference);
      logger.info("OfficeToPdf: load file [" + FileVariableReference.getIdentification(docSourceReference) + "] :"+ docContent.toString());

      // build the report
      final IXDocReport report;
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(docSource.getValue());
      report = XDocReportRegistry.getRegistry().loadReport(byteArrayInputStream, TemplateEngineKind.Velocity);

      final IContext iContext = report.createContext();
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      report.convert(iContext, Options.getTo(ConverterTypeTo.PDF), out);

      // save the result in a File Variable - use the destination StorageDefinition or use the source one if nothing is provided
      StorageDefinition storageDefinitionDestination = null;
      if (officeInput.getDestinationStorageDefinition() == null) {
        storageDefinitionDestination = docSource.getStorageDefinition();
      } else {
        storageDefinitionDestination = StorageDefinition.getFromString(officeInput.getDestinationStorageDefinition());
      }
      FileVariable docPdf = fileVariableFactory.createFileVariable(docSource.getStorageDefinition());
      docPdf.setValue(out.toByteArray());
      docPdf.setName(officeInput.getDestinationFileName());
      FileVariableReference docPdfReference = fileVariableFactory.saveFileVariable(docPdf);

      // register the result in the Output variable
      OfficeToPdfOutput officeToPdfOutput = new OfficeToPdfOutput();
      officeToPdfOutput.pdfFileVariable = docPdfReference.toJson();

      return officeToPdfOutput;
    } catch (Exception e) {
      logger.error("OfficeToPdf: Conversion error file [" + (docSourceReference == null ?
          "" :
          FileVariableReference.getIdentification(docSourceReference)) + "] " + e);

      throw new ConnectorException(BPMERROR_CONVERSION_ERROR,
          "Worker [" + getName() + "] cannot convert file[" + officeInput.getDestinationFileName() + "] : " + e);
    }
  }

  public String getName() {
    return "OfficeToPdf";
  }
}
