package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.web.HttpRequestHelper;
import edu.pdx.cs.joy.web.HttpRequestHelper.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test provided by the createRESTProject script
 */
class IndexDotHtmlIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  /**
   * Checks that html file exists
   */
  @Test
  void indexDotHtmlExists() throws IOException {
    Response indexDotHtml = fetchIndexDotHtml();
    assertThat(indexDotHtml.getHttpStatusCode(), equalTo(200));
  }

  /**
   * Checks that html file has reasonable content
   */
  @Test
  void indexDotHtmlHasReasonableContent() throws IOException {
    Response indexDotHtml = fetchIndexDotHtml();
    assertThat(indexDotHtml.getContent(), containsString("<form"));
  }

  /**
   * Checks that html file can perform a fetch
   */
  private Response fetchIndexDotHtml() throws IOException {
    int port = Integer.parseInt(PORT);
    return new IndexDotHtmlHelper(HOSTNAME, port).getIndexDotHtml();
  }

  /**
   * Helper class for the integration tests
   */
  static class IndexDotHtmlHelper {
    private static final String WEB_APP = "airline";
    private final HttpRequestHelper http;

    IndexDotHtmlHelper(String hostName, int port) {
      this.http = new HttpRequestHelper(String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, "index.html" ));
    }

    Response getIndexDotHtml() throws IOException {
      return http.get(Map.of());
    }
  }
}
