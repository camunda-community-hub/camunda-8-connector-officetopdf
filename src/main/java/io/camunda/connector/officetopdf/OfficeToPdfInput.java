package io.camunda.connector.officetopdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.camunda.connector.cherrytemplate.CherryInput;
import io.camunda.filestorage.StorageDefinition;
import jakarta.validation.constraints.NotEmpty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The Cherry Input is not mandatory, this is just a guideline
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeToPdfInput implements CherryInput {

  public static final String INPUT_SOURCE_FILE_VARIABLE = "sourceFileVariable";
  public static final String INPUT_DESTINATION_FILE_NAME = "destinationFileName";
  public static final String INPUT_DESTINATION_STORAGEDEFINITION = "destinationStorageDefinition";
  public static final String LABEL = "label";
  public static final String NAME = "name";
  public static final String CLASS = "class";
  public static final String LEVEL = "level";
  public static final String EXPLANATION = "explanation";
  @NotEmpty String sourceFileVariable;
  @NotEmpty String destinationFileName;
  String destinationStorageDefinition;

  public String getSourceFileVariable() {
    return sourceFileVariable;
  }

  public String getDestinationFileName() {
    return destinationFileName;
  }

  public String getDestinationStorageDefinition() {
    if (destinationStorageDefinition == null || destinationStorageDefinition.trim().isEmpty())
      return null;
    return destinationStorageDefinition;
  }

  /**
   * this method is exploded by Cherry Runtime to produce a nice element-template
   *
   * @return list of parameter
   */
  public List<Map<String, Object>> getInputParameters() {
    return Arrays.asList(Map.of(NAME, OfficeToPdfInput.INPUT_SOURCE_FILE_VARIABLE, // name
            LABEL, "Source file", // label
            CLASS, Object.class, // class
            LEVEL, "REQUIRED", // level
            EXPLANATION, "FileVariable for the file to convert"),
        Map.of(NAME, OfficeToPdfInput.INPUT_DESTINATION_FILE_NAME,// name
            LABEL, "Destination file name", // label
            CLASS, String.class, // class
            LEVEL, "REQUIRED",// level
            EXPLANATION, "Destination file name"),
        Map.of(NAME, OfficeToPdfInput.INPUT_DESTINATION_STORAGEDEFINITION, // name
            LABEL, "Destination storage definition", // label
            CLASS, String.class, // class
            "defaultValue", StorageDefinition.StorageDefinitionType.JSON.toString(), // default value
            LEVEL, "OPTIONAL", // level
            EXPLANATION,
            "Storage Definition use to describe how to save the file. If not provided, the Source file's storage definition is used."));
  }

}
