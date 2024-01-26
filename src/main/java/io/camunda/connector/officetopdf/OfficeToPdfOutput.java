package io.camunda.connector.officetopdf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OfficeToPdfOutput {

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
   * @return list of parameter
   */
  public List<Map<String, Object>> getOutputParameters() {
    return Arrays.asList(Map.of("name", OfficeToPdfOutput.OUTPUT_PDF_FILE_VARIABLE, // name
        "label", "Destination file", // label
        "class", Object.class, // class
        "level", "REQUIRED", // level
        "explanation", "FileVariable converted (a File Variable Reference)"));
  }

}
