package io.camunda.connector.officetopdf;

import io.camunda.connector.cherrytemplate.CherryOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OfficeToPdfOutput implements CherryOutput {

  public static final String OUTPUT_PDF_FILE_VARIABLE = "pdfFileVariable";
  String pdfFileVariable;

  /**
   * Attention, the first letter after the get must be in lower case
   *
   * @return the variable
   */
  public String getpdfFileVariable() {
    return pdfFileVariable;
  }

  /**
   * this method is exploded by Cherry Runtime to produce a nice element-template
   *
   * @return list of parameters
   */
  public List<Map<String, Object>> getOutputParameters() {
    return List.of(Map.of("name", OfficeToPdfOutput.OUTPUT_PDF_FILE_VARIABLE, // name
        "label", "Destination file", // label
        "class", Object.class, // class
        "level", "REQUIRED", // level
        "explanation", "FileVariable converted (a File Variable Reference)"));
  }

}
