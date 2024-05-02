package io.camunda.connector.cherrytemplate;

import java.util.List;
import java.util.Map;

public interface CherryInput {

  /** These constant map PARAMETER_NAME_xxx constant in SDKRunnerCherryConnector */

  static final String PARAMETER_MAP_NAME = "name";
  static final String PARAMETER_MAP_LABEL = "label";
  static final String PARAMETER_MAP_CLASS = "class";
  static final String PARAMETER_MAP_LEVEL = "level";
  static final String PARAMETER_MAP_LEVEL_REQUIRED = "REQUIRED";
  static final String PARAMETER_MAP_LEVEL_OPTIONAL = "OPTIONAL";
  static final String PARAMETER_MAP_EXPLANATION = "explanation";
  static final String PARAMETER_MAP_DEFAULT_VALUE = "defaultValue";
  static final String PARAMETER_MAP_CONDITION_PROPERTY = "conditionProperty";
  static final String PARAMETER_MAP_GSON_TEMPLATE = "gsonTemplate";
  static final String PARAMETER_MAP_CONDITION_ONE_OF = "conditionOneOf";
  static final String PARAMETER_MAP_CHOICE_LIST = "choiceList";
  static final String PARAMETER_MAP_CHOICE_LIST_CODE = "code";
  static final String PARAMETER_MAP_CHOICE_LIST_DISPLAY_NAME = "displayName";
  static final String PARAMETER_MAP_VISIBLE_IN_TEMPLATE = "visibleInTemplate";

  /**
   * get the list of Input Parameters
   *
   * @return list of Map. Map contains key "name", "label", "defaultValue", "class", "level", "explanation"
   */
  List<Map<String, Object>> getInputParameters();

}
