package io.camunda.connector.officetopdf;

import io.camunda.connector.api.annotation.Secret;

import javax.validation.constraints.NotEmpty;

public class OfficeToPdfInput {

  public static final String INPUT_SOURCE_FILE_VARIABLE = "sourceFileVariable";
  @NotEmpty String sourceFileVariable;

  public static final String INPUT_DESTINATION_FILE_NAME = "destinationFileName";
  @NotEmpty String destinationFileName;

  public static final String INPUT_DESTINATION_STORAGEDEFINITION = "destinationStorageDefinition";
  String destinationStorageDefinition;



  public String getSourceFileVariable() {
    return sourceFileVariable;
  }

  public String getDestinationFileName() {
    return destinationFileName;
  }

  public String getDestinationStorageDefinition() {
    if (destinationStorageDefinition==null || destinationStorageDefinition.trim().isEmpty())
      return null;
    return destinationStorageDefinition;
  }

}
