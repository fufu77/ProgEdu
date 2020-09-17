package fcu.selab.progedu.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fcu.selab.progedu.data.FeedBack;

import fcu.selab.progedu.utils.ExceptionUtil;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenCompileFailureOfUnitTest implements Status {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MavenCompileFailureOfUnitTest.class);

  @Override
  public String extractFailureMsg(String consoleText) {
    try {
      String unitTest = "";
      String startString = "[ERROR] COMPILATION ERROR : ";
      String endString = "[INFO] BUILD FAILURE";

      unitTest = consoleText.substring(consoleText.indexOf(startString),
          consoleText.indexOf(endString, consoleText.indexOf(startString)));

      return unitTest.replace("/var/jenkins_home/workspace/", "").trim();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "ExtractFailureMsg Method Error";
    }
  }

  @Override
  public ArrayList<FeedBack> formatExamineMsg(String consoleText) {
    ArrayList<FeedBack> feedbackList = new ArrayList<>();
    String suggest = "https://www.learnjavaonline.org/";
    try {
      Pattern pattern = Pattern.compile("(\\[ERROR\\])(.*?)(:)"
          + "(\\[([\\d]{0,4},[\\d]{0,4})\\])(.*?)(\n)");
      Matcher matcher = pattern.matcher(consoleText);
      while (matcher.find()) {
        String fileName = matcher.group(2).substring(matcher.group(2).indexOf("_") + 1).trim();
        String line = matcher.group(5).replaceAll(",", ":").trim();
        String symptom = matcher.group(6).trim();
        feedbackList.add(new FeedBack(
            StatusEnum.COMPILE_ERROR_OF_UNIT_TEST, fileName, line, "", symptom, suggest));
      }
      if (feedbackList.isEmpty()) {
        feedbackList.add(
            new FeedBack(StatusEnum.COMPILE_ERROR_OF_UNIT_TEST,
                "Please notify teacher or assistant this situation, thank you!", ""));
      }
    } catch (Exception e) {
      feedbackList.add(
          new FeedBack(StatusEnum.COMPILE_ERROR_OF_UNIT_TEST,
              "CompileFailure ArrayList error", e.getMessage()));
    }
    return feedbackList;
  }
}