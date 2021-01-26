package fcu.selab.progedu.jenkinsconfig;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.transform.Transformer;

public class MavenConfig extends JenkinsConfig {

  URL baseUrl = this.getClass().getResource("/jenkins/config_maven.xml");
  Path basePath;
  File baseFile;
  Document xmlDocument;

  /**
   * init sample xml
   */
  public MavenConfig(String projectUrl, String updateDbUrl, String username, String projectName) throws Exception {

    this.basePath = Paths.get(this.baseUrl.toURI());
    this.baseFile = basePath.toFile();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    xmlDocument = builder.parse(this.baseFile);

    setAll(projectUrl, updateDbUrl, username, projectName);

  }

  @Override
  public String getXmlConfig() {

    String result = null;

    if (this.xmlDocument != null) {
      StringWriter strWtr = new StringWriter();
      StreamResult strResult = new StreamResult(strWtr);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      try {
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
        // text
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(this.xmlDocument.getDocumentElement()),
                strResult);

      } catch (Exception e) {
        System.err.println("XML.toString(Document): " + e);
      }


      result = strResult.getWriter().toString();


      try {
        strWtr.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    return result;
  }

  @Override
  public Document getXmlDocument() {
    return this.xmlDocument;
  }

  @Override
  public void setXmlDocument(Document document) {
    this.xmlDocument = document;
  }

  private void setAll(String projectUrl, String updateDbUrl, String username, String projectName) {
    setGitLabProjectUrl(projectUrl);
    setProgEduUpdateUrl(updateDbUrl);
    setProgEduUpdateUsername(username);
    setProgEduUpdateProjectName(projectName);
  }

  private void setGitLabProjectUrl(String projectUrl) {
    this.xmlDocument.getElementsByTagName("url").item(0).setTextContent(projectUrl);
  }

  private void setProgEduUpdateUrl(String updateDbUrl) {
    this.xmlDocument.getElementsByTagName("progeduDbUrl").item(0).setTextContent(updateDbUrl);
  }

  private void setProgEduUpdateUsername(String username) {
    this.xmlDocument.getElementsByTagName("user").item(0).setTextContent(username);
  }

  private void setProgEduUpdateProjectName(String projectName) {
    this.xmlDocument.getElementsByTagName("proName").item(0).setTextContent(projectName);
  }

}
