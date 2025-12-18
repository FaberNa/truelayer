package org.catapano.truelayer.helper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TranslationStubs {

    private final WireMockExtension wm;
    public TranslationStubs(WireMockExtension wm) {
        this.wm = wm;
    }


    /** 200 OK con payload “success/contents” (compatibile col tuo TranslationResponse.contents). */
    public void stubTranslateOk(String translated, String text, String translation) {
        wm.stubFor(post(urlEqualTo("/translate/" + translation+".json"))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withRequestBody(containing("text="))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(okBody(translated, text, translation))));
    }

    /** 429 Too Many Requests with payload “error”. */
    public void stubRateLimit429(String type) {
        wm.stubFor(post(urlEqualTo("/translate/" + type + ".json"))
                .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
                .withRequestBody(containing("text="))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Content-Type", "application/json")
                        .withBody(rateLimitBody())));
    }

    public void verifyTranslateCalled() {
        wm.verify(postRequestedFor(urlEqualTo("/translate")));
    }

    private static String okBody(String translated, String text, String translation) {
        return """
      {
        "success": { "total": 1 },
        "contents": {
          "translated": "%s",
          "text": "%s",
          "translation": "%s"
        }
      }
      """.formatted(escapeJson(translated), escapeJson(text), escapeJson(translation));
    }

    private static String rateLimitBody() {
        return """
      {
        "error": {
          "code": %d,
          "message": "%s"
        }
      }
      """.formatted(429, "Retry later");
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\t", "\\t");
    }

}
