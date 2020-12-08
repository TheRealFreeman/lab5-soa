package soa.eip;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

  public static final String DIRECT_URI = "direct:twitter";

  @Override
  public void configure() {
    from(DIRECT_URI).process(exchange -> {
      String body = exchange.getIn().getBody(String.class);
      String[] splitBody = body.split("max:");
      String newBody = splitBody[0];
      if (splitBody.length > 1) {
        newBody += "?count=" + splitBody[1];
      }
      exchange.getOut().setBody(newBody);
    }).log("Body contains \"${body}\"").log("Searching twitter for \"${body}\"!").toD("twitter-search:${body}")
        .log("Body now contains the response from twitter:\n${body}");
  }
}
