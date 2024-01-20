package io.camunda.connector.cherrytemplate;


/* ******************************************************************** */
/*                                                                      */
/*  CherryConnector                                                     */
/*                                                                      */
/*  This interface is not required by Cherry, but it is the method      */
/*  searched by the framework to collect additional information on      */
/*  the connector                                                       */
/* ******************************************************************** */

import java.util.Map;

public interface CherryConnector {

  /**
   * return a description of the connector
   * @return the description
   */
  String getDescription();

  /**
   * Reuturn the logo
   * @return the log (AVG string)
   */
  String getLogo();

  /**
   * Return the collection name of the connector
   * @return the collection name
   */
  String getCollectionName();


  Map<String,String> getBpmnErrors();
}
