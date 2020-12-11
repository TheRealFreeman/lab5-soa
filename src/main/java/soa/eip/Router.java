package soa.eip;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

  public static final String DIRECT_URI = "direct:twitter";

  private static void process(Exchange exchange) {
    // Get current body
    String body = exchange.getIn().getBody(String.class);
    // Splits string by the operator "max:""
    String[] splitBody = body.split("max:");
    String newBody = splitBody[0];
    if (splitBody.length > 1) {
      // Limit number of tweets to the chosen amount
      newBody += "?count=" + splitBody[1];
    }
    // Sets updated body
    exchange.getIn().setBody(newBody);
  }

  @Override
  public void configure() {
    from(DIRECT_URI).process(Router::process).log("Body contains \"${body}\"").log("Searching twitter for \"${body}\"!").toD("twitter-search:${body}")
            .log("Body now contains the response from twitter:\n${body}");

    from("websocket://localhost:50000/twitterWS").process(Router::process).log("Searching twitter for \"${body}\"!").toD("twitter-search:${body}")
        .to("websocket://localhost:50000/twitterWS");

  }
}