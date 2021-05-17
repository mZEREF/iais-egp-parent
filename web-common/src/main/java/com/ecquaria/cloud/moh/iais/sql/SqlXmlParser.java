/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.sql;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.io.IOException;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SqlXmlParser
 *
 * @author Jinhua
 * @date 2019/7/18 17:55
 */
public class SqlXmlParser extends DefaultHandler {
    private static final String ELEM_SQLS       = "sqls";
    private static final String ELEM_SQL        = "sql";
    private static final String ATTR_CATALOG    = "catalog";
    private static final String ATTR_KEY        = "key";

    private List<Sql> sqls;
    private String catalog;
    private String key;
    private StringBuilder sqlStat; // accumulator

    public List<Sql> parseSqlXml(String xmlFileName) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        SAXParser sp = spf.newSAXParser();
//        sp.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        sp.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        sp.parse(xmlFileName, this);

        return sqls;
    }

    // Document Events - START
    @Override
    public void startDocument() throws SAXException {
        sqls = IaisCommonUtils.genNewArrayList();
        sqlStat = new StringBuilder();
    }

    // Element Events - START
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equalsIgnoreCase(ELEM_SQLS)) {
            catalog = attributes.getValue(ATTR_CATALOG);
            if (catalog == null) {
                catalog = "default";
            }
        } else if (qName.equalsIgnoreCase(ELEM_SQL)) {
            sqlStat.setLength(0);
            key = attributes.getValue(ATTR_KEY);
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        sqlStat.append(ch, start, length);
    }

    // Element Events - END
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(ELEM_SQL)) {
            // add it to the list
            Sql sql = new Sql();
            sql.setCatalog(catalog);
            sql.setKey(key);
            sql.setSqlStr(sqlStat.toString());

            sqls.add(sql);
        }
    }

    // Document Events - END
    @Override
    public void endDocument() throws SAXException {
        // end of parsing
    }
}
