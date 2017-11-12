/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.simplestream.config;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import net.kyori.blizzard.Immutable;
import net.kyori.blizzard.NonNull;

import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A model of the XML element representing the application
 * configuration.
 */
@XmlRootElement(name = "config")
@Immutable
public class SsConfig {

    /**
     * Creates a new {@link Builder} for use to construct a {@link SsConfig}.
     *
     * @return A new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Deserialises the given {@link Reader}, to a configuration model.
     *
     * @param reader The reader to read from
     * @return The configuration
     */
    public static SsConfig deserialise(@NonNull final Reader reader) {
        try {
            return (SsConfig) JAXBContext.newInstance(SsConfig.class).createUnmarshaller().unmarshal(reader);
        }
        catch (final JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * The default config conveniently packaged as a {@link SsConfig}.
     */
    public static final SsConfig DEFAULT_CONFIG = builder()
            .build();

    @XmlElement private final int port;
    @XmlElement(name = "movie") private final List<Movie> movies;

    /**
     * A no-arg construction for JAXB.
     */
    private SsConfig() {
        // Populate with default options, in case user's config has missing
        // config nodes
        this.port = 81;
        this.movies = Lists.newArrayList();
    }

    /**
     * Creates a new {@link SsConfig}, with the provided {@link Builder}.
     *
     * @param builder The builder, to populate the config model with
     */
    private SsConfig(@NonNull final Builder builder) {
        this.port = builder.port;
        this.movies = Lists.newArrayList();
    }

    /**
     * Gets the port that NeaWeb should be run on.
     *
     * @return The port
     */
    public final int getPort() {
        return this.port;
    }

    /**
     * Gets the movies of which can be watched on SimpleStream.
     *
     * @return The movies
     */
    public final List<Movie> getMovies() {
        return Collections.unmodifiableList(this.movies);
    }

    /**
     * Serialises the configuration, to the given {@link Writer} in the XML
     * format.
     *
     * @param writer The writer, to write to
     * @param <T> The type of the writer
     * @return The writer, for chaining
     */
    public <T extends Writer> T serialise(@NonNull final T writer) {
        try {
            final Marshaller marshaller = JAXBContext.newInstance(SsConfig.class).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, writer);
        }
        catch (final JAXBException ex) {
            throw new RuntimeException(ex);
        }
        return writer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.port);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SsConfig)) return false;
        final SsConfig that = (SsConfig) obj;
        return Objects.equals(this.port, that.port);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("port", this.port)
                .add("movies", this.movies)
                .toString();
    }

    /**
     * A builder used to construct a {@link SsConfig}.
     */
    public static final class Builder {

        private int port = 81;

        private Builder() {
        }

        /**
         * Sets the port.
         *
         * @param port The port
         * @return {@code this}, for chaining
         */
        public Builder port(final int port) {
            this.port = port;
            return this;
        }

        /**
         * Creates a new {@link SsConfig} based on the given parameters.
         *
         * @return The configuration model
         */
        public SsConfig build() {
            return new SsConfig(this);
        }

    }

}
