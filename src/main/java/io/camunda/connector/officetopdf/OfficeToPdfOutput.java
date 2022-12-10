package io.camunda.connector.officetopdf;

public class OfficeToPdfOutput {

  public static final String OUTPUT_PDF_FILE_VARIABLE = "pdfFileVariable";
  String pdfFileVariable;

  /**
   * Attention, the first letter after the get must be in lower case
   * @return the variable
   */
  public String getpdfFileVariable() {
    return pdfFileVariable;
  }
}
