package io.camunda.connector.officetopdf;

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
import io.camunda.connector.cherrytemplate.CherryConnector;
import io.camunda.filestorage.FileRepoFactory;
import io.camunda.filestorage.FileVariable;
import io.camunda.filestorage.FileVariableReference;
import io.camunda.filestorage.StorageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@OutboundConnector(name = "OfficeToPDF", inputVariables = { OfficeToPdfInput.INPUT_SOURCE_FILE_VARIABLE,
    OfficeToPdfInput.INPUT_DESTINATION_FILE_NAME,
    OfficeToPdfInput.INPUT_DESTINATION_STORAGEDEFINITION }, type = OfficeToPdfFunction.TYPE_PDF_CONVERT_TO)
public class OfficeToPdfFunction implements OutboundConnectorFunction, CherryConnector {
  /**
   * Different BPMN Errors this connector can throw
   */
  public static final String BPMERROR_CONVERSION_ERROR = "CONVERSION_ERROR";
  public static final String BPMERROR_LOAD_FILE_ERROR = "LOAD_FILE_ERROR";
  /**
   * Topic for this connector
   */
  public static final String TYPE_PDF_CONVERT_TO = "c-pdf-convert-to";
  private static final String WORKERLOGO = "data:image/svg+xml,%3C?xml version='1.0' encoding='UTF-8' standalone='no'?%3E%3Csvg   xmlns:dc='http://purl.org/dc/elements/1.1/'   xmlns:cc='http://creativecommons.org/ns%23'   xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns%23'   xmlns:svg='http://www.w3.org/2000/svg'   xmlns='http://www.w3.org/2000/svg'   viewBox='0 0 18 18'   version='1.1'   id='svg69'   width='18'   height='18'%3E  %3Cmetadata     id='metadata73'%3E    %3Crdf:RDF%3E      %3Ccc:Work         rdf:about=''%3E        %3Cdc:format%3Eimage/svg+xml%3C/dc:format%3E        %3Cdc:type           rdf:resource='http://purl.org/dc/dcmitype/StillImage' /%3E        %3Cdc:title%3E%3C/dc:title%3E      %3C/cc:Work%3E    %3C/rdf:RDF%3E  %3C/metadata%3E  %3Cdefs     id='defs43'%3E    %3Cstyle       id='style2'%3E          .cls-1%7B     isolation:isolate;    %7D    .cls-2%7B     opacity:0.2;    %7D    .cls-2,.cls-3,.cls-5%7B     mix-blend-mode:multiply;    %7D    .cls-4%7B     fill:%23fff;    %7D    .cls-5%7B     opacity:0.12;    %7D    .cls-6%7B     fill:url(%23linear-gradient);    %7D    .cls-7%7B     fill:url(%23linear-gradient-2);    %7D    .cls-8%7B     fill:url(%23linear-gradient-3);    %7D    .cls-9%7B     fill:url(%23linear-gradient-4);    %7D    .cls-10%7B     fill:url(%23linear-gradient-5);    %7D    .cls-11%7B     fill:none;    %7D  %3C/style%3E    %3ClinearGradient       id='linear-gradient'       x1='45.200001'       y1='-1.42'       x2='45.200001'       y2='57.799999'       gradientUnits='userSpaceOnUse'%3E      %3Cstop         offset='0'         stop-color='%23ffb900'         id='stop4' /%3E      %3Cstop         offset='0.17'         stop-color='%23ef8400'         id='stop6' /%3E      %3Cstop         offset='0.31'         stop-color='%23e25c01'         id='stop8' /%3E      %3Cstop         offset='0.43'         stop-color='%23db4401'         id='stop10' /%3E      %3Cstop         offset='0.5'         stop-color='%23d83b01'         id='stop12' /%3E    %3C/linearGradient%3E    %3ClinearGradient       id='linear-gradient-2'       x1='34.52'       y1='0.67000002'       x2='3.1600001'       y2='45.450001'       gradientUnits='userSpaceOnUse'%3E      %3Cstop         offset='0'         stop-color='%23800600'         id='stop15' /%3E      %3Cstop         offset='0.6'         stop-color='%23c72127'         id='stop17' /%3E      %3Cstop         offset='0.73'         stop-color='%23c13959'         id='stop19' /%3E      %3Cstop         offset='0.85'         stop-color='%23bc4b81'         id='stop21' /%3E      %3Cstop         offset='0.94'         stop-color='%23b95799'         id='stop23' /%3E      %3Cstop         offset='1'         stop-color='%23b85ba2'         id='stop25' /%3E    %3C/linearGradient%3E    %3ClinearGradient       id='linear-gradient-3'       x1='18.5'       y1='55.630001'       x2='59.439999'       y2='55.630001'       gradientUnits='userSpaceOnUse'%3E      %3Cstop         offset='0'         stop-color='%23f32b44'         id='stop28' /%3E      %3Cstop         offset='0.6'         stop-color='%23a4070a'         id='stop30' /%3E    %3C/linearGradient%3E    %3ClinearGradient       id='linear-gradient-4'       x1='35.16'       y1='-0.23999999'       x2='28.52'       y2='9.2399998'       gradientUnits='userSpaceOnUse'%3E      %3Cstop         offset='0'         stop-opacity='0.4'         id='stop33' /%3E      %3Cstop         offset='1'         stop-opacity='0'         id='stop35' /%3E    %3C/linearGradient%3E    %3ClinearGradient       id='linear-gradient-5'       x1='46.32'       y1='56.549999'       x2='27.99'       y2='54.950001'       gradientUnits='userSpaceOnUse'%3E      %3Cstop         offset='0'         stop-opacity='0.4'         id='stop38' /%3E      %3Cstop         offset='1'         stop-opacity='0'         id='stop40' /%3E    %3C/linearGradient%3E  %3C/defs%3E  %3Cg     class='cls-1'     id='g67'     transform='scale(0.28125)'%3E    %3Cg       id='Icons_-_Color'       data-name='Icons - Color'%3E      %3Cg         id='Desktop_-_Full_Bleed'         data-name='Desktop - Full Bleed'%3E        %3Cg           class='cls-2'           id='g47'%3E          %3Cpath             class='cls-4'             d='m 19.93,49 a 3.22,3.22 0 0 0 -1.59,6 L 29.7,61.44 A 6.2,6.2 0 0 0 32.77,62.25 6,6 0 0 0 34.48,62 L 51.57,57.13 A 6.12,6.12 0 0 0 56,51.26 V 49 Z'             id='path45' /%3E        %3C/g%3E        %3Cg           class='cls-5'           id='g51'%3E          %3Cpath             class='cls-4'             d='m 19.93,49 a 3.22,3.22 0 0 0 -1.59,6 L 29.7,61.44 A 6.2,6.2 0 0 0 32.77,62.25 6,6 0 0 0 34.48,62 L 51.57,57.13 A 6.12,6.12 0 0 0 56,51.26 V 49 Z'             id='path49' /%3E        %3C/g%3E        %3Cpath           class='cls-6'           d='M 34.41,2 39,12.5 V 49 L 34.48,62 51.57,57.13 A 6.12,6.12 0 0 0 56,51.26 V 12.74 A 6.11,6.11 0 0 0 51.56,6.86 Z'           id='path53'           style='fill:url(%23linear-gradient)' /%3E        %3Cpath           class='cls-7'           d='m 12.74,48.61 5,-2.7 A 4.36,4.36 0 0 0 20,42.08 V 22.43 a 4.37,4.37 0 0 1 2.87,-4.1 L 39,12.5 V 8.07 A 6.32,6.32 0 0 0 34.41,2 6.18,6.18 0 0 0 32.68,1.76 v 0 A 6.41,6.41 0 0 0 29.54,2.59 L 11.08,13.12 A 6.1,6.1 0 0 0 8,18.42 v 27.36 a 3.21,3.21 0 0 0 4.74,2.83 z'           id='path55'           style='fill:url(%23linear-gradient-2)' /%3E        %3Cpath           class='cls-8'           d='M 39,49 H 19.93 a 3.22,3.22 0 0 0 -1.59,6 l 11.36,6.44 a 6.2,6.2 0 0 0 3.07,0.81 v 0 A 6,6 0 0 0 34.48,62 6.22,6.22 0 0 0 39,56 Z'           id='path57'           style='fill:url(%23linear-gradient-3)' /%3E        %3Cpath           class='cls-9'           d='m 12.74,48.61 5,-2.7 A 4.36,4.36 0 0 0 20,42.08 V 22.43 a 4.37,4.37 0 0 1 2.87,-4.1 L 39,12.5 V 8.07 A 6.32,6.32 0 0 0 34.41,2 6.18,6.18 0 0 0 32.68,1.76 v 0 A 6.41,6.41 0 0 0 29.54,2.59 L 11.08,13.12 A 6.1,6.1 0 0 0 8,18.42 v 27.36 a 3.21,3.21 0 0 0 4.74,2.83 z'           id='path59'           style='fill:url(%23linear-gradient-4)' /%3E        %3Cpath           class='cls-10'           d='M 39,49 H 19.93 a 3.22,3.22 0 0 0 -1.59,6 l 11.36,6.44 a 6.2,6.2 0 0 0 3.07,0.81 v 0 A 6,6 0 0 0 34.48,62 6.22,6.22 0 0 0 39,56 Z'           id='path61'           style='fill:url(%23linear-gradient-5)' /%3E        %3Crect           class='cls-11'           width='64'           height='64'           id='rect63'           x='0'           y='0' /%3E      %3C/g%3E    %3C/g%3E  %3C/g%3E  %3Cg     id='g117'     transform='matrix(0.23089663,0,0,0.23089663,6.2055391,7.0903316)'%3E    %3Cpath       d='m 16.7328,13.29858 c -0.1476,1.8864 -1.6524,2.7918 -1.9584,2.9574 -1.629,0.8892 -4.3704,0.5796 -5.6952001,-1.2852 -0.7308,-1.026 -1.098,-2.6586 -0.387,-3.789 l 0.009,-0.018 c 0.2556,-0.4158 0.8478,-1.1178 1.9404001,-1.1412 h 0.0396 c 0.3906,0 0.8406,0.1188 1.2384,0.2232 0.279,0.0738 0.5202,0.1368 0.7002,0.1494 0.1134,0.0072 0.2754,-0.0216 0.4806,-0.0576 0.6138,-0.108 1.638,-0.288 2.5344,0.3456 1.2204,0.865801 1.1052,2.5452 1.098,2.6154 z'       id='path89'       style='fill:%23aa0000;fill-opacity:1' /%3E    %3Cpath       d='m 9.2718,10.287181 c -0.351,0.2484 -0.5832,0.5562 -0.7236,0.783 l -0.009,0.0144 c -0.3636001,0.5796 -0.4644,1.2528 -0.3960001,1.9116 -0.0072,0.0036 -0.0144,0.0072 -0.0198,0.0126 -1.3338001,1.089 -2.8404,0.8082 -3.4254003,0.6372 l -0.0342,-0.009 c -1.8630002,-0.4644 -3.70259996,-2.4768 -3.3642,-4.6206 0.1872,-1.1808001 1.08,-2.538 2.4264,-2.8476 0.2322,-0.054 1.4454,-0.279 2.421,0.5382 0.2988,0.252 0.5202,0.6192001 0.7146,0.9432 0.1386,0.234 0.2592,0.4338 0.3906001,0.5652 0.0846,0.0846 0.2322,0.1674 0.4194,0.2736 0.549,0.3078001 1.3770001,0.7740001 1.5930002,1.7784 0.0018,0.0072 0.0036,0.0144 0.0072,0.0198 z'       id='path91'       style='fill:%23aa0000;fill-opacity:1' /%3E    %3Cpath       d='m 12.9294,10.18638 c -0.1062,0.018 -0.1998,0.0306 -0.2664,0.0306 -0.0108,0 -0.0216,-0.0018 -0.0306,-0.0018 -0.0504,-0.0036 -0.1098,-0.0108 -0.1728,-0.0234 0.5526,-1.4112 0.7812,-3.6684 -1.1394,-6.5268 -0.0036,-0.0072 -0.009,-0.0126 -0.0162,-0.018 0.1116,-0.4428 0.1494,-0.7524 0.1584,-0.828 0.0054,-0.0504 -0.0306,-0.0936 -0.0792,-0.099 -0.0504,-0.0072 -0.0936,0.0306 -0.099,0.0792 -0.0216,0.1926 -0.2376,1.9296001 -1.4490001,3.4722004 -0.7578001,0.9648001 -1.9512001,1.6830001 -2.3634002,1.8882 -0.0378,-0.0252 -0.0702,-0.0504 -0.0918,-0.072 -0.0594,-0.0594 -0.117,-0.1368 -0.1764,-0.2286 0.0342,-0.018 0.0702,-0.0378 0.108,-0.0576 0.4302,-0.2304 1.1502,-0.6138001 2.0286002,-1.5336001 1.0386001,-1.0872 1.2366001,-2.7522001 0.9450001,-3.2058 -0.1404,-0.2196 -0.3654001,-0.2412 -0.5454001,-0.2574 -0.2358,-0.0198 -0.378,-0.0342 -0.4032,-0.3546 -0.0234,-0.279 0.108,-0.5256 0.369,-0.6912 0.3510001,-0.225 0.9792001,-0.297 1.5876001,0.0486 0.3798,0.2142 0.3762,0.5796 0.3744,0.9324 -0.0018,0.2358 -0.0036,0.459 0.1134,0.6192 l 0.0504,0.0684 c 0.288,0.3906001 0.9612,1.3014001 1.3644,2.8908 C 13.626,8.01918 13.2192,9.56898 12.9294,10.18638 Z'       id='path93'       style='fill:%23502d16;fill-opacity:1' /%3E    %3Cpath       d='m 7.6338,2.8045802 c -0.0144,0.0072 -0.0288,0.0108 -0.0432,0.0108 -0.0324,0 -0.063,-0.018 -0.0792,-0.0468 -0.0018,-0.0018 -0.1278,-0.2268 -0.3546,-0.4338 -0.0378,-0.0324 -0.0396,-0.09 -0.0072,-0.126 0.0342,-0.0378 0.09,-0.0396 0.1278,-0.0072 0.2502,0.2268 0.3852,0.4698 0.3906,0.4806 0.025201,0.0432 0.009,0.0972 -0.0342,0.1224 z'       id='path95' /%3E    %3Cpath       d='m 7.8156,4.51998 c -0.1872,0.2214 -0.2844,0.261 -0.432,0.3204 l -0.0468,0.0198 c -0.0108,0.0054 -0.0234,0.0072 -0.0342,0.0072 -0.036,0 -0.0684,-0.0216 -0.0828,-0.0558 -0.0198,-0.045 0.0018,-0.099 0.0486,-0.117 l 0.0468,-0.0198 c 0.135,-0.0558 0.2034,-0.0846 0.3636,-0.27 0.0324,-0.0396 0.09,-0.0432 0.1278,-0.0108 0.0378,0.0324 0.0414,0.0882 0.009,0.126 z'       id='path97' /%3E    %3Cpath       d='m 10.134,3.1591802 c -0.0468,-0.0738 -0.108,-0.1134 -0.1764,-0.135 -0.2394,0.3222 -1.0818001,0.81 -1.9278,0.81 -0.225,0 -0.4518,-0.0342 -0.666,-0.1152 -0.576,-0.2196 -0.9702,-0.5256 -1.3518001,-0.8208 -0.3024,-0.2358 -0.5886,-0.4572 -0.9378,-0.6084001 -0.0468,-0.0216 -0.0666,-0.0738 -0.0468,-0.1188 0.0198,-0.0468 0.072,-0.0666 0.1188,-0.0468 0.369,0.162 0.6642,0.3888001 0.9756,0.6318 0.3708,0.2880001 0.756,0.5850001 1.3050001,0.7938 0.5598,0.2124 1.1880001,0.0738 1.6650001,-0.1404 -0.2052,-0.6336 -1.5318002,-2.56139997 -3.7188,-2.0700002 -0.3384,0.0756 -0.6336,0.1458 -0.8928,0.207 -1.098,0.2574 -1.5678,0.3672001 -2.0556002,0.2376 0.045,0.2268001 0.1638,0.4194 0.3528,0.5742001 0.378,0.3042 0.927,0.3798 1.1880001,0.3834 C 3.8322,2.59038 3.69,2.49318 3.5154,2.43198 3.4686,2.41578 3.4434,2.36538 3.4596,2.31858 c 0.0162,-0.0486 0.0684,-0.072 0.1152,-0.0558 0.567,0.1998 0.828,0.7074 1.2492001,1.7424002 0.441,1.0836 1.9836001,2.0934 3.2346,1.7190001 1.2744,-0.3816 1.2528001,-1.7244 1.251,-1.7388 0,-0.036 0.0216,-0.0702 0.054,-0.0846 0.0846,-0.0378 0.5184,-0.2358 0.8243999,-0.5238 0.0054,-0.0054 0.0108,-0.009 0.0162,-0.009 v -0.0018 C 10.1898,3.27798 10.1646,3.20778 10.134,3.1591802 M 7.1496005,2.20878 c 0.0342,-0.0378 0.09,-0.0396 0.1278,-0.0072 0.2502,0.2268 0.3852,0.4698 0.3906,0.4806 0.0252,0.0432 0.009,0.0972 -0.0342,0.1224 -0.0144,0.0072 -0.0288,0.0108 -0.0432,0.0108 -0.0324,0 -0.063,-0.018 -0.0792,-0.0468 -0.0018,-0.0018 -0.1278,-0.2268 -0.3546,-0.4338 -0.037801,-0.0324 -0.0396,-0.09 -0.0072,-0.126 m -1.5156002,1.2258 c -0.0036,0 -0.0558,0.009 -0.1584,0.009 -0.081,0 -0.1908,-0.0054 -0.3312,-0.0234 -0.0504,-0.0054 -0.0846,-0.0504 -0.0774,-0.1008 0.0054,-0.0486 0.0504,-0.0828 0.1008,-0.0774 0.2916,0.0378 0.4338,0.0162 0.4356,0.0162 0.0486,-0.009 0.0954,0.0234 0.1026,0.0738 0.009,0.0486 -0.0234,0.0936 -0.072,0.1026 M 7.8156,4.51998 c -0.1872,0.2214 -0.2844,0.261 -0.432,0.3204 l -0.0468,0.0198 c -0.0108,0.0054 -0.0234,0.0072 -0.0342,0.0072 -0.036,0 -0.0684,-0.0216 -0.0828,-0.0558 -0.0198,-0.045 0.0018,-0.099 0.0486,-0.117 l 0.0468,-0.0198 c 0.135,-0.0558 0.2034,-0.0846 0.3636,-0.27 0.0324,-0.0396 0.09,-0.0432 0.1278,-0.0108 0.0378,0.0324 0.0414,0.0882 0.009,0.126 z'       id='path99'       style='fill:%23008000;fill-opacity:1' /%3E    %3Cpath       d='m 5.7060003,3.33198 c 0.009,0.0486 -0.0234,0.0936 -0.072,0.1026 -0.0036,0 -0.0558,0.009 -0.1584,0.009 -0.081,0 -0.1908,-0.0054 -0.3312,-0.0234 -0.0504,-0.0054 -0.0846,-0.0504 -0.0774,-0.1008 0.0054,-0.0486 0.0504,-0.0828 0.1008,-0.0774 0.2916,0.0378 0.4338,0.0162 0.4356,0.0162 0.0486,-0.009 0.0954,0.0234 0.1026,0.0738 z'       id='path101' /%3E  %3C/g%3E%3C/svg%3E";
  Logger logger = LoggerFactory.getLogger(OfficeToPdfFunction.class.getName());

  @Override
  public OfficeToPdfOutput execute(OutboundConnectorContext context) throws ConnectorException {
    logger.info("OfficeToPdf.start processId[{}]", context.getJobContext().getBpmnProcessId());

    OfficeToPdfInput officeToPdfInput;
    try {
      officeToPdfInput = context.bindVariables(OfficeToPdfInput.class);
    } catch (Exception e) {
      logger.error("OfficeToPdf.bindVariable ", e);
      throw new ConnectorException(BPMERROR_LOAD_FILE_ERROR, "Connector [OfficeToPdf] cannot bind variables " + e);

    }
    logger.debug("OfficeToPdf.execute");

    if (officeToPdfInput.sourceFileVariable == null) {
      throw new ConnectorException(BPMERROR_LOAD_FILE_ERROR,
          "Connector [OfficeToPdf] cannot read file[" + OfficeToPdfInput.INPUT_SOURCE_FILE_VARIABLE + "]");
    }
    FileRepoFactory fileVariableFactory = FileRepoFactory.getInstance();

    FileVariableReference docSourceReference = null;
    try {
      docSourceReference = FileVariableReference.fromJson(officeToPdfInput.getSourceFileVariable());
      logger.info("OfficeToPdf.fromJson(): {} loadFile[{}]",
          officeToPdfInput.getSourceFileVariable().getClass().getName(),
          FileVariableReference.getInformation(docSourceReference));

      // Load the source document
      FileVariable docSource;
      try {
        docSource = fileVariableFactory.loadFileVariable(docSourceReference);
      } catch (Exception e) {
        throw new ConnectorException(BPMERROR_LOAD_FILE_ERROR,
            "Connector [OfficeToPdf] cannot load file [" + FileVariableReference.getInformation(docSourceReference)
                + "] : " + e);
      }
      // build the report
      final IXDocReport report;

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(docSource.getValue());
      report = XDocReportRegistry.getRegistry().loadReport(byteArrayInputStream, TemplateEngineKind.Velocity);

      final IContext iContext = report.createContext();
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      report.convert(iContext, Options.getTo(ConverterTypeTo.PDF), out);

      // save the result in a File Variable - use the destination StorageDefinition or use the source one if nothing is provided
      StorageDefinition storageDefinitionDestination = null;
      if (officeToPdfInput.getDestinationStorageDefinition() == null) {
        storageDefinitionDestination = docSource.getStorageDefinition();
      } else {
        storageDefinitionDestination = StorageDefinition.getFromString(
            officeToPdfInput.getDestinationStorageDefinition());
      }
      logger.debug("OfficeToPdf.saveToStorage[{}]", storageDefinitionDestination.getInformation());

      FileVariable docPdf = fileVariableFactory.createFileVariable(storageDefinitionDestination);
      docPdf.setValue(out.toByteArray());
      docPdf.setName(officeToPdfInput.getDestinationFileName());
      FileVariableReference docPdfReference = fileVariableFactory.saveFileVariable(docPdf);

      // register the result in the Output variable
      OfficeToPdfOutput officeToPdfOutput = new OfficeToPdfOutput();
      officeToPdfOutput.pdfFileVariable = docPdfReference.toJson();
      logger.info("OfficeToPdf.finish result[{}]", FileVariableReference.getInformation(docPdfReference));

      return officeToPdfOutput;
    } catch (ConnectorException c) {
      throw c;
    } catch (Exception e) {
      logger.error("OfficeToPdf: Conversion error file [{}] : {}",
          (docSourceReference == null ? "" : FileVariableReference.getInformation(docSourceReference)), e);

      throw new ConnectorException(BPMERROR_CONVERSION_ERROR,
          "Worker [OfficeToPdf] cannot convert file[" + officeToPdfInput.getDestinationFileName() + "] : " + e);
    }
  }

  /**
   * return a description of the connector
   *
   * @return the description
   */
  public String getDescription() {
    return "A PDF document is generated for an office (Word, OpenOffice) document";
  }

  /**
   * Return the logo
   *
   * @return the log (AVG string)
   */
  public String getLogo() {
    return WORKERLOGO;
  }

  /**
   * Return the collection name of the connector
   *
   * @return the collection name
   */
  public String getCollectionName() {
    return "Pdf";
  }

  /**
   * on which BPMN item this event can apply. Return a list like
   * "bpmn:Task",
   * "bpmn:IntermediateThrowEvent",
   * "bpmn:IntermediateCatchEvent"
   *
   * @return list of bpmn item
   */
  public List<String> appliesTo() {
    return List.of("bpmn:Task");
  }

  /**
   * return the potential error
   *
   * @return all BPMN errors
   */
  public Map<String, String> getListBpmnErrors() {
    return Map.of(OfficeToPdfFunction.BPMERROR_CONVERSION_ERROR, "Conversion error",
        OfficeToPdfFunction.BPMERROR_LOAD_FILE_ERROR, "Load File error");
  }

  public Class getInputParameterClass() {
    return OfficeToPdfInput.class;
  }

  public Class getOutputParameterClass() {
    return OfficeToPdfOutput.class;
  }

}
