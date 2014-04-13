@Grab('org.apache.camel:camel-core:2.13.0')
@Grab('org.slf4j:slf4j-simple:1.7.6')

import org.apache.camel.*
import org.apache.camel.impl.*
import org.apache.camel.builder.*

def context = new DefaultCamelContext()

context.addRoutes(new RouteBuilder() {
  public void configure() {
    from("file:data/inbox?noop=true")
      .to("log://camelLogger?level=INFO")
      .to("file:data/outbox")
  }
})

context.start()
Thread.sleep(60000)
context.stop()
