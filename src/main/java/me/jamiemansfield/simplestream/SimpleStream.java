/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.simplestream;

import io.javalin.Javalin;
import io.javalin.embeddedserver.Location;
import me.jamiemansfield.simplestream.config.SsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class SimpleStream {

    public static final Logger log = LoggerFactory.getLogger("SimpleStream");

    private static final Path CONFIG_PATH = Paths.get("config.xml");

    public static void main(final String[] args) {
        // Lets keep the user informed of whats going on.
        log.info("Starting SimpleStream...");

        // Initialise 'config.xml' with default options, if not exist.
        if (Files.notExists(CONFIG_PATH)) {
            try (final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(CONFIG_PATH.toFile()))) {
                SsConfig.DEFAULT_CONFIG.serialise(osw);
            }
            catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Load config from file.
        final SsConfig config;
        try (final InputStreamReader isr = new InputStreamReader(new FileInputStream(CONFIG_PATH.toFile()))) {
            config = SsConfig.deserialise(isr);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

        // Start webserver.
        final Javalin app = Javalin.create()
                .port(config.getPort())
                .enableStaticFiles("movies", Location.EXTERNAL)
                .start();
        app.get("/", ctx -> ctx.html(views.Home.template(config.getMovies())
                .render()
                .toString()));
        app.get("/:movie", ctx -> {
            final String movieId = ctx.param("movie");
            config.getMovies().stream()
                    .filter(stream -> Objects.equals(stream.getId(), movieId))
                    .findFirst()
                    .ifPresent(movie -> ctx.html(views.Movie.template(movie)
                            .render()
                            .toString()));
        });
    }

    private SimpleStream() {
    }

}
