/*
 * MIT License
 *
 * Copyright (c) 2020 Artipie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.nuget.http.metadata;

import com.artipie.nuget.PackageId;
import com.artipie.nuget.Repository;
import com.artipie.nuget.http.Absent;
import com.artipie.nuget.http.Resource;
import com.artipie.nuget.http.Route;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Package metadata route.
 * See <a href="https://docs.microsoft.com/en-us/nuget/api/registration-base-url-resource">Package Metadata</a>
 *
 * @since 0.1
 */
public final class PackageMetadata implements Route {

    /**
     * Base path for the route.
     */
    private static final String BASE = "/registrations";

    /**
     * RegEx pattern for registration path.
     */
    private static final Pattern REGISTRATION = Pattern.compile(
        String.format("%s/(?<id>[^/]+)/index.json$", PackageMetadata.BASE)
    );

    /**
     * Repository to read data from.
     */
    private final Repository repository;

    /**
     * Package content location.
     */
    private final ContentLocation content;

    /**
     * Ctor.
     *
     * @param repository Repository to read data from.
     * @param content Package content storage.
     */
    public PackageMetadata(final Repository repository, final ContentLocation content) {
        this.repository = repository;
        this.content = content;
    }

    @Override
    public String path() {
        return PackageMetadata.BASE;
    }

    @Override
    public Resource resource(final String path) {
        final Matcher matcher = REGISTRATION.matcher(path);
        final Resource resource;
        if (matcher.find()) {
            resource = new Registration(
                this.repository,
                this.content,
                new PackageId(matcher.group("id"))
            );
        } else {
            resource = new Absent();
        }
        return resource;
    }
}
