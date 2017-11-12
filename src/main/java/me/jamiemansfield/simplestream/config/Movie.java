/*
 * Copyright (c) Jamie Mansfield - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package me.jamiemansfield.simplestream.config;

import com.google.common.base.MoreObjects;
import net.kyori.blizzard.Immutable;
import net.kyori.blizzard.NonNull;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A model of the XML element representing a movie.
 */
@XmlRootElement(name = "movie")
@Immutable
public class Movie {

    @NonNull @XmlAttribute private final String id;
    @NonNull @XmlAttribute private final String name;
    @NonNull @XmlAttribute private final String description;

    /**
     * A no-arg construction for JAXB.
     */
    private Movie() {
        this.id          = "";
        this.name        = "";
        this.description = "";
    }

    public final String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final String getDescription() {
        return this.description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Movie)) return false;
        final Movie that = (Movie) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", this.name)
                .add("description", this.description)
                .toString();
    }

}
