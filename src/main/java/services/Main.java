package services;

import ratpack.handling.Handlers;
import ratpack.http.HttpUrlBuilder;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class Main {
  public static void main(String... args) throws Exception {
    RatpackServer.start(s -> s
        .serverConfig(c -> c.baseDir(BaseDir.find()))
        .handlers(c -> c
            .prefix("::distributions|distributions-snapshots", d -> d
                .all(Handlers.get())
                .all(ctx -> {

                  String location = HttpUrlBuilder.https()
                    .host("downloads.gradle.org")
                    .path(ctx.getRequest().getPath())
                    .build()
                    .toString();
                  ctx.redirect(301, location);
                })
            )
            .get("versions", ctx -> {
              ctx.render(ctx.file("versions.json"));
            })
        )
    );
  }
}
