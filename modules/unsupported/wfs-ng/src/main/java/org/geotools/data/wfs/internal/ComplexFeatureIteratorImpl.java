/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.Feature;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;

/**
 * Defines the complex feature iterator implementation class. It's responsible for exposing an
 * iterator-style interface for complex features.
 *
 * @author Adam Brown (Curtin University of Technology)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ComplexFeatureIteratorImpl implements FeatureIterator<Feature> {

    private static final Logger LOGGER = Logging.getLogger(ComplexFeatureIteratorImpl.class);

    private XmlComplexFeatureParser parser;

    private Feature nextFeature;

    /**
     * Initialises a new instance of ComplexFeatureIteratorImpl.
     *
     * @param parser The feature parser to use.
     */
    public ComplexFeatureIteratorImpl(XmlComplexFeatureParser parser) {
        this.parser = parser;
        parseNext();
    }

    private void parseNext() {
        try {
            nextFeature = parser.parse();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public boolean hasNext() {
        return nextFeature != null;
    }

    @Override
    public Feature next() throws NoSuchElementException {
        if (nextFeature == null) {
            throw new NoSuchElementException();
        }
        final Feature returnFeature = nextFeature;
        parseNext();
        return returnFeature;
    }

    @Override
    public void close() {
        try {
            parser.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
