package io.camunda.connector.officetopdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.camunda.connector.cherrytemplate.CherryInput;
import io.camunda.filestorage.StorageDefinition;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The Cherry Input is not mandatory, this is just a guideline
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeToPdfInput implements CherryInput {

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
    return Arrays.asList(Map.of("name", OfficeToPdfInput.INPUT_SOURCE_FILE_VARIABLE, // name
            "label", "Source file", // label
            "class", Object.class, // class
            "level", "REQUIRED", // level
            "explanation", "FileVariable for the file to convert"),
        Map.of("name", OfficeToPdfInput.INPUT_DESTINATION_FILE_NAME,// name
            "label", "Destination file name", // label
            "class", String.class, // class
            "level", "REQUIRED",// level
            "explanation", "Destination file name"),
        Map.of("name", OfficeToPdfInput.INPUT_DESTINATION_STORAGEDEFINITION, // name
            "label", "Destination storage definition", // label
            "class", String.class, // class
            "defaultValue", StorageDefinition.StorageDefinitionType.JSON.toString(), // default value
            "level", "OPTIONAL", // level
            "explanation",
            "Storage Definition use to describe how to save the file. If not provided, the Source file's storage definition is used."));
  }

}
