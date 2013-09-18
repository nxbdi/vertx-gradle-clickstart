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

import com.bloidonia.vertx.mods.JsonUtils;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class DemoVerticle extends Verticle {

    public void start() {

        final int port = Integer.valueOf(System.getProperty("app.port", "8080"));

        container.logger().info("Start " + getClass().getName() + " on " + port);

        final EventBus eventBus = vertx.eventBus();

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

        final JsonObject persistorConfig;// = container.config().getObject("db_mydb");
        persistorConfig = new JsonObject() {{
            putString("address", "com.bloidonia.jdbcpersistor");
            putString("driver", "com.mysql.jdbc.Driver");
            putString("url", "jdbc:" + System.getProperty("DATABASE_URL_MYDB"));
            putString("username", System.getProperty("DATABASE_USERNAME_MYDB"));
            putString("password", System.getProperty("DATABASE_PASSWORD_MYDB"));
        }};


        final String persistorAddress = persistorConfig.getString("address");

        container.logger().info("Connect to DB " + persistorConfig.getString("url") + " - " + persistorConfig.getString("username"));

        container.logger().info("deploy module com.bloidonia~mod-jdbc-persistor~2.1 as a WorkerVerticle");
        container.deployWorkerVerticle("com.bloidonia.vertx.mods.JdbcProcessor", persistorConfig, 1, true, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {

                if (event.failed()) {
                    container.logger().warn("Failure to initialize JDBC Processor");
                } else {
                    String result = event.result();
                    container.logger().info("JDBC Processor initialized: " + result);

                    JsonObject select1Msg = new

                            JsonObject() {{
                                putString("action", "select");
                                putString("stmt", "select 1");
                            }};


                    container.logger().info("send select 1");
                    eventBus.send(persistorAddress, select1Msg, new Handler<Message<JsonObject>>() {
                        @Override
                        public void handle(Message<JsonObject> event) {
                            container.logger().info("result of select 1: " + event.body().toString());

                        }
                    });
                }
            }
        });

    }
}
