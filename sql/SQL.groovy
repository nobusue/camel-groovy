@Grab('org.apache.camel:camel-core:2.13.0')
@Grab('org.apache.camel:camel-jdbc:2.13.0')
@Grab('org.apache.camel:camel-sql:2.13.0')
@Grab('org.slf4j:slf4j-simple:1.7.6')
@Grab('org.apache.derby:derby:10.10.1.1')

import org.apache.camel.*
import org.apache.camel.impl.*
import org.apache.camel.builder.*
import org.apache.camel.util.jndi.*
import org.springframework.jdbc.datasource.embedded.*

def db = new EmbeddedDatabaseBuilder()
         .setType(EmbeddedDatabaseType.DERBY)
         .addScript("create.sql")
         .build()

def reg = new SimpleRegistry()
reg.put("myDS", db)

def context = new DefaultCamelContext(reg)

context.addRoutes(new RouteBuilder() {
  public void configure() {
    from("timer://jdkTimer?period=1000")
      .setBody(constant("select * from projects"))
      .to("jdbc:myDS")
      .process(new Processor() {
        def void process(Exchange exchange) {
          def body = exchange.getIn().getBody()
          def newBody = []
          body.each{ newBody << "${it.toString()}" }
          exchange.getIn().setBody(newBody.join("\n"))
        }
     }).to("file:dump")
  }
})

context.start()
Thread.sleep(1*1500)
context.stop()
