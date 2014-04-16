package example

import org.apache.camel.*
import org.apache.camel.impl.*
import org.apache.camel.builder.*

public class JdkTimer {

  public static void main(String... args) {
    def context = new DefaultCamelContext()

    context.addRoutes(new RouteBuilder() {
      public void configure() {
        from("timer://jdkTimer?period=3000")
          .to("log://camelLogger?level=INFO")
      }
    })

    context.start()

    addShutdownHook{ context.stop() }
    synchronized(this){ this.wait() }
  }
}
