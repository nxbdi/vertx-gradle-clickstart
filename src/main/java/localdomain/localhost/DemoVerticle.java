/*
 * Copyright 2010-2013, the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package localdomain.localhost;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class DemoVerticle extends Verticle {

    public void start() {

        final int port = Integer.valueOf(System.getProperty("app.port", "8080"));

        container.logger().info("Start " + getClass().getName() + " on " + port);

        final HttpServer server = vertx.createHttpServer();
        server.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest event) {
                event.response().putHeader("Content-Type", "text/html");
                event.response().end("<html>" +
                        "<head><title>Vertx ClickStart</title></head>" +
                        "<body>" +
                        "<h1>Vertx ClickStart</h1></body>" +
                        "<p>Fork me <a href='https://github.com/CloudBees-community/vertx-gradle-clickstart'>here</a></p>" +
                        "</html>");
            }
        }).listen(port);

    }
}
