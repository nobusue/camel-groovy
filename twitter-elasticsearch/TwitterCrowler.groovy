@Grab('org.apache.camel:camel-core:2.13.0')
@Grab('org.apache.camel:camel-twitter:2.13.0')
@Grab('org.apache.camel:camel-elasticsearch:2.13.0')
@Grab('org.apache.camel:camel-jackson:2.13.0')
@Grab('org.slf4j:slf4j-simple:1.7.6')

import org.apache.camel.*
import org.apache.camel.impl.*
import org.apache.camel.builder.*
import org.apache.camel.model.dataformat.JsonLibrary

def context = new DefaultCamelContext()

context.addRoutes(new RouteBuilder() {
  public void configure() {
    from("twitter://search?type=direct&keywords=camel"+
         "&consumerKey=xxxx"+
         "&consumerSecret=xxxx"+
         "&accessToken=xxxx"+
         "&accessTokenSecret=xxxx")
      .marshal().json(JsonLibrary.Jackson)
      .to("elasticsearch://elasticsearch?operation=INDEX"+
          "&indexName=twitter&indexType=tweet")
  }
})

context.start()
Thread.sleep(10 * 1000)
context.stop()
